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
import android.content.pm.ActivityInfo
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import io.github.yamin8000.owl.common.ui.components.EmptyList
import io.github.yamin8000.owl.common.ui.components.LockScreenOrientation
import io.github.yamin8000.owl.common.ui.components.MySnackbar
import io.github.yamin8000.owl.common.ui.components.PersianText
import io.github.yamin8000.owl.common.ui.util.LocalTTS
import io.github.yamin8000.owl.feature_home.domain.model.Meaning
import io.github.yamin8000.owl.feature_home.ui.components.MainBottomBar
import io.github.yamin8000.owl.feature_home.ui.components.MainTopBar
import io.github.yamin8000.owl.feature_home.ui.components.MeaningCard
import io.github.yamin8000.owl.feature_home.ui.components.SuggestionsChips
import io.github.yamin8000.owl.feature_home.ui.components.WordCard
import io.github.yamin8000.owl.feature_home.ui.util.ShareUtils.handleShareIntent
import io.github.yamin8000.owl.strings.R
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    vm: HomeViewModel = hiltViewModel(),
    onNavigateToAbout: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToFavourites: () -> Unit,
    onNavigateToHistory: () -> Unit,
) {
    val state = vm.state.collectAsStateWithLifecycle().value

    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current

    val owner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            vm.onEvent(HomeEvent.UpdateTTS)
        }
    }

    CompositionLocalProvider(LocalTTS provides vm.tts) {
        Surface(
            modifier = modifier.fillMaxSize(),
            content = {
                val listState = rememberScrollState()
                if (listState.isScrollInProgress && state.isVibrating) {
                    LocalHapticFeedback.current.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }

                ObserverEvent(vm.shareChannelFlow) { data ->
                    if (data != null) {
                        handleShareIntent(context, data)
                    }
                }

                ObserverEvent(vm.errorChannelFlow) { event ->
                    state.snackbarHostState.showSnackbar(
                        getErrorText(context = context, error = event)
                    )
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
                            onNavigateToAbout = onNavigateToAbout,
                            onNavigateToSettings = onNavigateToSettings,
                            onNavigateToFavourites = onNavigateToFavourites,
                            onNavigateToHistory = onNavigateToHistory,
                            onRandomClick = { vm.onEvent(HomeEvent.RandomWord) }
                        )
                    },
                    bottomBar = {
                        val term = vm.searchTerm.collectAsState().value
                        MainBottomBar(
                            searchTerm = term,
                            suggestionsChips = {
                                SuggestionsChips(
                                    searchTerm = term,
                                    suggestions = state.searchSuggestions,
                                    onSuggestionClick = { vm.onEvent(HomeEvent.NewSearch(it)) },
                                )
                            },
                            isSearching = state.isSearching,
                            onSearch = {
                                vm.onEvent(HomeEvent.NewSearch())
                                keyboardManager?.hide()
                                focusManager.clearFocus()
                            },
                            onCancel = {
                                vm.onEvent(HomeEvent.CancelSearch)
                                keyboardManager?.hide()
                                focusManager.clearFocus()
                            },
                            onSearchTermChange = {
                                vm.onEvent(HomeEvent.OnTermChanged(it))
                                if (vm.isWordSelectedFromKeyboardSuggestions.value) {
                                    vm.onEvent(HomeEvent.NewSearch(it))
                                }
                            }
                        )
                    },
                    content = { contentPadding ->
                        if (state.searchResult != null) {
                            SearchList(
                                modifier = Modifier.padding(contentPadding),
                                meanings = state.searchResult.meanings.toPersistentList(),
                                onAddToFavourite = { vm.onEvent(HomeEvent.OnAddToFavourite(state.word)) },
                                onWordChipClick = { vm.onEvent(HomeEvent.NewSearch(it)) },
                                onShareWord = { vm.onEvent(HomeEvent.OnShareData) },
                                isOnline = state.isOnline,
                                word = state.word,
                                phonetic = state.phonetic
                            )
                        } else {
                            Column(
                                modifier = modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                content = {
                                    PersianText(stringResource(R.string.search_hint))
                                    EmptyList()
                                }
                            )
                        }
                    }
                )
            }
        )
    }
}

@Composable
private fun SearchList(
    modifier: Modifier = Modifier,
    isOnline: Boolean,
    word: String,
    phonetic: String,
    onAddToFavourite: () -> Unit,
    onShareWord: () -> Unit,
    onWordChipClick: (String) -> Unit,
    meanings: List<Meaning>
) {
    val context = LocalContext.current
    val internetError = remember { context.getString(R.string.general_net_error) }
    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            item(key = isOnline) {
                AnimatedVisibility(
                    visible = !isOnline,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut(),
                    content = {
                        PersianText(
                            text = internetError,
                            modifier = Modifier.padding(8.dp),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                )
            }
            item(key = word + phonetic) {
                WordCard(
                    word = word,
                    pronunciation = phonetic,
                    onShareWord = onShareWord,
                    onAddToFavourite = onAddToFavourite
                )
            }

            itemsIndexed(
                items = meanings,
                key = { index, item -> item.id ?: index },
                itemContent = { _, meaning ->
                    MeaningCard(
                        modifier = Modifier.animateItem(),
                        word = word,
                        meaning = meaning,
                        onWordChipClick = onWordChipClick
                    )
                }
            )
        }
    )
}

@Composable
private fun <T> ObserverEvent(
    flow: Flow<T>,
    onEvent: suspend (T) -> Unit
) {
    val lifeCycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(flow, lifeCycleOwner.lifecycle) {
        lifeCycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}

private fun getErrorText(
    context: Context,
    error: HomeSnackbarType?
) = when (error) {
    HomeSnackbarType.SearchFailed -> context.getString(R.string.general_net_error)
    HomeSnackbarType.TermIsEmpty -> context.getString(R.string.no_search_term_entered)
    HomeSnackbarType.NoInternet -> context.getString(R.string.general_net_error)
    HomeSnackbarType.ApiAuthorizationError -> context.getString(R.string.api_authorization_error)
    HomeSnackbarType.ApiThrottled -> context.getString(R.string.api_throttled)
    HomeSnackbarType.Cancelled -> context.getString(R.string.cancelled)
    HomeSnackbarType.NotFound -> context.getString(R.string.definition_not_found)
    HomeSnackbarType.Unknown -> context.getString(R.string.untracked_error)
    HomeSnackbarType.AddedToFavourite -> context.getString(R.string.added_to_favourites)
    null -> ""
}