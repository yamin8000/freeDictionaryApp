/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     HomeTopBar.kt Copyrighted by Yamin Siahmargooei at 2024/8/18
 *     HomeTopBar.kt Last modified at 2024/8/17
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

package io.github.yamin8000.owl.feature_home.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Casino
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material.icons.twotone.History
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.yamin8000.owl.common.ui.components.ClickableIcon
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.strings.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainTopBar(
    modifier: Modifier = Modifier,
    onNavigateToAbout: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToFavourites: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onRandomClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        shadowElevation = Sizes.Medium,
        content = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                actions = {
                    ClickableIcon(
                        imageVector = Icons.TwoTone.History,
                        contentDescription = stringResource(R.string.search_history),
                        onClick = onNavigateToHistory
                    )
                    ClickableIcon(
                        imageVector = Icons.TwoTone.Favorite,
                        contentDescription = stringResource(R.string.favourites),
                        onClick = onNavigateToFavourites
                    )
                    ClickableIcon(
                        imageVector = Icons.TwoTone.Casino,
                        contentDescription = stringResource(R.string.random_word),
                        onClick = onRandomClick
                    )
                    ClickableIcon(
                        imageVector = Icons.TwoTone.Settings,
                        contentDescription = stringResource(R.string.settings),
                        onClick = onNavigateToSettings
                    )
                    ClickableIcon(
                        imageVector = Icons.TwoTone.Info,
                        contentDescription = stringResource(R.string.about_app),
                        onClick = onNavigateToAbout
                    )
                }
            )
        }
    )
}