/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     CrudContent.kt Copyrighted by Yamin Siahmargooei at 2024/5/9
 *     CrudContent.kt Last modified at 2024/5/6
 *     This file is part of freeDictionaryApp/freeDictionaryApp.app.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
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

package io.github.yamin8000.owl.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.theme.DefaultCutShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun <T> CrudContent(
    title: String,
    items: List<T>,
    onBackClick: () -> Unit,
    onRemoveAll: () -> Unit,
    onRemoveSingle: (T) -> Unit,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    emptyContent: (@Composable () -> Unit) = { EmptyList() }
) {
    ScaffoldWithTitle(
        modifier = modifier,
        title = title,
        onBackClick = onBackClick,
        content = {
            if (items.isNotEmpty()) {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier.padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalItemSpacing = 8.dp,
                    columns = StaggeredGridCells.Fixed(2),
                    content = {
                        item(
                            span = StaggeredGridItemSpan.FullLine,
                            content = { RemoveAllContent(onRemoveAllClick = remember { onRemoveAll }) }
                        )
                        items(
                            span = { StaggeredGridItemSpan.SingleLane },
                            count = items.size,
                            itemContent = {
                                val onClick = remember { { onItemClick(items[it]) } }
                                val onLongClick = remember { { onRemoveSingle(items[it]) } }
                                CrudItem(
                                    item = items[it].toString(),
                                    onClick = onClick,
                                    onLongClick = onLongClick
                                )
                            }
                        )
                    }
                )
            } else emptyContent()
        }
    )
}

@Composable
private fun CrudItem(
    modifier: Modifier = Modifier,
    item: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    RemovableCard(
        modifier = modifier,
        item = item,
        onClick = onClick,
        onLongClick = onLongClick
    )
}

@Composable
private fun RemoveAllContent(
    onRemoveAllClick: () -> Unit
) {
    var isShowingDialog by remember { mutableStateOf(false) }
    val hideDialog = remember { { isShowingDialog = false } }
    val showDialog = remember { { isShowingDialog = true } }

    if (isShowingDialog) {
        AlertDialog(
            onDismissRequest = hideDialog,
            title = { PersianText(stringResource(R.string.clear_all)) },
            text = { PersianText(stringResource(R.string.remove_all_prompt)) },
            confirmButton = {
                Button(
                    onClick = onRemoveAllClick,
                    content = { PersianText(stringResource(R.string.yes)) }
                )
            },
            dismissButton = {
                Button(
                    onClick = hideDialog,
                    content = { PersianText(stringResource(R.string.no)) }
                )
            }
        )
    }
    Button(
        onClick = showDialog,
        shape = DefaultCutShape,
        content = { PersianText(text = stringResource(R.string.clear_all)) }
    )
}