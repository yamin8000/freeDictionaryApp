/*
 *     freeDictionaryApp/freeDictionaryApp.feature_overlay.main
 *     OverlayWindow.kt Copyrighted by Yamin Siahmargooei at 2024/9/5
 *     OverlayWindow.kt Last modified at 2024/9/5
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_overlay.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_overlay.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_overlay.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_overlay.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.yamin8000.owl.common.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.common.ui.theme.defaultGradientBorder
import io.github.yamin8000.owl.search.domain.model.Meaning
import io.github.yamin8000.owl.search.ui.components.MeaningCard
import io.github.yamin8000.owl.search.ui.components.WordCard
import io.github.yamin8000.owl.strings.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverlayScreen(
    modifier: Modifier = Modifier,
    vm: OverlayWindowViewModel = hiltViewModel(),
    onDismissRequest: () -> Unit,
    navigateToApp: (String) -> Unit
) {
    val state = vm.state.collectAsStateWithLifecycle().value

    val configuration = LocalConfiguration.current
    val isPortrait = remember(configuration) {
        configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }
    val screenHeight = remember(configuration) {
        configuration.screenHeightDp.dp
    }
    val buttonsOffset = 64.dp
    val windowHeight = remember(isPortrait, screenHeight) {
        if (isPortrait) screenHeight / 2 else screenHeight - buttonsOffset
    }
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        ),
        content = {
            Box(
                modifier = Modifier.height(windowHeight + buttonsOffset),
                content = {
                    Surface(
                        modifier = Modifier
                            .height(windowHeight)
                            .padding(horizontal = 16.dp),
                        shape = DefaultCutShape,
                        border = defaultGradientBorder(),
                        content = {
                            SearchList(
                                isSearching = state.isSearching,
                                word = state.word,
                                phonetic = state.phonetic,
                                meanings = state.meanings
                            )
                        }
                    )
                    ButtonsRow(
                        modifier = Modifier
                            .padding(top = buttonsOffset)
                            .align(Alignment.BottomCenter),
                        onDismissRequest = onDismissRequest,
                        navigateToApp = { navigateToApp(state.word) }
                    )
                }
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    SearchList(
        isSearching = true,
        word = "duh",
        phonetic = "duh",
        meanings = emptyList()
    )
}

@Composable
private fun SearchList(
    modifier: Modifier = Modifier,
    isSearching: Boolean,
    word: String,
    phonetic: String,
    meanings: List<Meaning>
) {
    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            if (isSearching) {
                item {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            item {
                WordCard(
                    word = word,
                    pronunciation = phonetic
                )
            }
            items(meanings) { meaning ->
                MeaningCard(
                    word = word,
                    meaning = meaning
                )
            }
            /*item {
                ButtonsRow(
                    onDismissRequest = onDismissRequest,
                    navigateToApp = navigateToApp
                )
            }*/
        }
    )
}

@Composable
private fun ButtonsRow(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    navigateToApp: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        content = {
            Button(
                onClick = onDismissRequest,
                content = { Text(stringResource(R.string.close)) }
            )
            Button(
                onClick = navigateToApp,
                content = { Text(stringResource(R.string.more_in_app)) }
            )
        }
    )
}