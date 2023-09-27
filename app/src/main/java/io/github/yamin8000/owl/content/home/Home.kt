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

package io.github.yamin8000.owl.content.home

import android.content.pm.ActivityInfo
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.content.MainBottomBar
import io.github.yamin8000.owl.content.MainTopBar
import io.github.yamin8000.owl.ui.composable.*
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    searchTerm: String?,
    onHistoryClick: () -> Unit,
    onFavouritesClick: () -> Unit,
    onInfoClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    Surface(
        modifier = Modifier.fillMaxSize(),
        content = {
            val state = rememberHomeState()

            if (state.listState.isScrollInProgress && state.isVibrating.value)
                LocalHapticFeedback.current.performHapticFeedback(HapticFeedbackType.TextHandleMove)

            InternetAwareComposable { state.isOnline.value = it }

            val locale = if (state.ttsLang.value.isEmpty())
                Locale.US else Locale.forLanguageTag(state.ttsLang.value)

            if (searchTerm != null) {
                state.searchText = searchTerm
                LaunchedEffect(Unit) { state.addSearchTextToHistory() }
            }
            LaunchedEffect(state.isOnline.value) {
                if (state.isFirstTimeOpening)
                    state.searchText = "free"
                if (state.searchText.isNotBlank())
                    state.searchForDefinition()
            }

            if (state.searchResult.value.item.isNotEmpty() && state.isSharing.value)
                state.handleShareIntent()

            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
                        scrollBehavior = scrollBehavior,
                        onHistoryClick = onHistoryClick,
                        onFavouritesClick = onFavouritesClick,
                        onInfoClick = onInfoClick,
                        onSettingsClick = onSettingsClick,
                        onRandomWordClick = { state.scope.launch { state.searchForRandomWord() } }
                    )
                },
                bottomBar = {
                    MainBottomBar(
                        searchTerm = searchTerm,
                        suggestions = state.searchSuggestions.value,
                        isSearching = state.isSearching.value,
                        onSearchTermChanged = {
                            state.searchText = it
                            state.scope.launch { state.handleSuggestions() }
                            if (state.isWordSelectedFromKeyboardSuggestions) {
                                state.scope.launch { state.searchForDefinitionHandler() }
                                state.clearSuggestions()
                            }
                        },
                        onSuggestionClick = {
                            state.searchText = it
                            state.lifecycleOwner.lifecycleScope.launch { state.searchForDefinitionHandler() }
                        },
                        onSearch = {
                            state.searchText = it
                            state.lifecycleOwner.lifecycleScope.launch { state.searchForDefinitionHandler() }
                        },
                        onCancel = { state.cancel() }
                    )
                },
                content = { contentPadding ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(contentPadding)
                            .padding(top = 8.dp),
                        content = {
                            AnimatedVisibility(
                                visible = !state.isOnline.value,
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

                            if (state.searchResult.value.item.isNotEmpty()) {
                                val entry = state.searchResult.value.item.firstOrNull()
                                val word = entry?.word ?: ""
                                val phonetic =
                                    entry?.phonetics?.firstOrNull { it.text != null }?.text ?: ""
                                WordCard(
                                    localeTag = locale.toLanguageTag(),
                                    word = word,
                                    pronunciation = phonetic,
                                    onShareWord = { state.isSharing.value = true },
                                    onAddToFavourite = {
                                        state.scope.launch {
                                            state.addToFavourite(word)
                                            state.snackbarHostState.showSnackbar(
                                                addedToFavourites
                                            )
                                        }
                                    }
                                )

                                WordDefinitionsList(
                                    word = word,
                                    localeTag = locale.toLanguageTag(),
                                    listState = state.listState,
                                    meanings = state.searchResult.value.item.first().meanings,
                                    onWordChipClick = {
                                        state.searchText = it
                                        state.lifecycleOwner.lifecycleScope.launch { state.searchForDefinitionHandler() }
                                    }
                                )
                            } else EmptyList()
                        }
                    )
                }
            )
        }
    )
}