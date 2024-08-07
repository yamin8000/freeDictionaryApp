/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     Home.kt Copyrighted by Yamin Siahmargooei at 2024/5/9
 *     Home.kt Last modified at 2024/5/6
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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.data.model.Entry
import io.github.yamin8000.owl.ui.composable.EmptyList
import io.github.yamin8000.owl.ui.composable.InternetAwareComposable
import io.github.yamin8000.owl.ui.composable.LockScreenOrientation
import io.github.yamin8000.owl.ui.composable.MySnackbar
import io.github.yamin8000.owl.ui.composable.PersianText
import io.github.yamin8000.owl.ui.theme.MyPreview
import io.github.yamin8000.owl.ui.theme.PreviewTheme
import io.github.yamin8000.owl.util.TermSuggestionsHelper
import io.github.yamin8000.owl.util.viewModelFactory
import kotlinx.collections.immutable.toPersistentList

@MyPreview
@Composable
private fun HomeScreenPreview() {
    PreviewTheme {
        HomeScreen(
            searchTerm = "",
            isStartingBlank = false,
            isVibrating = false,
            onTopBarClick = {},
            onAddToHistory = {},
            onAddToFavourite = {}
        )
    }
}

// TODO: Refactor to HomeScreen and HomeContent(stateless)
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    searchTerm: String?,
    isStartingBlank: Boolean,
    isVibrating: Boolean,
    onTopBarClick: (HomeTopBarItem) -> Unit,
    onAddToHistory: (String) -> Unit,
    onAddToFavourite: (String) -> Unit
) {
    val context = LocalContext.current

    val termSuggestionsHelper = remember { TermSuggestionsHelper(context) }

    val vm: HomeViewModel = viewModel(factory = viewModelFactory {
        initializer {
            HomeViewModel(
                sentSearchTerm = searchTerm,
                isStartingBlank = isStartingBlank,
                termSuggestionsHelper = termSuggestionsHelper
            )
        }
    })

    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        vm.searchState.collect { searchState ->
            when (searchState) {
                is SearchState.RequestFailed -> {
                    vm.showSnackbar(getErrorMessage(searchState.code, context))
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
        modifier = modifier.fillMaxSize(),
        content = {
            val listState = rememberScrollState()
            if (listState.isScrollInProgress && isVibrating)
                LocalHapticFeedback.current.performHapticFeedback(HapticFeedbackType.TextHandleMove)

            InternetAwareComposable(onlineChanged = { vm.updateIsOnline(it) })

            if (vm.isSharing.collectAsStateWithLifecycle().value) {
                val temp = vm.searchResult.collectAsStateWithLifecycle().value.firstOrNull()
                if (temp != null) {
                    HandleShareIntent(temp)
                    vm.stopWordSharing()
                }
            }

            Scaffold(
                snackbarHost = {
                    SnackbarHost(vm.snackbarHostState.collectAsStateWithLifecycle().value) { data ->
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
                                HomeTopBarItem.Random -> vm.searchForRandomWord()
                                else -> onTopBarClick(it)
                            }
                        }
                    }
                    MainTopBar(onItemClick = onClick)
                },
                bottomBar = {
                    val search = vm.searchTerm.collectAsStateWithLifecycle()
                    val onSearch: () -> Unit = remember(search.value) {
                        { vm.searchForDefinition(search.value) }
                    }
                    val onSuggestionsClick: (String) -> Unit = remember {
                        {
                            vm.updateSearchTerm(it)
                            vm.searchForDefinition(it)
                        }
                    }
                    val onSearchTermChange: (String) -> Unit =
                        remember(vm.isWordSelectedFromKeyboardSuggestions.value) {
                            {
                                vm.updateSearchTerm(it)
                                vm.handleSuggestions()
                                if (vm.isWordSelectedFromKeyboardSuggestions.value) {
                                    vm.clearSuggestions()
                                    vm.searchForDefinition(it)
                                }
                            }
                        }
                    val onCancel = remember { { vm.cancel() } }
                    MainBottomBar(
                        searchTerm = search.value,
                        suggestions = vm.searchSuggestions.collectAsStateWithLifecycle().value.toPersistentList(),
                        isSearching = vm.isSearching.collectAsStateWithLifecycle().value,
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
                                visible = !vm.isOnline.collectAsStateWithLifecycle().value,
                                enter = slideInVertically() + fadeIn(),
                                exit = slideOutVertically() + fadeOut(),
                                content = {
                                    PersianText(
                                        text = stringResource(R.string.general_net_error),
                                        modifier = Modifier.padding(8.dp),
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            )

                            val addedToFavourites = stringResource(R.string.added_to_favourites)

                            val searchResult by vm.searchResult.collectAsStateWithLifecycle()
                            if (searchResult.isNotEmpty()) {
                                val entry = searchResult.firstOrNull()
                                val word = entry?.word ?: ""
                                val phonetic = entry?.phonetics
                                    ?.firstOrNull { it.text != null }
                                    ?.text ?: ""

                                val onWordChipClick: (String) -> Unit = remember {
                                    {
                                        vm.updateSearchTerm(it)
                                        vm.searchForDefinition(it)
                                    }
                                }
                                WordDefinitionsList(
                                    word = word,
                                    listState = listState,
                                    meanings = searchResult.first().meanings.toPersistentList(),
                                    onWordChipClick = onWordChipClick,
                                    wordCard = {
                                        WordCard(
                                            word = word,
                                            pronunciation = phonetic,
                                            onShareWord = remember { { vm.startWordSharing() } },
                                            onAddToFavourite = remember(searchResult) {
                                                {
                                                    onAddToFavourite(word)
                                                    vm.showSnackbar(addedToFavourites)
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
    entry.meanings.forEachIndexed { index, (partOfSpeech, definitions, _, _) ->
        appendLine("${index + 1})")
        appendLine("Type: $partOfSpeech")
        definitions.take(5).forEach { (definition, example, synonyms, antonyms) ->
            appendLine("Definition: $definition")
            if (example != null)
                appendLine("Example: $example")
            if (synonyms.isNotEmpty())
                appendLine("Synonyms: ${synonyms.take(5).joinToString()}")
            if (antonyms.isNotEmpty())
                appendLine("Antonyms: ${antonyms.take(5).joinToString()}")
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
