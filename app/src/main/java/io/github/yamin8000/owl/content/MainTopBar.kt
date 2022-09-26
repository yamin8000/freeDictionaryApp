/*
 *     Owl: an android app for Owlbot Dictionary API
 *     MainTopBar.kt Created by Yamin Siahmargooei at 2022/9/19
 *     This file is part of Owl.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Owl is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Owl is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Owl.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.content

import android.content.res.Configuration
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.composable.ClickableIcon
import io.github.yamin8000.owl.ui.theme.PreviewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    onHistoryClick: () -> Unit,
    onFavouritesClick: () -> Unit,
    onRandomWordClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onInfoClick: () -> Unit,
) {
    TopAppBar(
        title = {},
        actions = {
            ClickableIcon(
                iconPainter = painterResource(R.drawable.ic_history),
                contentDescription = stringResource(R.string.search_history),
                onClick = onHistoryClick,
            )

            ClickableIcon(
                iconPainter = painterResource(R.drawable.ic_favorites),
                contentDescription = stringResource(R.string.favourites),
                onClick = onFavouritesClick,
            )

            ClickableIcon(
                iconPainter = painterResource(R.drawable.ic_casino),
                contentDescription = stringResource(R.string.random_word),
                onClick = onRandomWordClick,
            )

            ClickableIcon(
                iconPainter = painterResource(R.drawable.ic_settings_applications),
                contentDescription = stringResource(R.string.settings),
                onClick = onSettingsClick
            )

            ClickableIcon(
                iconPainter = painterResource(R.drawable.ic_contact_support),
                contentDescription = stringResource(R.string.about_app),
                onClick = onInfoClick
            )
        }, navigationIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(R.string.app_name),
                tint = MaterialTheme.colorScheme.onSurface
            )
        })
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun Preview() {
    PreviewTheme { MainTopBar({}, {}, {}, {}, {}) }
}