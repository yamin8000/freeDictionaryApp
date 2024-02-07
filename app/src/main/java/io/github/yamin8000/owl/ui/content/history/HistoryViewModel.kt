/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     HistoryViewModel.kt Copyrighted by Yamin Siahmargooei at 2024/1/28
 *     HistoryViewModel.kt Last modified at 2024/1/28
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

package io.github.yamin8000.owl.ui.content.history

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.yamin8000.owl.util.log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val historyDataStore: DataStore<Preferences>
) : ViewModel() {
    private val scope = viewModelScope

    private var _history = MutableStateFlow(emptySet<String>())
    val history = _history

    init {
        scope.launch {
            historyDataStore.data.collect { preferences ->
                _history.value = buildSet {
                    preferences.asMap().forEach { entry ->
                        add(entry.value.toString())
                    }
                }
            }
        }
    }

    fun remove(
        history: String
    ) = scope.launch {
        historyDataStore.edit { it.remove(stringPreferencesKey(history)) }
        val data = this@HistoryViewModel.history.value.toMutableSet()
        data.remove(history)
        _history.value = data
    }

    fun removeAll() = scope.launch {
        historyDataStore.edit { it.clear() }
        _history.value = emptySet()
    }

    fun add(
        history: String
    ) = scope.launch {
        historyDataStore.edit {
            it[stringPreferencesKey(history)] = history
        }
    }
}