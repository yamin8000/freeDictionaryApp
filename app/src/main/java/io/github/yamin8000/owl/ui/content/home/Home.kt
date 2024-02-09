/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     Home.kt Copyrighted by Yamin Siahmargooei at 2023/8/26
 *     Home.kt Last modified at 2023/8/26
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

package io.github.yamin8000.owl.ui.content.home

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.data.model.Entry
import io.github.yamin8000.owl.ui.composable.EmptyList
import io.github.yamin8000.owl.ui.composable.InternetAwareComposable
import io.github.yamin8000.owl.ui.composable.LockScreenOrientation
import io.github.yamin8000.owl.ui.composable.MySnackbar
import io.github.yamin8000.owl.ui.composable.PersianText
import io.github.yamin8000.owl.util.AutoCompleteHelper
import io.github.yamin8000.owl.util.viewModelFactory
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

@Composable
internal fun HomeContent(
    searchTerm: String?,
    isStartingBlank: Boolean,
    isVibrating: Boolean,
    ttsLang: String,
    onTopBarClick: (HomeTopBarItem) -> Unit,
    onAddToHistory: (String) -> Unit,
    onAddToFavourite: (String) -> Unit
) {
    val context = LocalContext.current

    val autoCompleteHelper = remember { AutoCompleteHelper(context) }

    val vm: HomeViewModel = viewModel(factory = viewModelFactory {
        initializer {
            HomeViewModel(
                sentSearchTerm = searchTerm,
                isStartingBlank = isStartingBlank,
                autoCompleteHelper = autoCompleteHelper
            )
        }
    })

    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        vm.searchState.collect { searchState ->
            when (searchState) {
                is SearchState.RequestFailed -> {
                    snackbarHostState.showSnackbar(getErrorMessage(searchState.code, context))
                }

                is SearchState.RequestFinished -> {
                    onAddToHistory(searchState.term)
                    keyboardManager?.hide()
                    focusManager.clearFocus()
                }

                SearchState.RequestSucceed -> {
                    keyboardManager?.hide()
                    focusManager.clearFocus()
                }

                SearchState.Cached -> {
                    keyboardManager?.hide()
                    focusManager.clearFocus()
                }

                SearchState.Unknown -> {}
            }
            vm.resetSearchState()
        }
    }

    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    Surface(
        modifier = Modifier.fillMaxSize(),
        content = {
            val listState = rememberScrollState()
            if (listState.isScrollInProgress && isVibrating)
                LocalHapticFeedback.current.performHapticFeedback(HapticFeedbackType.TextHandleMove)

            InternetAwareComposable(
                onlineChanged = {
                    vm.updateIsOnline(it)
                }
            )

            val locale = remember(ttsLang) { vm.getLocale(ttsLang) }

            if (vm.isSharing.collectAsState().value) {
                val temp = vm.searchResult.collectAsState().value.firstOrNull()
                if (temp != null) {
                    HandleShareIntent(temp)
                    vm.stopWordSharing()
                }
            }

            Scaffold(
                snackbarHost = {
                    SnackbarHost(snackbarHostState) { data ->
                        MySnackbar {
                            PersianText(
                                text = data.visuals.message,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                },
                topBar = {
                    val onClick: (HomeTopBarItem) -> Unit = remember {
                        {
                            when (it) {
                                HomeTopBarItem.Random -> vm.ioScope.launch { vm.searchForRandomWord() }
                                else -> onTopBarClick(it)
                            }
                        }
                    }
                    MainTopBar(onItemClick = onClick)
                },
                bottomBar = {
                    val search = vm.searchTerm.collectAsState()
                    val onSearch = remember(search.value) {
                        { vm.searchForDefinition(search.value) }
                    }
                    val onSuggestionsClick: (String) -> Unit = remember {
                        {
                            vm.updateSearchTerm(it)
                            vm.ioScope.launch { vm.searchForDefinition(it) }
                        }
                    }
                    val onSearchTermChange: (String) -> Unit =
                        remember(vm.isWordSelectedFromKeyboardSuggestions.value) {
                            {
                                vm.updateSearchTerm(it)
                                vm.ioScope.launch { vm.handleSuggestions() }
                                if (vm.isWordSelectedFromKeyboardSuggestions.value) {
                                    vm.clearSuggestions()
                                    vm.ioScope.launch { vm.searchForDefinition(it) }
                                }
                            }
                        }
                    val onCancel = remember { { vm.cancel() } }
                    MainBottomBar(
                        searchTerm = search.value,
                        suggestions = vm.searchSuggestions.collectAsState().value.toPersistentList(),
                        isSearching = vm.isSearching.collectAsState().value,
                        onSearch = onSearch,
                        onCancel = onCancel,
                        onSuggestionClick = onSuggestionsClick,
                        onSearchTermChange = onSearchTermChange
                    )
                },
                content = { contentPadding ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(contentPadding),
                        content = {
                            AnimatedVisibility(
                                visible = !vm.isOnline.collectAsState().value,
                                enter = slideInVertically() + fadeIn(),
                                exit = slideOutVertically() + fadeOut(),
                                content = {
                                    PersianText(
                                        text = stringResource(R.string.general_net_error),
                                        modifier = Modifier.padding(16.dp),
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            )

                            val addedToFavourites = stringResource(R.string.added_to_favourites)

                            val searchResult = vm.searchResult.collectAsState()
                            if (searchResult.value.isNotEmpty()) {
                                val entry = searchResult.value.firstOrNull()
                                val word = entry?.word ?: ""
                                val phonetic = entry?.phonetics
                                    ?.firstOrNull { it.text != null }
                                    ?.text ?: ""

                                WordDefinitionsList(
                                    word = word,
                                    localeTag = locale.toLanguageTag(),
                                    listState = listState,
                                    meanings = searchResult.value.first().meanings.toPersistentList(),
                                    onWordChipClick = {
                                        vm.updateSearchTerm(it)
                                        vm.ioScope.launch { vm.searchForDefinition(it) }
                                    },
                                    wordCard = {
                                        WordCard(
                                            localeTag = locale.toLanguageTag(),
                                            word = word,
                                            pronunciation = phonetic,
                                            onShareWord = vm::startWordSharing,
                                            onAddToFavourite = {
                                                vm.ioScope.launch {
                                                    onAddToFavourite(word)
                                                    snackbarHostState.showSnackbar(addedToFavourites)
                                                }
                                            }
                                        )
                                    }
                                )
                            } else {
                                PersianText(stringResource(R.string.search_hint))
                                EmptyList()
                            }
                        }
                    )
                }
            )
        }
    )
}

@Composable
private fun HandleShareIntent(
    entry: Entry
) {
    val context = LocalContext.current
    val text = createShareText(context, entry)

    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

private fun createShareText(
    context: Context,
    entry: Entry
) = buildString {
    append("Word: ")
    append(entry.word)
    appendLine()
    append("Pronunciation(IPA): ")
    append(entry.phonetics.firstOrNull { it.text != null }?.text ?: "-")
    appendLine()
    appendLine()
    entry.meanings.forEachIndexed { index, meaning ->
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
    997 -> context.getString(R.string.cancelled)
    998 -> context.getString(R.string.no_search_term_entered)
    999 -> context.getString(R.string.untracked_error)
    else -> context.getString(R.string.general_net_error)
}
