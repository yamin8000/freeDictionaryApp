/*
 *     freeDictionaryApp/freeDictionaryApp.common.main
 *     RemoveAllContent.kt Copyrighted by Yamin Siahmargooei at 2025/2/7
 *     RemoveAllContent.kt Last modified at 2025/2/7
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

package io.github.yamin8000.owl.common.ui.components.crud

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.strings.R

@Composable
internal fun RemoveAllContent(
    onRemoveAllClick: () -> Unit
) {
    var isShowingDialog by remember { mutableStateOf(false) }

    if (isShowingDialog) {
        AlertDialog(
            onDismissRequest = { isShowingDialog = false },
            title = { AppText(stringResource(R.string.clear_all)) },
            text = { AppText(stringResource(R.string.remove_all_prompt)) },
            confirmButton = {
                Button(
                    onClick = onRemoveAllClick,
                    content = { AppText(stringResource(R.string.yes)) }
                )
            },
            dismissButton = {
                Button(
                    onClick = { isShowingDialog = false },
                    content = { AppText(stringResource(R.string.no)) }
                )
            }
        )
    }
    Button(
        onClick = { isShowingDialog = true },
        shape = DefaultCutShape,
        content = { AppText(text = stringResource(R.string.clear_all)) }
    )
}