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
import io.github.yamin8000.owl.db.entity.WordEntity
import io.github.yamin8000.owl.model.Definition
import io.github.yamin8000.owl.model.Entry
import io.github.yamin8000.owl.model.RandomWord
import io.github.yamin8000.owl.network.APIs
import io.github.yamin8000.owl.network.Web
import io.github.yamin8000.owl.network.Web.getAPI
import io.github.yamin8000.owl.util.AutoCompleteHelper
import io.github.yamin8000.owl.util.Constants
import io.github.yamin8000.owl.util.Constants.NOT_WORD_CHARS_REGEX
import io.github.yamin8000.owl.util.Constants.db
import io.github.yamin8000.owl.util.DataStoreHelper
import io.github.yamin8000.owl.util.DefinitionListSaver
import io.github.yamin8000.owl.util.ImmutableHolder
import io.github.yamin8000.owl.util.getImmutableHolderSaver
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.Locale
import java.util.Map.entry

class HomeState(
    val listState: ScrollState,
    val isSearching: MutableState<Boolean>,
    val lifecycleOwner: LifecycleOwner,
    private val focusManager: FocusManager,
    var searchText: String,
    val searchResult: MutableState<ImmutableHolder<List<Entry>>>,
    val entry: MutableState<Entry?>,
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
        }
    }

    val isFirstTimeOpening: Boolean
        get() = searchResult.value.item.isEmpty() && entry.value == null && searchText.isEmpty()

    val isWordSelectedFromKeyboardSuggestions: Boolean
        get() = searchText.length > 1 && searchText.last() == ' ' && !searchText.all { it == ' ' }

    fun searchForRandomWord() {
        job = scope.launch {
            isSearching.value = true
            focusManager.clearFocus()
            val randomWord = try {
                getNewRandomWord()
            } catch (e: HttpException) {
                snackbarHostState.showSnackbar(getErrorMessage(e.code(), context))
                null
            } catch (e: Exception) {
                snackbarHostState.showSnackbar(getErrorMessage(999, context))
                null
            } finally {
                isSearching.value = false
            }
            isSearching.value = false
            searchText = randomWord?.word ?: ""
            if (searchText.isNotBlank())
                searchForRandomWordDefinition()
        }
    }

    private suspend fun searchForDefinitionRequest(
        searchTerm: String
    ): List<Entry> {
        isSearching.value = true
        searchText = searchTerm
        val entries = try {
            Web.retrofit.getAPI<APIs.FreeDictionaryAPI>().search(searchTerm.trim())
        } catch (e: HttpException) {
            snackbarHostState.showSnackbar(getErrorMessage(e.code(), context))
            //val cache = checkIfDefinitionIsCached(searchTerm)
            //cache
            listOf()
        } catch (e: CancellationException) {
            listOf()
        } catch (e: Exception) {
            snackbarHostState.showSnackbar(getErrorMessage(999, context))
            //val cache = checkIfDefinitionIsCached(searchTerm)
            //cache
            listOf()
        } finally {
            isSearching.value = false
        }
        if (entries.isNotEmpty())
            focusManager.clearFocus()
        isSearching.value = false
        return entries
    }

    suspend fun searchForDefinitionHandler() {
        if (searchText.isNotBlank()) {
            searchForDefinition()
            addSearchTextToHistory()
        } else snackbarHostState.showSnackbar(getErrorMessage(998, context))
    }

    private suspend fun searchForRandomWordDefinition() {
        if (searchText.isNotBlank()) searchForDefinition()
        else snackbarHostState.showSnackbar(getErrorMessage(998, context))
    }

    suspend fun addSearchTextToHistory() {
        context.historyDataStore.edit {
            it[stringPreferencesKey(searchText)] = searchText
        }
    }

    fun searchForDefinition() {
        job = scope.launch {
            searchResult.value = ImmutableHolder(searchForDefinitionRequest(searchText))
            entry.value = searchResult.value.item.firstOrNull()
            clearSuggestions()
            if (entry.value != null) {
                entry.value?.let {
                    //addWordToDatabase(it)
                    //handleWordCacheableData(it)
                }
            }
        }
    }

    /*private suspend fun checkIfDefinitionIsCached(
        searchTerm: String
    ): Word? {
        val word = db.wordDao().getByParam("word", searchTerm.trim().lowercase()).firstOrNull()
        return if (word != null) retrieveCachedWordData(word) else null
    }*/

    /*private suspend fun retrieveCachedWordData(
        word: WordEntity
    ): Word? {
        val definitions = db.definitionDao().getByParam("wordId", word.id)
        if (definitions.isEmpty()) return null
        return Word(
            word.word,
            word.pronunciation,
            definitions.map {
                Definition(
                    type = it.type,
                    definition = it.definition,
                    example = it.example,
                    imageUrl = it.imageUrl,
                    emoji = it.emoji
                )
            }
        )
    }*/

    /*private suspend fun addWordToDatabase(
        word: Word
    ) {
        val wordDao = db.wordDao()
        val definitionDao = db.definitionDao()

        val cachedWord = wordDao.getByParam("word", word.word.trim().lowercase()).firstOrNull()
        if (cachedWord == null) {
            val wordId = wordDao.insert(
                WordEntity(
                    word.word.trim().lowercase(),
                    word.pronunciation?.trim()?.lowercase()
                )
            )
            definitionDao.insertAll(
                word.definitions.map {
                    DefinitionEntity(
                        type = it.type,
                        definition = it.definition,
                        example = it.example,
                        imageUrl = it.imageUrl,
                        emoji = it.emoji,
                        wordId = wordId
                    )
                }
            )
        }
    }*/

    /*private suspend fun handleWordCacheableData(
        word: Word
    ) {
        val wordDao = db.wordDao()

        val oldData = wordDao.getAll().map { it.word }.toSet()
        var newData = extractDataFromWordDefinitions(word.definitions)

        if (oldData.contains(word.word).not())
            newData.add(word.word)

        newData = sanitizeWords(newData)

        addWordDataToCache(newData)
    }*/

    private suspend fun addWordDataToCache(
        newData: MutableSet<String>
    ) {
        val wordDao = db.wordDao()

        newData.forEach { item ->
            val temp = wordDao.getByParam("word", item.trim().lowercase()).firstOrNull()
            if (temp == null)
                wordDao.insert(WordEntity(item.trim().lowercase()))
        }
    }

    private fun extractDataFromWordDefinitions(
        definitions: List<Definition>
    ) = definitions.asSequence()
        .flatMap { listOf(it.definition, it.example) }
        .mapNotNull { it?.split(Regex("\\s+")) }
        .flatten()
        .toMutableSet()

    private fun sanitizeWords(
        data: Set<String>
    ) = data.asSequence()
        .map { it.lowercase() }
        .map { it.replace(NOT_WORD_CHARS_REGEX, "") }
        .toMutableSet()

    private suspend fun getNewRandomWord(): RandomWord {
        return Web.ninjaApiRetrofit.getAPI<APIs.NinjaAPI>().getRandomWord()
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
        append("\n")
        append("Pronunciation(IPA): ")
        append(entry.value?.phonetics?.firstOrNull { it.text != null }?.text ?: "-")
        append("\n\n")
        /*entry { index, item ->
            if (searchResult.value.item.size > 1)
                append("${index + 1})\n")
            append("Definition: ${item.definition}\n\n")
            item.type?.let { append("Type: $it\n\n") }
            item.example?.let { append("Example: $it\n\n") }
            item.emoji?.let { append("Emoji: $it") }
        }*/
        trim()
        append("\n\n")
        append(context.getString(R.string.this_text_generated_using_owl))
        append("\n")
        append(context.getString(R.string.github_source))
        append("\n")
        append(context.getString(R.string.this_text_extracted_from_free_dictionary))
        append("\n")
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
        if (searchText.length >= Constants.DEFAULT_N_GRAM_SIZE) {
            val suggestions = autoCompleteHelper.suggestTermsForSearch(searchText)
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
    searchText: String = rememberSaveable { mutableStateOf("").value },
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
