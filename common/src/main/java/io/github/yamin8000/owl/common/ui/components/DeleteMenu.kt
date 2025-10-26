/*
 *     freeDictionaryApp/freeDictionaryApp.common.main
 *     DeleteMenu.kt Copyrighted by Yamin Siahmargooei at 2025/2/7
 *     DeleteMenu.kt Last modified at 2025/2/7
 *     This file is part of freeDictionaryApp/freeDictionaryApp.common.main.
 *     Copyright (C) 2025  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.common.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.common.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.common.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.yamin8000.owl.common.ui.theme.MyPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.strings.R

@MyPreview
@Composable
private fun Preview() {
    PreviewTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(Sizes.xxLarge),
            content = {
                DeleteMenu(
                    item = "Item",
                    expanded = true,
                    onDelete = {},
                    onDismiss = {},
                    onWordClick = {}
                )
            }
        )
    }
}

@Composable
internal fun DeleteMenu(
    item: String,
    expanded: Boolean,
    onWordClick: () -> Unit,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val delete = stringResource(R.string.delete)
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismiss,
        content = {
            DropdownMenuItem(
                onClick = onWordClick,
                text = { AppText(item) },
            )
            DropdownMenuItem(
                onClick = onDelete,
                text = { AppText(text = delete) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.TwoTone.Delete,
                        contentDescription = delete
                    )
                }
            )
        }
    )
}