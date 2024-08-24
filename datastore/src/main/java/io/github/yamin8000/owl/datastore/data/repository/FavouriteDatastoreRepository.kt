/*
 *     freeDictionaryApp/freeDictionaryApp.datastore.main
 *     FavouriteDatastoreRepository.kt Copyrighted by Yamin Siahmargooei at 2024/8/25
 *     FavouriteDatastoreRepository.kt Last modified at 2024/8/25
 *     This file is part of freeDictionaryApp/freeDictionaryApp.datastore.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.datastore.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.datastore.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.datastore.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.github.yamin8000.owl.datastore.domain.repository.BaseDatastoreRepository
import io.github.yamin8000.owl.datastore.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavouriteDatastoreRepository(
    private val datastore: DataStore<Preferences>
) : FavouriteRepository, BaseDatastoreRepository by BasicDatastoreRepository(datastore) {

    override suspend fun add(history: String) {
        datastore.edit { it[stringPreferencesKey(history)] = history }
    }

    override suspend fun remove(history: String) {
        datastore.edit { it.remove(stringPreferencesKey(history)) }
    }

    override suspend fun removeAll() {
        datastore.edit { it.clear() }
    }

    override suspend fun all(): Flow<List<String>> {
        val test = datastore.data.map { preferences ->
            preferences.asMap().map { entry ->
                entry.value.toString()
            }
        }
        return test
    }
}