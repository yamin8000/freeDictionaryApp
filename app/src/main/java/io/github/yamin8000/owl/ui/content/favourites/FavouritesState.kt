/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     FavouritesState.kt Copyrighted by Yamin Siahmargooei at 2023/8/26
 *     FavouritesState.kt Last modified at 2023/8/26
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

package io.github.yamin8000.owl.ui.content.favourites

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
import io.github.yamin8000.owl.ui.favouritesDataStore
import io.github.yamin8000.owl.util.list.ListSatiation
import kotlinx.coroutines.launch

internal class FavouritesState(
    private val context: Context,
    val scope: LifecycleCoroutineScope,
    var favourites: MutableState<Set<String>>
) {

    init {
        scope.launch { fetchFavourites() }
    }

    val listSatiation: ListSatiation
        get() {
            return when (favourites.value.size) {
                0 -> ListSatiation.Empty
                else -> ListSatiation.Partial
            }
        }

    private suspend fun fetchFavourites() {
        context.favouritesDataStore.data.collect { preferences ->
            val newSet = favourites.value.toMutableSet()
            preferences.asMap().forEach { entry -> newSet.add(entry.value.toString()) }
            favourites.value = newSet
        }
    }

    suspend fun removeFavourite(
        favourite: String
    ) {
        context.favouritesDataStore.edit { it.remove(stringPreferencesKey(favourite)) }
        val newSet = favourites.value.toMutableSet()
        newSet.remove(favourite)
        favourites.value = newSet
    }
}

@Composable
internal fun rememberFavouritesState(
    context: Context = LocalContext.current,
    lifeCycleScope: LifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope,
    favourites: MutableState<Set<String>> = rememberSaveable { mutableStateOf(emptySet()) }
) = remember(context) { FavouritesState(context, lifeCycleScope, favourites) }