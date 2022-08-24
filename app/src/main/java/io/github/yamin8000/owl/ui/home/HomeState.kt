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

package io.github.yamin8000.owl.ui.home

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import io.github.yamin8000.owl.model.Definition
import io.github.yamin8000.owl.model.RandomWord
import io.github.yamin8000.owl.model.Word
import io.github.yamin8000.owl.network.APIs
import io.github.yamin8000.owl.network.Web
import io.github.yamin8000.owl.network.Web.getAPI
import io.github.yamin8000.owl.util.favouritesDataStore
import io.github.yamin8000.owl.util.historyDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class HomeState(
    val listState: LazyListState,
    var isSearching: MutableState<Boolean>,
    val lifecycleOwner: LifecycleOwner,
    private val focusManager: FocusManager,
    var searchText: String,
    var rawWordSearchBody: MutableState<Word?>,
    var searchResult: MutableState<List<Definition>>,
    var errorMessage: MutableState<String>,
    val context: Context,
    val isSharing: MutableState<Boolean>
) {
    private val lifeCycleScopeContext = lifecycleOwner.lifecycleScope.coroutineContext

    val isShowingError: Boolean
        get() = errorMessage.value.isNotBlank()

    val floatingActionButtonVisibility: Boolean
        get() = !listState.isScrollInProgress

    val isFirstTimeOpening: Boolean
        get() = searchResult.value.isEmpty() && rawWordSearchBody.value == null && searchText.isEmpty()

    suspend fun searchForRandomWord() {
        reset()
        val randomWord = withContext(lifeCycleScopeContext) {
            try {
                getNewRandomWord()
            } catch (e: HttpException) {
                getNewRandomWord()
            } catch (e: Exception) {
                null
            }
        }
        searchText = randomWord?.word ?: ""
        if (searchText.isBlank()) searchForRandomWord()
        withContext(lifeCycleScopeContext) { searchForDefinitionHandler() }
    }

    private suspend fun searchForDefinitionRequest(
        searchTerm: String
    ): Word? {
        reset()
        searchText = searchTerm
        val body = withContext(lifeCycleScopeContext) {
            try {
                Web.retrofit.getAPI<APIs.OwlBotWordAPI>().searchWord(searchTerm.trim())
            } catch (e: HttpException) {
                errorMessage.value = getErrorMessage(e.code(), context)
                null
            } catch (e: Exception) {
                errorMessage.value = getErrorMessage(999, context)
                null
            }
        }
        isSearching.value = false
        return body
    }

    suspend fun searchForDefinitionHandler() {
        if (searchText.isNotBlank()) {
            searchForDefinition()
            addSearchTextToHistory()
        } else errorMessage.value = getErrorMessage(998, context)
    }

    private suspend fun addSearchTextToHistory() {
        context.historyDataStore.edit {
            it[stringPreferencesKey(searchText)] = searchText
        }
    }

    suspend fun searchForDefinition() {
        rawWordSearchBody.value = withContext(lifeCycleScopeContext) {
            searchForDefinitionRequest(searchText)
        }
        searchResult.value = rawWordSearchBody.value?.definitions ?: listOf()
        searchResult.value = searchResult.value.sortedByDescending { it.imageUrl }
    }

    private suspend fun getNewRandomWord(): RandomWord {
        return Web.ninjaApiRetrofit.getAPI<APIs.NinjaAPI>().getRandomWord()
    }

    private fun reset() {
        focusManager.clearFocus()
        isSearching.value = true
        errorMessage.value = ""
    }

    suspend fun addToFavourite(
        favouriteWord: String
    ) {
        val wordInDataStore = withContext(lifeCycleScopeContext) {
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
        searchResult.value.forEachIndexed { index, item ->
            if (searchResult.value.size > 1)
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
    searchResult: MutableState<List<Definition>> = rememberSaveable { mutableStateOf(emptyList()) },
    errorMessage: MutableState<String> = rememberSaveable { mutableStateOf("") },
    context: Context = LocalContext.current,
    isSharing: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }
) = remember(
    listState,
    isSearching,
    lifecycleOwner,
    focusManager,
    searchText,
    rawWordSearchBody,
    searchResult,
    errorMessage,
    context,
    isSharing
) {
    HomeState(
        listState,
        isSearching,
        lifecycleOwner,
        focusManager,
        searchText,
        rawWordSearchBody,
        searchResult,
        errorMessage,
        context,
        isSharing
    )
}
