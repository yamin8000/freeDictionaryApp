/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     BottomAppBarDuringSearch.kt Copyrighted by Yamin Siahmargooei at 2024/12/5
 *     BottomAppBarDuringSearch.kt Last modified at 2024/12/5
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
import androidx.compose.material.icons.twotone.Stop
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.yamin8000.owl.strings.R

@Composable
internal fun BottomAppBarDuringSearch(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit
) {
    BottomAppBar(
        modifier = modifier,
        actions = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCancel,
                content = {
                    Icon(
                        imageVector = Icons.TwoTone.Stop,
                        contentDescription = stringResource(R.string.cancel)
                    )
                }
            )
        }
    )
}