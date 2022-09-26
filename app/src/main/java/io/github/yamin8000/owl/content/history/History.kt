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

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.composable.EmptyListErrorText
import io.github.yamin8000.owl.ui.composable.PersianText
import io.github.yamin8000.owl.ui.composable.RemovableCard
import io.github.yamin8000.owl.ui.composable.SurfaceWithTitle
import io.github.yamin8000.owl.ui.theme.PreviewTheme
import kotlinx.coroutines.launch

@Composable
fun HistoryContent(
    onHistoryItemClick: (String) -> Unit
) {
    val historyState = rememberHistoryState()

    SurfaceWithTitle(
        title = stringResource(R.string.search_history)
    ) {
        if (historyState.history.value.isNotEmpty()) {
            RemoveAlHistoryButton(historyState)
            HistoryGrid(
                history = historyState.history.value.toList(),
                onItemClick = onHistoryItemClick,
                onItemLongClick = { history ->
                    historyState.lifeCycleScope.launch {
                        historyState.removeSingleHistory(history)
                    }
                }
            )
        } else EmptyListErrorText()
    }
}

@Composable
fun HistoryGrid(
    history: List<String>,
    onItemClick: (String) -> Unit,
    onItemLongClick: (String) -> Unit
) {
    LazyVerticalGrid(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        columns = GridCells.Fixed(2)
    ) {
        items(history) {
            HistoryItem(
                it,
                onClick = onItemClick,
                onLongClick = onItemLongClick
            )
        }
    }
}

@Composable
private fun RemoveAlHistoryButton(historyState: HistoryState) {
    Button(onClick = {
        historyState.lifeCycleScope.launch { historyState.removeAllHistory() }
    }) {
        PersianText(text = stringResource(R.string.remove_history))
    }
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun HistoryItemPreview() {
    PreviewTheme { HistoryItem(history = "Owl", onClick = {}, onLongClick = {}) }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun HistoryGridPreview() {
    PreviewTheme {
        HistoryGrid(history = listOf("Owl", "Book", "test"), onItemClick = {}, onItemLongClick = {})
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun Preview() {
    PreviewTheme { HistoryContent {} }
}