/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     HomeViewModel.kt Copyrighted by Yamin Siahmargooei at 2024/5/9
 *     HomeViewModel.kt Last modified at 2024/3/23
 *     This file is part of freeDictionaryApp/freeDictionaryApp.app.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.ui.content.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.yamin8000.owl.data.db.entity.DefinitionEntity
import io.github.yamin8000.owl.data.db.entity.EntryEntity
import io.github.yamin8000.owl.data.db.entity.MeaningEntity
import io.github.yamin8000.owl.data.db.entity.PhoneticEntity
import io.github.yamin8000.owl.data.db.entity.TermEntity
import io.github.yamin8000.owl.data.model.Definition
import io.github.yamin8000.owl.data.model.Entry
import io.github.yamin8000.owl.data.model.License
import io.github.yamin8000.owl.data.model.Meaning
import io.github.yamin8000.owl.data.model.Phonetic
import io.github.yamin8000.owl.network.APIs
import io.github.yamin8000.owl.network.Web
import io.github.yamin8000.owl.network.Web.getAPI
import io.github.yamin8000.owl.util.Constants
import io.github.yamin8000.owl.util.Constants.FREE
import io.github.yamin8000.owl.util.TermSuggestionsHelper
import io.github.yamin8000.owl.util.log
import io.github.yamin8000.owl.util.sanitizeWords
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import kotlin.coroutines.cancellation.CancellationException

internal class HomeViewModel(
    sentSearchTerm: String?,
    isStartingBlank: Boolean,
    private val termSuggestionsHelper: TermSuggestionsHelper
) : ViewModel() {
    private val ioScope = CoroutineScope(Dispatchers.IO)

    private var job: Job? = null

    private val _isOnline = MutableStateFlow(false)
    val isOnline = _isOnline.asStateFlow()

    private val _isSharing = MutableStateFlow(false)
    val isSharing = _isSharing.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchTerm = MutableStateFlow(sentSearchTerm ?: "")
    val searchTerm = _searchTerm.asStateFlow()

    private val _searchResult = MutableStateFlow(listOf<Entry>())
    val searchResult = _searchResult.asStateFlow()

    private val _searchSuggestions = MutableStateFlow(listOf<String>())
    val searchSuggestions = _searchSuggestions.asStateFlow()

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Unknown)
    val searchState = _searchState.asSharedFlow()

    private val _snackbarHostState = MutableStateFlow(SnackbarHostState())
    val snackbarHostState = _snackbarHostState.asStateFlow()

    val isWordSelectedFromKeyboardSuggestions: State<Boolean>
        get() = derivedStateOf { _searchTerm.value.length > 1 && _searchTerm.value.last() == ' ' && !_searchTerm.value.all { it == ' ' } }

    init {
        if (_searchTerm.value.isNotBlank()) {
            ioScope.launch { searchForDefinition(_searchTerm.value) }
        } else if (!isStartingBlank) {
            _searchTerm.value = "free"
            ioScope.launch { searchForDefinition(_searchTerm.value) }
        }
    }

    suspend fun resetSearchState() {
        _searchState.emit(SearchState.Unknown)
    }

    fun updateIsOnline(isOnline: Boolean) {
        _isOnline.value = isOnline
    }

    fun startWordSharing() {
        _isSharing.value = true
    }

    fun stopWordSharing() {
        _isSharing.value = false
    }

    fun updateSearchTerm(new: String) {
        _searchTerm.value = new
    }

    fun searchForRandomWord() = viewModelScope.launch {
        searchForDefinition(getNewRandomWord())
    }

    fun searchForDefinition(
        searchTerm: String
    ) = ioScope.launch {
        if (searchTerm.isNotBlank()) {
            job = ioScope.launch {
                _searchResult.value = searchForDefinitionRequest(searchTerm)
                val entry = searchResult.value.firstOrNull()
                if (entry != null) {
                    clearSuggestions()
                }
                _searchState.emit(SearchState.RequestFinished(searchTerm))
            }
        } else _searchState.emit(SearchState.RequestFailed(SearchState.EMPTY))
    }

    private suspend fun searchForDefinitionRequest(
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
    }

    private suspend fun findCachedDefinitionOrNull(
        searchTerm: String
    ): Entry? {
        val entry = Constants.db.entryDao()
            .where("word", searchTerm.trim().lowercase())
            .firstOrNull()
        return if (entry != null) retrieveCachedWordData(entry) else null
    }

    private suspend fun retrieveCachedWordData(
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
    }

    private suspend fun addWordToDatabase(
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
        .toMutableSet()

    private suspend fun getNewRandomWord(): String {
        return Constants.db.entryDao().all().map { it.word }.shuffled().firstOrNull() ?: FREE
    }

    fun handleSuggestions() = ioScope.launch {
        clearSuggestions()
        if (_searchTerm.value.length >= Constants.DEFAULT_N_GRAM_SIZE) {
            val suggestions = termSuggestionsHelper.suggestTermsForSearch(_searchTerm.value)
            _searchSuggestions.value = suggestions.take(Constants.DEFAULT_N_GRAM_SIZE * 3)
        }
    }

    fun clearSuggestions() {
        _searchSuggestions.value = listOf()
    }

    fun cancel() {
        _isSearching.value = false
        job?.cancel()
    }

    fun showSnackbar(message: String) {
        viewModelScope.launch {
            _snackbarHostState.value.showSnackbar(message)
        }
    }
}