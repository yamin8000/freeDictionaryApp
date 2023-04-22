/*
 *     Owl/Owl.app.main
 *     App.kt Copyrighted by Yamin Siahmargooei at 2023/4/22
 *     App.kt Last modified at 2023/4/22
 *     This file is part of Owl/Owl.app.main.
 *     Copyright (C) 2023  Yamin Siahmargooei
 *
 *     Owl/Owl.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Owl/Owl.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Owl.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.content

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val Context.historyDataStore: DataStore<Preferences> by preferencesDataStore(name = "history")
val Context.favouritesDataStore: DataStore<Preferences> by preferencesDataStore(name = "favourites")

class App : Application()