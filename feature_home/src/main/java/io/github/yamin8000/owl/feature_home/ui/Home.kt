/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     Home.kt Copyrighted by Yamin Siahmargooei at 2024/8/17
 *     Home.kt Last modified at 2024/7/20
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

import android.content.Context
import android.content.Intent
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import io.github.yamin8000.owl.common.ui.components.EmptyList
import io.github.yamin8000.owl.common.ui.components.MySnackbar
import io.github.yamin8000.owl.common.ui.components.PersianText
import io.github.yamin8000.owl.common.ui.navigation.Nav
import io.github.yamin8000.owl.common.ui.theme.MyPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.feature_home.domain.model.Entry
import io.github.yamin8000.owl.feature_home.ui.components.MainBottomBar
import io.github.yamin8000.owl.feature_home.ui.components.MainTopBar
import io.github.yamin8000.owl.feature_home.ui.components.WordCard
import io.github.yamin8000.owl.feature_home.ui.components.WordDefinitionsList
import io.github.yamin8000.owl.strings.R
import kotlinx.collections.immutable.toPersistentList

@MyPreview
@Composable
private fun HomeScreenPreview() {
    PreviewTheme {
        /*HomeScreen(
            isVibrating = false,
            onTopBarClick = {},
            onAddToHistory = {},
            onAddToFavourite = {}
        )*/
    }
}

// TODO: Refactor to HomeScreen and HomeContent(stateless)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    vm: HomeViewModel = hiltViewModel(),
    navController: NavController,
    onAddToHistory: (String) -> Unit,
    onAddToFavourite: (String) -> Unit
) {
    val state = vm.state.collectAsStateWithLifecycle().value

    //val termSuggestionsHelper = remember { TermSuggestionsHelper(context) }

    //LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current

    LaunchedEffect(state.snackBarEvent) {
        if (state.snackBarEvent != null) {
            when (state.snackBarEvent) {
                HomeSnackbarEvent.SearchFailed -> {
                    state.snackbarHostState.showSnackbar(context.getString(R.string.general_net_error))
                }

                HomeSnackbarEvent.TermIsEmpty -> {
                    state.snackbarHostState.showSnackbar(context.getString(R.string.no_search_term_entered))
                }
            }
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        content = {
            val listState = rememberScrollState()
            /*if (listState.isScrollInProgress && isVibrating)
                LocalHapticFeedback.current.performHapticFeedback(HapticFeedbackType.TextHandleMove)*/

            if (state.isSharing) {
                val temp = state.searchResult.firstOrNull()
                if (temp != null) {
                    HandleShareIntent(temp)
                }
            }

            Scaffold(
                snackbarHost = {
                    SnackbarHost(state.snackbarHostState) { data ->
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
                    MainTopBar(
                        onNavigateToAbout = { navController.navigate(Nav.Route.About.toString()) },
                        onNavigateToSettings = { navController.navigate(Nav.Route.Settings.toString()) },
                        onNavigateToFavourites = { navController.navigate(Nav.Route.Favourites.toString()) },
                        onNavigateToHistory = { navController.navigate(Nav.Route.History.toString()) },
                        onRandomClick = { vm.onEvent(HomeEvent.RandomWord) }
                    )
                },
                bottomBar = {
                    val onSuggestionsClick: (String) -> Unit = remember {
                        {
                            //vm.updateSearchTerm(it)
                            //vm.searchForDefinition(it)
                        }
                    }
                    /*val onSearchTermChange: (String) -> Unit =
                        remember(vm.isWordSelectedFromKeyboardSuggestions.value) {
                            {
                                //vm.updateSearchTerm(it)
                                //vm.handleSuggestions()
                                if (vm.isWordSelectedFromKeyboardSuggestions.value) {
                                    vm.clearSuggestions()
                                    //vm.searchForDefinition(it)
                                }
                            }
                        }*/
                    val onCancel = remember {
                        {
                            //vm.cancel()
                        }
                    }
                    MainBottomBar(
                        searchTerm = vm.searchTerm.collectAsState().value,
                        suggestions = vm.searchSuggestions.collectAsStateWithLifecycle().value.toPersistentList(),
                        isSearching = state.isSearching,
                        onSearch = {
                            vm.onEvent(HomeEvent.NewSearch)
                            keyboardManager?.hide()
                            focusManager.clearFocus()
                        },
                        onCancel = {
                            vm.onEvent(HomeEvent.CancelSearch)
                            keyboardManager?.hide()
                            focusManager.clearFocus()
                        },
                        onSuggestionClick = onSuggestionsClick,
                        onSearchTermChange = {
                            vm.onEvent(HomeEvent.OnTermChanged(it))
                            //vm.handleSuggestions()
                            if (vm.isWordSelectedFromKeyboardSuggestions.value) {
                                //vm.clearSuggestions()
                                vm.onEvent(HomeEvent.NewSearch)
                            }
                        }
                    )
                },
                content = { contentPadding ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(contentPadding),
                        content = {
                            AnimatedVisibility(
                                visible = !state.isOnline,
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

                            //val addedToFavourites = stringResource(R.string.added_to_favourites)

                            //val searchResult by vm.searchResult.collectAsStateWithLifecycle()
                            if (state.searchResult.isNotEmpty()) {
                                val entry = state.searchResult.firstOrNull()
                                val word = entry?.word ?: ""
                                val phonetic = entry?.phonetics
                                    ?.firstOrNull { it.text != null }
                                    ?.text ?: ""

                                val onWordChipClick: (String) -> Unit = remember {
                                    {
                                        //vm.updateSearchTerm(it)
                                        //vm.searchForDefinition(it)
                                    }
                                }
                                WordDefinitionsList(
                                    word = word,
                                    listState = listState,
                                    meanings = state.searchResult.first().meanings.toPersistentList(),
                                    onWordChipClick = onWordChipClick,
                                    wordCard = {
                                        WordCard(
                                            word = word,
                                            pronunciation = phonetic,
                                            onShareWord = { vm.onEvent(HomeEvent.OnShareData) },
                                            onAddToFavourite = remember(state.searchResult) {
                                                {
                                                    onAddToFavourite(word)
                                                    //vm.showSnackbar(addedToFavourites)
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

/*private fun getErrorMessage(
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
}*/
