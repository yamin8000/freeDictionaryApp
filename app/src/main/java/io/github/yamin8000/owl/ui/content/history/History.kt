/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     History.kt Copyrighted by Yamin Siahmargooei at 2023/8/26
 *     History.kt Last modified at 2023/8/26
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

package io.github.yamin8000.owl.ui.content.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.composable.EmptyList
import io.github.yamin8000.owl.ui.composable.PersianText
import io.github.yamin8000.owl.ui.composable.RemovableCard
import io.github.yamin8000.owl.ui.composable.ScaffoldWithTitle
import io.github.yamin8000.owl.ui.historyDataStore
import io.github.yamin8000.owl.ui.theme.DefaultCutShape
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HistoryContent(
    onHistoryItemClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val vm = HistoryViewModel(LocalContext.current.historyDataStore)

    ScaffoldWithTitle(
        title = stringResource(R.string.search_history),
        onBackClick = onBackClick,
        content = {
            val list = vm.history.collectAsState().value.toList()
            if (list.isNotEmpty()) {
                HistoryList(
                    onRemoveAll = { vm.scope.launch { vm.removeAllHistory() } },
                    onRemoveSingle = { vm.scope.launch { vm.removeSingleHistory(it) } },
                    list = list,
                    onItemClick = onHistoryItemClick
                )
            } else EmptyList()
        }
    )
}

@Composable
private fun HistoryList(
    onRemoveAll: () -> Unit,
    onRemoveSingle: (String) -> Unit,
    onItemClick: (String) -> Unit,
    list: List<String>
) {
    LazyVerticalGrid(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        columns = GridCells.Fixed(2),
        content = {
            item(
                span = { GridItemSpan(maxCurrentLineSpan) },
                content = { RemoveAllHistoryContent(onRemoveAllClick = onRemoveAll) }
            )
            items(
                span = { GridItemSpan(1) },
                count = list.size,
                itemContent = {
                    HistoryItem(
                        history = list[it],
                        onClick = onItemClick,
                        onLongClick = { item -> onRemoveSingle(item) }
                    )
                }
            )
        }
    )
}

@Composable
private fun RemoveAllHistoryContent(
    onRemoveAllClick: () -> Unit
) {
    var isShowingDialog by remember { mutableStateOf(false) }
    if (isShowingDialog) {
        AlertDialog(
            onDismissRequest = { isShowingDialog = false },
            title = { PersianText(stringResource(R.string.clear_all)) },
            text = { PersianText(stringResource(R.string.remove_all_history_prompt)) },
            confirmButton = {
                Button(
                    onClick = onRemoveAllClick,
                    content = { PersianText(stringResource(R.string.yes)) }
                )
            },
            dismissButton = {
                Button(
                    onClick = { isShowingDialog = false },
                    content = { PersianText(stringResource(R.string.no)) }
                )
            }
        )
    }
    Button(
        onClick = { isShowingDialog = true },
        shape = DefaultCutShape,
        content = { PersianText(text = stringResource(R.string.clear_all)) }
    )
}

@Composable
private fun HistoryItem(
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