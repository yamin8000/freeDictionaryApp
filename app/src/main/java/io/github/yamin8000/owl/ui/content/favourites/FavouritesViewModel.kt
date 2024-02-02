/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     FavouritesViewModel.kt Copyrighted by Yamin Siahmargooei at 2024/2/1
 *     FavouritesViewModel.kt Last modified at 2024/2/1
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

package io.github.yamin8000.owl.ui.content.favourites

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class FavouritesViewModel(
    private val favouritesDataStore: DataStore<Preferences>
) : ViewModel() {
    val scope = viewModelScope

    private var _favourites = MutableStateFlow(setOf<String>())
    val favourites = _favourites.asStateFlow()

    init {
        scope.launch {
            favouritesDataStore.data.collect { preferences ->
                _favourites.value = buildSet {
                    preferences.asMap().forEach { entry ->
                        add(entry.key.toString())
                    }
                }
            }
        }
    }

    suspend fun remove(
        favourite: String
    ) {
        favouritesDataStore.edit { it.remove(stringPreferencesKey(favourite)) }
        val data = _favourites.value.toMutableSet()
        data.remove(favourite)
        _favourites.value = data
    }

    suspend fun removeAll() {
        favouritesDataStore.edit { it.clear() }
        _favourites.value = emptySet()
    }

    suspend fun add(
        favourite: String
    ) = withContext(scope.coroutineContext) {
        favouritesDataStore.edit {
            it[stringPreferencesKey(favourite)] = favourite
        }
    }
}