/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     HomeViewModel.kt Copyrighted by Yamin Siahmargooei at 2024/8/17
 *     HomeViewModel.kt Last modified at 2024/6/2
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_home.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_home.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.yamin8000.owl.datastore.domain.usecase.history.HistoryUseCases
import io.github.yamin8000.owl.datastore.domain.usecase.settings.SettingUseCases
import io.github.yamin8000.owl.feature_home.di.HomeViewModelFactory
import io.github.yamin8000.owl.feature_home.domain.model.Entry
import io.github.yamin8000.owl.feature_home.domain.repository.TermSuggesterRepository
import io.github.yamin8000.owl.feature_home.domain.usecase.FreeDictionaryUseCase
import io.github.yamin8000.owl.feature_home.domain.usecase.GetRandomWordUseCase
import io.github.yamin8000.owl.feature_home.domain.usecase.WordCacheUseCases
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel(assistedFactory = HomeViewModelFactory::class)
class HomeViewModel @AssistedInject constructor(
    private val savedState: SavedStateHandle,
    private val freeDictionaryUseCase: FreeDictionaryUseCase,
    private val termSuggesterRepository: TermSuggesterRepository,
    private val settingsUseCases: SettingUseCases,
    private val historyUseCases: HistoryUseCases,
    private val cacheUseCases: WordCacheUseCases,
    private val randomWordUseCase: GetRandomWordUseCase,
    @Assisted("intent") private val intentSearch: String?,
    @Assisted("navigation") private val navigationSearch: String?
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _state.update { it.copy(isSearching = false) }
        viewModelScope.launch {
            when (throwable) {
                is HttpException -> when (throwable.code()) {
                    401 -> errorChannel.send(HomeSnackbarType.ApiAuthorizationError)
                    404 -> errorChannel.send(HomeSnackbarType.NotFound)
                    429 -> errorChannel.send(HomeSnackbarType.ApiThrottled)
                    else -> errorChannel.send(HomeSnackbarType.Unknown)
                }

                is CancellationException -> errorChannel.send(HomeSnackbarType.Cancelled)
                is UnknownHostException -> errorChannel.send(HomeSnackbarType.NoInternet)
                else -> errorChannel.send(HomeSnackbarType.Unknown)
            }
        }
    }

    private val scope = CoroutineScope(viewModelScope.coroutineContext + exceptionHandler)

    val searchTerm = savedState.getStateFlow("Search", intentSearch ?: navigationSearch ?: "")

    private var errorChannel = Channel<HomeSnackbarType>()
    val errorChannelFlow = errorChannel.receiveAsFlow()

    private var shareChannel = Channel<Entry?>()
    val shareChannelFlow = shareChannel.receiveAsFlow()

    private var _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private var searchJob: Job? = null

    val isWordSelectedFromKeyboardSuggestions: State<Boolean>
        get() = derivedStateOf { searchTerm.value.length > 1 && searchTerm.value.last() == ' ' && !searchTerm.value.all { it == ' ' } }

    private val internetCheckDelay = 5000L
    private val dnsServers = listOf(
        "8.8.8.8",
        "8.8.4.4",
        "1.1.1.1",
        "1.0.0.1",
        "185.51.200.2",
        "178.22.122.100",
        "10.202.10.202",
        "10.202.10.102"
    )

    init {
        scope.launch {
            _state.update { it.copy(isVibrating = settingsUseCases.getVibration()) }
            if (!settingsUseCases.getStartingBlank()) {
                savedState["Search"] = "free"
            }
            if (searchTerm.value.isNotBlank()) {
                searchForDefinition(searchTerm.value)
            }
            while (true) {
                onEvent(HomeEvent.OnCheckInternet)
                delay(internetCheckDelay)
            }
        }
    }

    fun onEvent(
        event: HomeEvent
    ) {
        when (event) {
            is HomeEvent.NewSearch -> {
                val term = event.searchTerm ?: searchTerm.value
                searchJob = searchForDefinition(term)
            }

            HomeEvent.RandomWord -> searchForRandomWord()
            HomeEvent.OnShareData -> scope.launch { shareChannel.send(state.value.searchResult.firstOrNull()) }

            is HomeEvent.OnTermChanged -> {
                savedState["Search"] = event.term
                scope.launch {
                    _state.update {
                        it.copy(searchSuggestions = termSuggesterRepository.suggestTerms(event.term))
                    }
                }
            }

            HomeEvent.CancelSearch -> cancel()
            HomeEvent.OnCheckInternet -> {
                scope.launch {
                    _state.update { stateUpdate ->
                        stateUpdate.copy(isOnline = dnsServers.any { dnsAccessible(it) })
                    }
                }
            }

            is HomeEvent.OnAddToFavourite -> {

            }
        }
    }

    private suspend fun dnsAccessible(
        dnsServer: String
    ) = try {
        withContext(Dispatchers.IO) {
            Runtime.getRuntime().exec("/system/bin/ping -c 1 $dnsServer").waitFor()
        } == 0
    } catch (e: Exception) {
        false
    }

    private fun searchForRandomWord() = scope.launch {
        searchForDefinition(randomWordUseCase())
    }

    private fun searchForDefinition(
        searchTerm: String
    ) = scope.launch {
        if (searchTerm.isNotBlank()) {
            historyUseCases.addHistory(searchTerm)

            _state.update { it.copy(isSearching = true) }

            val cachedWord = cacheUseCases.getCachedWord(searchTerm)
            if (cachedWord != null) loadCachedWord(cachedWord)
            else searchForDefinitionUsingApi(searchTerm)

            _state.update { it.copy(isSearching = false) }
        } else errorChannel.send(HomeSnackbarType.TermIsEmpty)
    }

    private fun loadCachedWord(cachedWord: Entry) {
        _state.update {
            it.copy(
                searchResult = listOf(cachedWord),
                searchSuggestions = emptyList()
            )
        }
    }

    private suspend fun searchForDefinitionUsingApi(
        searchTerm: String
    ) {
        _state.update {
            it.copy(
                searchResult = freeDictionaryUseCase(searchTerm),
                searchSuggestions = emptyList()
            )
        }
    }

    /*private suspend fun searchForDefinitionRequest(
        searchTerm: String
    ): List<Entry> {
        *//*val cache = findCachedDefinitionOrNull(searchTerm)
        if (cache == null) {
        } else {
            return listOf(cache)
        }*//*
    }*/

    /*private suspend fun addWordToDatabase(
        entry: Entry
    ) {
        val wordDao = Constants.db.entryDao()
        val meaningDao = Constants.db.meaningDao()
        val definitionDao = Constants.db.definitionDao()
        val phoneticDao = Constants.db.phoneticDao()

        val cachedWord = wordDao.where("word", entry.word.trim().lowercase()).firstOrNull()
        if (cachedWord == null) {
            val entryId = wordDao.insert(
                EntryEntity(word = entry.word.trim().lowercase())
            )
            phoneticDao.insertAll(
                entry.phonetics.map { PhoneticEntity(value = it.text, entryId = entryId) }
            )

            entry.meanings.forEach { (partOfSpeech, definitions, _, _) ->
                val meaningEntity = MeaningEntity(
                    entryId = entryId,
                    partOfSpeech = partOfSpeech
                )
                val meaningId = meaningDao.insert(meaningEntity)
                definitionDao.insertAll(
                    definitions.map {
                        DefinitionEntity(
                            meaningId = meaningId,
                            definition = it.definition,
                            example = it.example
                        )
                    }
                )
            }
        }
    }

    private suspend fun cacheEntryData(
        entry: Entry
    ) {
        val termDao = Constants.db.termDao()

        val oldData = termDao.all().map { it.word }.toSet()
        var newData = extractDataFromEntry(entry.meanings)

        if (!oldData.contains(entry.word))
            newData.add(entry.word)

        newData = sanitizeWords(newData).filter { it !in oldData }.toMutableSet()

        addWordDataToCache(newData)
    }

    private suspend fun addWordDataToCache(
        newData: Set<String>
    ) {
        val termDao = Constants.db.termDao()

        newData.forEach { item ->
            val temp = termDao.where("word", item.trim().lowercase()).firstOrNull()
            if (temp == null)
                termDao.insert(TermEntity(item.trim().lowercase()))
        }
    }

    private fun extractDataFromEntry(
        meanings: List<Meaning>
    ) = meanings.asSequence()
        .flatMap { (partOfSpeech, definitions, _, _) ->
            listOf(partOfSpeech)
                .plus(definitions.map { it.definition })
                .plus(definitions.map { it.example })
                .plus(definitions.flatMap { it.synonyms })
                .plus(definitions.flatMap { it.antonyms })
        }.filterNotNull()
        .map { it.split(Regex("\\s+")) }
        .flatten()
        .map { it.trim() }
        .toMutableSet()*/

    private fun cancel() {
        _state.update {
            it.copy(isSearching = false)
        }
        searchJob?.cancel()
    }
}