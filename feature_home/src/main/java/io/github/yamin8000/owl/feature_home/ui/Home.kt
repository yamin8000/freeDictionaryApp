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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.components.EmptyList
import io.github.yamin8000.owl.common.ui.components.MySnackbar
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.common.util.LocalTTS
import io.github.yamin8000.owl.feature_home.ui.components.MainTopBar
import io.github.yamin8000.owl.feature_home.ui.components.SearchList
import io.github.yamin8000.owl.feature_home.ui.components.bottom_app_bar.MainBottomBar
import io.github.yamin8000.owl.feature_home.ui.components.bottom_app_bar.SuggestionsChips
import io.github.yamin8000.owl.feature_home.ui.util.ShareUtils.handleShareIntent
import io.github.yamin8000.owl.feature_home.ui.util.Utils.ObserverEvent
import io.github.yamin8000.owl.feature_home.ui.util.Utils.getErrorText
import io.github.yamin8000.owl.search.domain.model.Entry
import io.github.yamin8000.owl.strings.R
import kotlinx.collections.immutable.persistentListOf
import kotlin.random.Random

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewTheme {
        HomeContent(
            state = HomeState(
                searchResult = Entry.mock(),
                isOnline = Random.nextBoolean(),
                isSearching = Random.nextBoolean(),
                searchSuggestions = persistentListOf("apple", "banana", "orange"),
                word = "example",
                phonetic = "/ɪɡˈzæmpəl/"
            ),
            term = "example",
            isWordSelectedFromKeyboardSuggestions = Random.nextBoolean(),
            onAction = {},
            onNavigateToAbout = {},
            onNavigateToSettings = {},
            onNavigateToFavourites = {},
            onNavigateToHistory = {}
        )
    }
}

@Composable
fun HomeScreen(
    onNavigateToAbout: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToFavourites: () -> Unit,
    onNavigateToHistory: () -> Unit,
    modifier: Modifier = Modifier,
    vm: HomeViewModel = hiltViewModel()
) {
    val state = vm.state.collectAsStateWithLifecycle().value

    val owner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            vm.onAction(HomeAction.UpdateTTS)
        }
    }

    val context = LocalContext.current
    ObserverEvent(vm.shareChannelFlow) { data ->
        if (data != null) {
            handleShareIntent(context, data)
        }
    }

    ObserverEvent(vm.errorChannelFlow) { error ->
        state.snackbarHostState.showSnackbar(getErrorText(context, error))
    }

    CompositionLocalProvider(LocalTTS provides vm.tts) {
        HomeContent(
            state = state,
            term = vm.searchTerm.collectAsState().value,
            isWordSelectedFromKeyboardSuggestions = vm.isWordSelectedFromKeyboardSuggestions.value,
            onAction = { action -> vm.onAction(action) },
            onNavigateToAbout = onNavigateToAbout,
            onNavigateToSettings = onNavigateToSettings,
            onNavigateToFavourites = onNavigateToFavourites,
            onNavigateToHistory = onNavigateToHistory,
            modifier = modifier
        )
    }
}

@Composable
internal fun HomeContent(
    state: HomeState,
    term: String,
    isWordSelectedFromKeyboardSuggestions: Boolean,
    onAction: (HomeAction) -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToFavourites: () -> Unit,
    onNavigateToHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberScrollState()
    if (listState.isScrollInProgress && state.isVibrating) {
        LocalHapticFeedback.current.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(state.snackbarHostState) { data ->
                MySnackbar {
                    AppText(
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
                onRandomClick = { onAction(HomeAction.RandomWord) }
            )
        },
        bottomBar = {
            val focusManager = LocalFocusManager.current
            val keyboardManager = LocalSoftwareKeyboardController.current
            MainBottomBar(
                searchTerm = term,
                suggestionsChips = {
                    SuggestionsChips(
                        searchTerm = term,
                        suggestions = state.searchSuggestions,
                        onSuggestionClick = { onAction(HomeAction.NewSearch(it)) },
                    )
                },
                isSearching = state.isSearching,
                onSearch = {
                    onAction(HomeAction.NewSearch())
                    keyboardManager?.hide()
                    focusManager.clearFocus()
                },
                onCancel = {
                    onAction(HomeAction.CancelSearch)
                    keyboardManager?.hide()
                    focusManager.clearFocus()
                },
                onSearchTermChange = {
                    onAction(HomeAction.OnTermChanged(it))
                    if (isWordSelectedFromKeyboardSuggestions) {
                        onAction(HomeAction.NewSearch(it))
                    }
                }
            )
        },
        content = { contentPadding ->
            if (state.searchResult != null) {
                SearchList(
                    modifier = Modifier.padding(contentPadding),
                    meanings = state.searchResult.meanings,
                    onAddToFavourite = { onAction(HomeAction.OnAddToFavourite(state.word)) },
                    onWordChipClick = { onAction(HomeAction.NewSearch(it)) },
                    onShareWord = { onAction(HomeAction.OnShareData) },
                    isOnline = state.isOnline,
                    word = state.word,
                    phonetic = state.phonetic
                )
            } else {
                Column(
                    modifier = modifier.padding(Sizes.Large),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        Sizes.Large,
                        Alignment.CenterVertically
                    ),
                    content = {
                        AppText(stringResource(R.string.search_hint))
                        EmptyList()
                    }
                )
            }
        }
    )
}