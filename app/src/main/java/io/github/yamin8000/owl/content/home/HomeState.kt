/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     HomeState.kt Copyrighted by Yamin Siahmargooei at 2023/8/26
 *     HomeState.kt Last modified at 2023/8/26
 *     This file is part of freeDictionaryApp/freeDictionaryApp.app.main.
 *     Copyright (C) 2023  Yamin Siahmargooei
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

package io.github.yamin8000.owl.content.home

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.content.favouritesDataStore
import io.github.yamin8000.owl.content.historyDataStore
import io.github.yamin8000.owl.content.settingsDataStore
import io.github.yamin8000.owl.db.entity.DefinitionEntity
import io.github.yamin8000.owl.db.entity.EntryEntity
import io.github.yamin8000.owl.db.entity.MeaningEntity
import io.github.yamin8000.owl.db.entity.PhoneticEntity
import io.github.yamin8000.owl.model.Definition
import io.github.yamin8000.owl.model.Entry
import io.github.yamin8000.owl.model.License
import io.github.yamin8000.owl.model.Meaning
import io.github.yamin8000.owl.model.Phonetic
import io.github.yamin8000.owl.network.APIs
import io.github.yamin8000.owl.network.Web
import io.github.yamin8000.owl.network.Web.getAPI
import io.github.yamin8000.owl.util.AutoCompleteHelper
import io.github.yamin8000.owl.util.Constants
import io.github.yamin8000.owl.util.Constants.db
import io.github.yamin8000.owl.util.DataStoreHelper
import io.github.yamin8000.owl.util.DefinitionListSaver
import io.github.yamin8000.owl.util.ImmutableHolder
import io.github.yamin8000.owl.util.getImmutableHolderSaver
import io.github.yamin8000.owl.util.sanitizeWords
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.Locale

private const val FREE = "free"

class HomeState(
    val listState: ScrollState,
    val isSearching: MutableState<Boolean>,
    val lifecycleOwner: LifecycleOwner,
    private val focusManager: FocusManager,
    var searchText: MutableState<String>,
    val searchResult: MutableState<ImmutableHolder<List<Entry>>>,
    private val entry: MutableState<Entry?>,
    val context: Context,
    val isSharing: MutableState<Boolean>,
    val ttsLang: MutableState<String>,
    val searchSuggestions: MutableState<ImmutableHolder<List<String>>>,
    val isOnline: MutableState<Boolean>,
    val isVibrating: MutableState<Boolean>
) {
    val scope = lifecycleOwner.lifecycleScope

    val snackbarHostState: SnackbarHostState = SnackbarHostState()

    private val alreadyAddedToFavourites = context.getString(R.string.already_added_to_favourites)

    private val autoCompleteHelper = AutoCompleteHelper(context, scope)

    private val dataStore = DataStoreHelper(context.settingsDataStore)

    private var job: Job? = null

    init {
        scope.launch {
            ttsLang.value = dataStore.getString(Constants.TTS_LANG) ?: Locale.US.toLanguageTag()
            isVibrating.value = dataStore.getBool(Constants.IS_VIBRATING) ?: true
            val isBlank = dataStore.getBool(Constants.IS_STARTING_BLANK) ?: true
            if (!isBlank) {
                searchText.value = FREE
                searchForDefinition()
            }
        }
    }

    val isWordSelectedFromKeyboardSuggestions: Boolean
        get() = searchText.value.length > 1 && searchText.value.last() == ' ' && !searchText.value.all { it == ' ' }

    suspend fun searchForRandomWord() {
        searchText.value = getNewRandomWord()
        searchForDefinition()
    }

    private suspend fun searchForDefinitionRequest(
        searchTerm: String
    ): List<Entry> {
        isSearching.value = true
        searchText.value = searchTerm
        val entries = try {
            Web.retrofit.getAPI<APIs.FreeDictionaryAPI>().search(searchTerm.trim())
        } catch (e: HttpException) {
            snackbarHostState.showSnackbar(getErrorMessage(e.code(), context))
            val cache = checkIfDefinitionIsCached(searchTerm)
            listOf(cache)
        } catch (e: CancellationException) {
            listOf()
        } catch (e: Exception) {
            snackbarHostState.showSnackbar(getErrorMessage(999, context))
            val cache = checkIfDefinitionIsCached(searchTerm)
            listOf(cache)
        } finally {
            isSearching.value = false
        }
        if (entries.isNotEmpty())
            focusManager.clearFocus()
        isSearching.value = false
        return entries.filterNotNull()
    }

    suspend fun searchForDefinitionHandler() {
        if (searchText.value.isNotBlank()) {
            searchForDefinition()
            addSearchTextToHistory()
        } else snackbarHostState.showSnackbar(getErrorMessage(998, context))
    }

    suspend fun addSearchTextToHistory() {
        context.historyDataStore.edit {
            it[stringPreferencesKey(searchText.value)] = searchText.value
        }
    }

    fun searchForDefinition() {
        job = scope.launch {
            searchResult.value = ImmutableHolder(searchForDefinitionRequest(searchText.value))
            entry.value = searchResult.value.item.firstOrNull()
            clearSuggestions()
            if (entry.value != null) {
                entry.value?.let {
                    addWordToDatabase(it)
                    cacheEntryData(it)
                }
            }
        }
    }

    private suspend fun checkIfDefinitionIsCached(
        searchTerm: String
    ): Entry? {
        val entry = db.entryDao().getByParam("word", searchTerm.trim().lowercase()).firstOrNull()
        return if (entry != null) retrieveCachedWordData(entry) else null
    }

    private suspend fun retrieveCachedWordData(
        entry: EntryEntity
    ): Entry {
        val phonetics = db.phoneticDao().getByParam("entryId", entry.id)
        val meanings = db.meaningDao().getByParam("entryId", entry.id)
        val definitions = db.definitionDao().getAll()

        return Entry(
            word = entry.word,
            phonetics = phonetics.map { Phonetic(text = it.value) },
            license = License("", ""),
            sourceUrls = listOf(),
            meanings = meanings.map { meaning ->
                Meaning(
                    partOfSpeech = meaning.partOfSpeech,
                    antonyms = listOf(),
                    synonyms = listOf(),
                    definitions = definitions.filter { it.meaningId == meaning.id }.map {
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
        val wordDao = db.entryDao()
        val meaningDao = db.meaningDao()
        val definitionDao = db.definitionDao()
        val phoneticDao = db.phoneticDao()

        val cachedWord = wordDao.getByParam("word", entry.word.trim().lowercase()).firstOrNull()
        if (cachedWord == null) {
            val entryId = wordDao.insert(
                EntryEntity(word = entry.word.trim().lowercase())
            )
            phoneticDao.insertAll(
                entry.phonetics.map { PhoneticEntity(value = it.text, entryId = entryId) }
            )

            entry.meanings.forEach { meaning ->
                val definitions = meaning.definitions
                val meaningEntity = MeaningEntity(
                    entryId = entryId,
                    partOfSpeech = meaning.partOfSpeech
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
        val entryDao = db.entryDao()

        val oldData = entryDao.getAll().map { it.word }.toSet()
        var newData = extractDataFromEntry(entry.meanings)

        if (!oldData.contains(entry.word))
            newData.add(entry.word)

        newData = sanitizeWords(newData).filter { it !in oldData }.toMutableSet()

        addWordDataToCache(newData)
    }

    private suspend fun addWordDataToCache(
        newData: MutableSet<String>
    ) {
        val entryDao = db.entryDao()

        newData.forEach { item ->
            val temp = entryDao.getByParam("word", item.trim().lowercase()).firstOrNull()
            if (temp == null)
                entryDao.insert(EntryEntity(item.trim().lowercase()))
        }
    }

    private fun extractDataFromEntry(
        meanings: List<Meaning>
    ) = meanings.asSequence()
        .flatMap { meaning ->
            listOf(meaning.partOfSpeech)
                .plus(meaning.definitions.map { it.definition })
                .plus(meaning.definitions.map { it.example })
                .plus(meaning.definitions.flatMap { it.synonyms })
                .plus(meaning.definitions.flatMap { it.antonyms })
        }.filterNotNull()
        .map { it.split(Regex("\\s+")) }
        .flatten()
        .toMutableSet()

    private suspend fun getNewRandomWord(): String {
        return db.entryDao().getAll().map { it.word }.shuffled().firstOrNull() ?: FREE
    }

    suspend fun addToFavourite(
        favouriteWord: String
    ) {
        val wordInDataStore = findWordInDataStore(favouriteWord)
        if (wordInDataStore == null) addFavouriteWordToDataStore(favouriteWord)
        else snackbarHostState.showSnackbar(alreadyAddedToFavourites)
    }

    private suspend fun addFavouriteWordToDataStore(
        favouriteWord: String
    ) {
        context.favouritesDataStore.edit {
            it[stringPreferencesKey(favouriteWord)] = favouriteWord
        }
    }

    private suspend fun findWordInDataStore(
        favouriteWord: String
    ) = getFavourites()[stringPreferencesKey(favouriteWord)]

    private suspend fun getFavourites() = context.favouritesDataStore.data.first()

    private fun createShareText() = buildString {
        append("Word: ")
        append(entry.value?.word ?: "-")
        appendLine()
        append("Pronunciation(IPA): ")
        append(entry.value?.phonetics?.firstOrNull { it.text != null }?.text ?: "-")
        appendLine()
        appendLine()
        entry.value?.meanings?.forEachIndexed { index, meaning ->
            appendLine("${index + 1})")
            appendLine("Type: ${meaning.partOfSpeech}")
            meaning.definitions.take(5).forEach { definition ->
                appendLine("Definition: ${definition.definition}")
                if (definition.example != null)
                    appendLine("Example: ${definition.example}")
                if (definition.synonyms.isNotEmpty())
                    appendLine("Synonyms: ${definition.synonyms.take(5).joinToString()}")
                if (definition.antonyms.isNotEmpty())
                    appendLine("Antonyms: ${definition.antonyms.take(5).joinToString()}")
                appendLine()
            }
            appendLine()
        }
        trim()
        appendLine(context.getString(R.string.this_text_generated_using_owl))
        appendLine(context.getString(R.string.github_source))
        appendLine(context.getString(R.string.this_text_extracted_from_free_dictionary))
        append(context.getString(R.string.free_dictionary_link))
    }

    private fun getErrorMessage(
        code: Int,
        context: Context
    ) = when (code) {
        401 -> context.getString(R.string.api_authorization_error)
        404 -> context.getString(R.string.definition_not_found)
        429 -> context.getString(R.string.api_throttled)
        998 -> context.getString(R.string.no_search_term_entered)
        999 -> context.getString(R.string.untracked_error)
        else -> context.getString(R.string.general_net_error)
    }

    fun handleShareIntent() {
        isSharing.value = false
        val text = createShareText()

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    suspend fun handleSuggestions() {
        clearSuggestions()
        if (searchText.value.length >= Constants.DEFAULT_N_GRAM_SIZE) {
            val suggestions = autoCompleteHelper.suggestTermsForSearch(searchText.value)
            searchSuggestions.value = ImmutableHolder(
                suggestions.take(Constants.DEFAULT_N_GRAM_SIZE * 3)
            )
        }
    }

    fun clearSuggestions() {
        searchSuggestions.value = ImmutableHolder(listOf())
    }

    fun cancel() {
        job?.cancel()
        isSearching.value = false
    }
}

@Composable
fun rememberHomeState(
    listState: ScrollState = rememberScrollState(),
    isSearching: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    focusManager: FocusManager = LocalFocusManager.current,
    searchText: MutableState<String> = rememberSaveable { mutableStateOf("") },
    searchResult: MutableState<ImmutableHolder<List<Entry>>> = rememberSaveable(stateSaver = DefinitionListSaver) {
        mutableStateOf(ImmutableHolder(emptyList()))
    },
    entry: MutableState<Entry?> = rememberSaveable { mutableStateOf(null) },
    context: Context = LocalContext.current,
    isSharing: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    ttsLang: MutableState<String> = rememberSaveable { mutableStateOf(Locale.US.toLanguageTag()) },
    searchSuggestions: MutableState<ImmutableHolder<List<String>>> = rememberSaveable(stateSaver = getImmutableHolderSaver()) {
        mutableStateOf(ImmutableHolder(emptyList()))
    },
    isOnline: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    isVibrating: MutableState<Boolean> = rememberSaveable { mutableStateOf(true) }
) = remember(
    listState,
    isSearching,
    lifecycleOwner,
    focusManager,
    searchText,
    searchResult,
    entry,
    context,
    isSharing,
    ttsLang,
    searchSuggestions,
    isOnline,
    isVibrating
) {
    HomeState(
        listState,
        isSearching,
        lifecycleOwner,
        focusManager,
        searchText,
        searchResult,
        entry,
        context,
        isSharing,
        ttsLang,
        searchSuggestions,
        isOnline,
        isVibrating
    )
}
