/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     MainTopBar.kt Copyrighted by Yamin Siahmargooei at 2023/8/26
 *     MainTopBar.kt Last modified at 2023/8/26
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

package io.github.yamin8000.owl.content

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.composable.AnimatedAppIcon
import io.github.yamin8000.owl.ui.composable.ClickableIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onHistoryClick: () -> Unit,
    onFavouritesClick: () -> Unit,
    onRandomWordClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onInfoClick: () -> Unit,
) {
    Surface(
        shadowElevation = 8.dp,
        content = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = { AnimatedAppIcon() },
                actions = {
                    ClickableIcon(
                        imageVector = Icons.TwoTone.History,
                        contentDescription = stringResource(R.string.search_history),
                        onClick = onHistoryClick,
                    )

                    ClickableIcon(
                        imageVector = Icons.TwoTone.Favorite,
                        contentDescription = stringResource(R.string.favourites),
                        onClick = onFavouritesClick,
                    )

                    ClickableIcon(
                        imageVector = Icons.TwoTone.Casino,
                        contentDescription = stringResource(R.string.random_word),
                        onClick = onRandomWordClick,
                    )

                    ClickableIcon(
                        imageVector = Icons.TwoTone.Settings,
                        contentDescription = stringResource(R.string.settings),
                        onClick = onSettingsClick
                    )

                    ClickableIcon(
                        imageVector = Icons.TwoTone.Info,
                        contentDescription = stringResource(R.string.about_app),
                        onClick = onInfoClick
                    )
                }
            )
        }
    )
}