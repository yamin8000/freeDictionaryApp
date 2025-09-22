/*
 *     freeDictionaryApp/freeDictionaryApp.feature_history.main
 *     History.kt Copyrighted by Yamin Siahmargooei at 2024/8/24
 *     History.kt Last modified at 2024/8/18
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_history.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_history.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_history.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.karlancer.feature_history.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.yamin8000.owl.common.ui.components.crud.CrudContent
import io.github.yamin8000.owl.strings.R

@Composable
fun HistoryScreen(
    onHistoryItemClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    vm: HistoryViewModel = hiltViewModel(),
) {
    val state = vm.state.collectAsStateWithLifecycle().value

    CrudContent(
        modifier = modifier,
        title = stringResource(R.string.search_history),
        items = state.history,
        onBackClick = onBackClick,
        onRemoveAll = { vm.onEvent(HistoryEvent.RemoveAll) },
        onRemoveSingle = { vm.onEvent(HistoryEvent.RemoveHistory(it)) },
        onItemClick = onHistoryItemClick
    )
}