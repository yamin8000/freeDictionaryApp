/*
 *     freeDictionaryApp/freeDictionaryApp.feature_favourites.main
 *     Favourites.kt Copyrighted by Yamin Siahmargooei at 2024/8/25
 *     Favourites.kt Last modified at 2024/8/18
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_favourites.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_favourites.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_favourites.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_favourites

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.yamin8000.owl.common.ui.components.CrudContent
import io.github.yamin8000.owl.strings.R
import kotlinx.collections.immutable.toPersistentList

@Composable
fun FavouritesScreen(
    vm: FavouritesViewModel = hiltViewModel(),
    onFavouritesItemClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val state = vm.state.collectAsStateWithLifecycle().value
    CrudContent(
        title = stringResource(R.string.favourites),
        items = state.favourites.toPersistentList(),
        onBackClick = onBackClick,
        onRemoveAll = { vm.onEvent(FavouriteEvent.RemoveAll) },
        onRemoveSingle = { vm.onEvent(FavouriteEvent.Remove(it)) },
        onItemClick = onFavouritesItemClick
    )
}