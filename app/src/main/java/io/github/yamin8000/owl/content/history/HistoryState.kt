/*
 *     Owl: an android app for Owlbot Dictionary API
 *     HistoryState.kt Created by Yamin Siahmargooei at 2022/8/24
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

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import io.github.yamin8000.owl.content.historyDataStore
import io.github.yamin8000.owl.util.list.ListSatiation
import kotlinx.coroutines.launch

class HistoryState(
    private val context: Context,
    val lifeCycleScope: LifecycleCoroutineScope,
    var history: MutableState<Set<String>>
) {

    init {
        lifeCycleScope.launch { fetchHistory() }
    }

    val listSatiation: ListSatiation
        get() {
            return when (history.value.size) {
                0 -> ListSatiation.Empty
                else -> ListSatiation.Partial
            }
        }

    private suspend fun fetchHistory() {
        context.historyDataStore.data.collect { preferences ->
            val newSet = history.value.toMutableSet()
            preferences.asMap().forEach { entry -> newSet.add(entry.value.toString()) }
            history.value = newSet
        }
    }

    suspend fun removeSingleHistory(
        singleHistory: String
    ) {
        context.historyDataStore.edit { it.remove(stringPreferencesKey(singleHistory)) }
        val newSet = history.value.toMutableSet()
        newSet.remove(singleHistory)
        history.value = newSet
    }

    suspend fun removeAllHistory() {
        context.historyDataStore.edit { it.clear() }
        history.value = emptySet()
    }
}

@Composable
fun rememberHistoryState(
    context: Context = LocalContext.current,
    lifeCycleScope: LifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope,
    history: MutableState<Set<String>> = rememberSaveable { mutableStateOf(emptySet()) }
) = remember(context, lifeCycleScope, history) { HistoryState(context, lifeCycleScope, history) }