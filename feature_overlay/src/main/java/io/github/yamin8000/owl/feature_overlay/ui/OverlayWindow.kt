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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.yamin8000.owl.common.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.common.ui.theme.defaultGradientBorder
import io.github.yamin8000.owl.feature_overlay.ui.components.ButtonsRow
import io.github.yamin8000.owl.feature_overlay.ui.components.SearchList

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
    val buttonsOffset = Sizes.xxLarge
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
                            .padding(horizontal = Sizes.Large),
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