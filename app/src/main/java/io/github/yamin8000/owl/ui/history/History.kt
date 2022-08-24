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

package io.github.yamin8000.owl.ui.history

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.composable.EmptyListErrorText
import io.github.yamin8000.owl.ui.composable.PersianText
import io.github.yamin8000.owl.ui.composable.RemovableCard
import io.github.yamin8000.owl.ui.util.navigation.Nav
import kotlinx.coroutines.launch

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun HistoryContent(
    navController: NavHostController? = null
) {
    val historyState = rememberHistoryState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PersianText(
                modifier = Modifier.padding(16.dp),
                text = stringResource(id = R.string.search_history),
                fontSize = 20.sp
            )
            if (historyState.history.value.isEmpty())
                EmptyListErrorText()
            if (historyState.history.value.isNotEmpty())
                RemoveAlHistoryButton(historyState)
            LazyVerticalGrid(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                columns = GridCells.Fixed(2)
            ) {
                items(historyState.history.value.toList()) {
                    HistoryItem(
                        it,
                        historyState,
                    ) { history -> navController?.navigate("${Nav.Routes.home}/${history}") }
                }
            }
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
    historyState: HistoryState,
    onClick: ((String) -> Unit)? = null
) {
    RemovableCard(
        item = history,
        onClick = { onClick?.invoke(history) },
        onLongClick = {
            historyState.lifeCycleScope.launch {
                historyState.removeSingleHistory(history)
            }
        }
    )
}
