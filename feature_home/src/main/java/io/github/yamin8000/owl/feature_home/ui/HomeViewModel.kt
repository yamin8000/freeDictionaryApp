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
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.yamin8000.owl.common.ui.navigation.Nav
import io.github.yamin8000.owl.feature_home.domain.usecase.FreeDictionaryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class HomeViewModel @Inject constructor(
    //private val termSuggestionsHelper: TermSuggestionsHelper
    private val useCase: FreeDictionaryUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val searchTerm = savedStateHandle.getStateFlow(Nav.Arguments.Search.toString(), "")

    private var _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private var job: Job? = null

    private val _searchSuggestions = MutableStateFlow(listOf<String>())
    val searchSuggestions = _searchSuggestions.asStateFlow()

    val isWordSelectedFromKeyboardSuggestions: State<Boolean>
        get() = derivedStateOf { searchTerm.value.length > 1 && searchTerm.value.last() == ' ' && !searchTerm.value.all { it == ' ' } }

    private val onlineCheckDelay = 5000L
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
        /*if (_searchTerm.value.isNotBlank()) {
            ioScope.launch { searchForDefinition(_searchTerm.value) }
        } else if (!isStartingBlank) {
            _searchTerm.value = "free"
            ioScope.launch { searchForDefinition(_searchTerm.value) }
        }*/

        viewModelScope.launch {
            repeat(Int.MAX_VALUE) {
                _state.update { stateUpdate ->
                    val isOnline = dnsServers.any { dnsAccessible(it) }
                    if (isOnline) {
                        stateUpdate.copy(error = null)
                    } else {
                        stateUpdate.copy(error = HomeError.NoInternet)
                    }
                }
                delay(onlineCheckDelay)
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

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.NewSearch -> {
                job = searchForDefinition(searchTerm.value)
            }

            HomeEvent.RandomWord -> searchForRandomWord()
            HomeEvent.SearchSucceed -> {}
            HomeEvent.OnShareData -> {
                _state.update {
                    it.copy(isSharing = true)
                }
            }

            is HomeEvent.OnTermChanged -> {
                savedStateHandle[Nav.Arguments.Search.toString()] = event.term
            }

            HomeEvent.CancelSearch -> cancel()
        }
    }

    private fun searchForRandomWord() = viewModelScope.launch {
        //searchForDefinition(getNewRandomWord())
    }

    private fun searchForDefinition(
        searchTerm: String
    ) = viewModelScope.launch {
        if (searchTerm.isNotBlank()) {
            _state.update {
                it.copy(isSearching = true)
            }
            val (result, error) = useCase(searchTerm)
            _state.update {
                it.copy(isSearching = false)
            }
            if (error == null) {
                _state.update {
                    it.copy(searchResult = result)
                }
            } else {
                when (error) {
                    is HttpException -> {

                    }

                    is CancellationException -> {

                    }

                    is UnknownHostException -> {

                    }

                    else -> {

                    }
                }
                _state.update {
                    it.copy(error = HomeError.SearchFailed)
                }
            }
        } else {
            _state.update {
                it.copy(error = HomeError.TermIsEmpty)
            }
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

    fun clearSuggestions() {
        _searchSuggestions.value = listOf()
    }

    private fun cancel() {
        _state.update {
            it.copy(isSearching = false)
        }
        job?.cancel()
    }
}