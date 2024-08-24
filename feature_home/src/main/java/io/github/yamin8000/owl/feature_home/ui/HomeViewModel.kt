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
    @Assisted("intent") private val intentSearch: String?,
    @Assisted("navigation") private val navigationSearch: String?
) : ViewModel() {
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
        viewModelScope.launch {
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
            HomeEvent.OnShareData -> viewModelScope.launch { shareChannel.send(state.value.searchResult.firstOrNull()) }

            is HomeEvent.OnTermChanged -> {
                savedState["Search"] = event.term
                viewModelScope.launch {
                    _state.update {
                        it.copy(searchSuggestions = termSuggesterRepository.suggestTerms(event.term))
                    }
                }
            }

            HomeEvent.CancelSearch -> cancel()
            HomeEvent.OnCheckInternet -> {
                viewModelScope.launch {
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

    private fun searchForRandomWord() = viewModelScope.launch {
        //searchForDefinition(getNewRandomWord())
    }

    private fun searchForDefinition(
        searchTerm: String
    ) = viewModelScope.launch {
        if (searchTerm.isNotBlank()) {
            historyUseCases.addHistory(searchTerm)
            _state.update {
                it.copy(isSearching = true)
            }
            val (result, error) = freeDictionaryUseCase(searchTerm)
            _state.update {
                it.copy(isSearching = false)
            }
            if (error == null) {
                _state.update {
                    it.copy(
                        searchResult = result,
                        searchSuggestions = emptyList()
                    )
                }
            } else handleError(error)
        } else errorChannel.send(HomeSnackbarType.TermIsEmpty)
    }

    private suspend fun handleError(
        error: Throwable
    ) {
        when (error) {
            is HttpException -> when (error.code()) {
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

    /*private suspend fun searchForDefinitionRequest(
        searchTerm: String
    ): List<Entry> {
        _isSearching.value = true
        _searchTerm.value = searchTerm

        val cache = findCachedDefinitionOrNull(searchTerm)
        if (cache == null) {
            return try {
                val result =
                    Web.getRetrofit().getAPI<APIs.FreeDictionaryAPI>().search(searchTerm.trim())
                val entry = result.firstOrNull()
                if (entry != null) {
                    addWordToDatabase(entry)
                    cacheEntryData(entry)
                }
                _searchState.emit(SearchState.RequestSucceed)
                result
            } catch (e: HttpException) {
                log(e.stackTraceToString())
                _searchState.emit(SearchState.RequestFailed(e.code()))
                listOf()
            } catch (e: CancellationException) {
                log(e.stackTraceToString())
                _searchState.emit(SearchState.RequestFailed(SearchState.CANCEL))
                listOf()
            } catch (e: Exception) {
                log(e.stackTraceToString())
                _searchState.emit(SearchState.RequestFailed(SearchState.UNKNOWN))
                listOf()
            } finally {
                _isSearching.value = false
            }
        } else {
            _isSearching.value = false
            _searchState.emit(SearchState.Cached)
            return listOf(cache)
        }
    }*/

    /*private suspend fun findCachedDefinitionOrNull(
        searchTerm: String
    ): Entry? {
        val entry = Constants.db.entryDao()
            .where("word", searchTerm.trim().lowercase())
            .firstOrNull()
        return if (entry != null) retrieveCachedWordData(entry) else null
    }*/

    /*private suspend fun retrieveCachedWordData(
        entry: EntryEntity
    ): Entry {
        val phonetics = Constants.db.phoneticDao().where("entryId", entry.id)
        val meanings = Constants.db.meaningDao().where("entryId", entry.id)
        val definitions = Constants.db.definitionDao().all()

        return Entry(
            word = entry.word,
            phonetics = phonetics.map { Phonetic(text = it.value) },
            license = License("", ""),
            sourceUrls = listOf(),
            meanings = meanings.map { (_, partOfSpeech, id) ->
                Meaning(
                    partOfSpeech = partOfSpeech,
                    antonyms = listOf(),
                    synonyms = listOf(),
                    definitions = definitions.filter { it.meaningId == id }.map {
                        Definition(
                            definition = it.definition,
                            example = it.example,
                            antonyms = listOf(),
                            synonyms = listOf()
                        )
                    }
                )
            }
        )
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
    }*/

    /*private suspend fun cacheEntryData(
        entry: Entry
    ) {
        val termDao = Constants.db.termDao()

        val oldData = termDao.all().map { it.word }.toSet()
        var newData = extractDataFromEntry(entry.meanings)

        if (!oldData.contains(entry.word))
            newData.add(entry.word)

        newData = sanitizeWords(newData).filter { it !in oldData }.toMutableSet()

        addWordDataToCache(newData)
    }*/

    /*private suspend fun addWordDataToCache(
        newData: Set<String>
    ) {
        val termDao = Constants.db.termDao()

        newData.forEach { item ->
            val temp = termDao.where("word", item.trim().lowercase()).firstOrNull()
            if (temp == null)
                termDao.insert(TermEntity(item.trim().lowercase()))
        }
    }*/

    /*private fun extractDataFromEntry(
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

    /*private suspend fun getNewRandomWord(): String {
        return Constants.db.entryDao().all().map { it.word }.shuffled().firstOrNull() ?: FREE
    }*/

    /*fun handleSuggestions() = ioScope.launch {
        clearSuggestions()
        if (_searchTerm.value.length >= Constants.DEFAULT_N_GRAM_SIZE) {
            val suggestions = termSuggestionsHelper.suggestTermsForSearch(_searchTerm.value)
            _searchSuggestions.value = suggestions.take(Constants.DEFAULT_N_GRAM_SIZE * 3)
        }
    }*/

    private fun cancel() {
        _state.update {
            it.copy(isSearching = false)
        }
        searchJob?.cancel()
    }
}