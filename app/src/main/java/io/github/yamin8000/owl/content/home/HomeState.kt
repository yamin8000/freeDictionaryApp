/*
 *     Owl: an android app for Owlbot Dictionary API
 *     HomeState.kt Created by Yamin Siahmargooei at 2022/8/22
 *     This file is part of Owl.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Owl is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Owl is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Owl.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.content.home

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import io.github.yamin8000.owl.model.Definition
import io.github.yamin8000.owl.model.RandomWord
import io.github.yamin8000.owl.model.Word
import io.github.yamin8000.owl.network.APIs
import io.github.yamin8000.owl.network.Web
import io.github.yamin8000.owl.network.Web.getAPI
import io.github.yamin8000.owl.util.*
import io.github.yamin8000.owl.util.Constants.NOT_WORD_CHARS_REGEX
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.File
import java.util.*

class HomeState(
    val listState: LazyListState,
    val isSearching: MutableState<Boolean>,
    val lifecycleOwner: LifecycleOwner,
    private val focusManager: FocusManager,
    var searchText: String,
    val rawWordSearchBody: MutableState<Word?>,
    val searchResult: MutableState<ImmutableHolder<List<Definition>>>,
    val context: Context,
    val isSharing: MutableState<Boolean>,
    val ttsLang: MutableState<String>,
    val searchSuggestions: MutableState<ImmutableHolder<List<String>>>
) {
    private val autoCompleteHelper = AutoCompleteHelper(context)

    val scope = lifecycleOwner.lifecycleScope
    private val lifeCycleContext = scope.coroutineContext

    private val dataStore = DataStoreHelper(context.settingsDataStore)

    val snackbarHostState: SnackbarHostState = SnackbarHostState()

    init {
        scope.launch {
            ttsLang.value = dataStore.getString(Constants.TTS_LANG) ?: Locale.US.toLanguageTag()
        }
    }

    val isFirstTimeOpening: Boolean
        get() = searchResult.value.item.isEmpty() && rawWordSearchBody.value == null && searchText.isEmpty()

    val isWordSelectedFromKeyboardSuggestions: Boolean
        get() = searchText.length > 1 && searchText.last() == ' ' && !searchText.all { it == ' ' }

    suspend fun searchForRandomWord() {
        isSearching.value = true
        focusManager.clearFocus()
        val randomWord = withContext(lifeCycleContext) {
            try {
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
        }
        isSearching.value = false
        searchText = randomWord?.word ?: ""
        if (searchText.isNotBlank())
            withContext(lifeCycleContext) { searchForRandomWordDefinition() }
    }

    private suspend fun searchForDefinitionRequest(
        searchTerm: String
    ): Word? {
        isSearching.value = true
        searchText = searchTerm
        val body = withContext(lifeCycleContext) {
            try {
                Web.retrofit.getAPI<APIs.OwlBotWordAPI>().searchWord(searchTerm.trim())
            } catch (e: HttpException) {
                snackbarHostState.showSnackbar(getErrorMessage(e.code(), context))
                null
            } catch (e: Exception) {
                snackbarHostState.showSnackbar(getErrorMessage(999, context))
                null
            } finally {
                isSearching.value = false
            }
        }
        if (body != null)
            focusManager.clearFocus()
        isSearching.value = false
        return body
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

    private suspend fun addSearchTextToHistory() {
        context.historyDataStore.edit {
            it[stringPreferencesKey(searchText)] = searchText
        }
    }

    suspend fun searchForDefinition() {
        rawWordSearchBody.value = withContext(lifeCycleContext) {
            searchForDefinitionRequest(searchText)
        }
        searchResult.value = ImmutableHolder(rawWordSearchBody.value?.definitions ?: listOf())
        searchResult.value =
            ImmutableHolder(searchResult.value.item.sortedByDescending { it.imageUrl })
        clearSuggestions()

        if (rawWordSearchBody.value != null)
            rawWordSearchBody.value?.let { addDataToWordsCache(it) }
    }

    private fun addDataToWordsCache(
        word: Word
    ) {
        val file = File(context.cacheDir, Constants.WORDS_TEXT_FILE)
        val oldData = readSavedWordsFromFile(file)

        var data = mutableSetOf<String>()
        word.definitions.map { it.definition }.forEach {
            data.addAll(it.split(Regex("\\s+")))
        }
        word.definitions.map { it.example }.forEach {
            if (it != null) data.addAll(it.split(Regex("\\s+")))
        }

        if (!oldData.contains(word.word))
            data.add(word.word)

        data = sanitizeWords(data, oldData)

        addWordsToFileCache(data, file)
    }

    private fun addWordsToFileCache(
        data: MutableSet<String>,
        file: File
    ) {
        data.forEach { item ->
            if (item.isNotBlank()) {
                file.appendText(",")
                file.appendText(item)
            }
        }
    }

    private fun sanitizeWords(
        data: Set<String>,
        oldData: Set<String>
    ): MutableSet<String> {
        return data.asSequence()
            .map { it.lowercase() }
            .map { it.replace(NOT_WORD_CHARS_REGEX, "") }
            .filter { it !in oldData }.toMutableSet()
    }

    private fun readSavedWordsFromFile(file: File): Set<String> {
        return if (file.exists()) file.readText().split(',').filter { it.isNotBlank() }.toSet()
        else setOf()
    }

    private suspend fun getNewRandomWord(): RandomWord {
        return Web.ninjaApiRetrofit.getAPI<APIs.NinjaAPI>().getRandomWord()
    }

    suspend fun addToFavourite(
        favouriteWord: String
    ) {
        val wordInDataStore = withContext(lifeCycleContext) {
            findWordInDataStore(favouriteWord)
        }
        if (wordInDataStore == null) addFavouriteWordToDataStore(favouriteWord)
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

    private fun createShareText() = buildString {
        append("Word: ")
        append(rawWordSearchBody.value?.word ?: "-")
        append("\n")
        append("Pronunciation(IPA): ")
        append(rawWordSearchBody.value?.pronunciation ?: "-")
        append("\n\n")
        searchResult.value.item.forEachIndexed { index, item ->
            if (searchResult.value.item.size > 1)
                append("${index + 1})\n")
            append("Definition: ${item.definition}\n\n")
            item.type?.let { append("Type: $it\n\n") }
            item.example?.let { append("Example: $it\n\n") }
            item.emoji?.let { append("Emoji: $it") }
        }
        trim()
        append("\n\n")
        append(context.getString(R.string.this_text_generated_using_owl_fa))
        append("\nThis text is generated using Owl app.\n")
        append(context.getString(R.string.github_source))
        append("\nThis text is extracted from Owlbot Dictionary.\n")
        append(context.getString(R.string.owl_bot_link))
    }

    fun handleSuggestions() {
        clearSuggestions()
        if (searchText.length >= Constants.DEFAULT_N_GRAM_SIZE) {
            val suggestions = autoCompleteHelper.suggestTermsForSearch(searchText)
            suggestions.joinToString().log()
            searchSuggestions.value = ImmutableHolder(
                suggestions.take(Constants.DEFAULT_N_GRAM_SIZE * 3)
            )
        }
    }

    fun clearSuggestions() {
        searchSuggestions.value = ImmutableHolder(listOf())
    }
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

@Composable
fun rememberHomeState(
    listState: LazyListState = rememberLazyListState(),
    isSearching: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    focusManager: FocusManager = LocalFocusManager.current,
    searchText: String = rememberSaveable { mutableStateOf("").value },
    rawWordSearchBody: MutableState<Word?> = rememberSaveable { mutableStateOf(null) },
    searchResult: MutableState<ImmutableHolder<List<Definition>>> = rememberSaveable(stateSaver = DefinitionListSaver) {
        mutableStateOf(ImmutableHolder(emptyList()))
    },
    context: Context = LocalContext.current,
    isSharing: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    ttsLang: MutableState<String> = rememberSaveable { mutableStateOf(Locale.US.toLanguageTag()) },
    searchSuggestions: MutableState<ImmutableHolder<List<String>>> = rememberSaveable(stateSaver = getImmutableHolderSaver()) {
        mutableStateOf(ImmutableHolder(emptyList()))
    }
) = remember(
    listState,
    isSearching,
    lifecycleOwner,
    focusManager,
    searchText,
    rawWordSearchBody,
    searchResult,
    context,
    isSharing,
    ttsLang,
    searchSuggestions
) {
    HomeState(
        listState,
        isSearching,
        lifecycleOwner,
        focusManager,
        searchText,
        rawWordSearchBody,
        searchResult,
        context,
        isSharing,
        ttsLang,
        searchSuggestions
    )
}
