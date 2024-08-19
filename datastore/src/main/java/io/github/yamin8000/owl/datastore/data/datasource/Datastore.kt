/*
 *     freeDictionaryApp/freeDictionaryApp.datastore.main
 *     Datastores.kt Copyrighted by Yamin Siahmargooei at 2024/8/19
 *     Datastores.kt Last modified at 2024/8/19
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

package io.github.yamin8000.owl.datastore.data.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

object Datastore {
    internal val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    internal val Context.historyDataStore: DataStore<Preferences> by preferencesDataStore(name = "history")
    internal val Context.favouritesDataStore: DataStore<Preferences> by preferencesDataStore(name = "favourites")
}