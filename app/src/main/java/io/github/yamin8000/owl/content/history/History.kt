/*
 *     Owl: an android app for Owlbot Dictionary API
 *     History.kt Created by Yamin Siahmargooei at 2022/8/24
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

package io.github.yamin8000.owl.content.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.composable.*
import io.github.yamin8000.owl.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.util.list.ListSatiation
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryContent(
    onHistoryItemClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val state = rememberHistoryState()

    ScaffoldWithTitle(
        title = stringResource(R.string.search_history),
        onBackClick = onBackClick,
        content = {
            when (state.listSatiation) {
                ListSatiation.Empty -> EmptyList()
                ListSatiation.Partial -> {
                    val list = state.history.value.toList()
                    LazyVerticalGrid(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        columns = GridCells.Fixed(2),
                        content = {
                            item(
                                span = { GridItemSpan(maxCurrentLineSpan) },
                                content = {
                                    RemoveAlHistoryButton {
                                        state.lifeCycleScope.launch { state.removeAllHistory() }
                                    }
                                }
                            )
                            items(
                                span = { GridItemSpan(1) },
                                count = list.size,
                                itemContent = {
                                    HistoryItem(
                                        history = list[it],
                                        onClick = onHistoryItemClick,
                                        onLongClick = {
                                            state.lifeCycleScope.launch {
                                                state.removeSingleHistory(it)
                                            }
                                        }
                                    )
                                }
                            )
                        }
                    )
                }
            }
        }
    )
}

@Composable
private fun RemoveAlHistoryButton(
    onRemoveAllClick: () -> Unit
) {
    Button(
        onClick = onRemoveAllClick,
        shape = DefaultCutShape,
        content = { PersianText(text = stringResource(R.string.clear_all)) }
    )
}

@Composable
fun HistoryItem(
    history: String,
    onClick: (String) -> Unit,
    onLongClick: (String) -> Unit
) {
    RemovableCard(
        item = history,
        onClick = { onClick.invoke(history) },
        onLongClick = { onLongClick(history) }
    )
}