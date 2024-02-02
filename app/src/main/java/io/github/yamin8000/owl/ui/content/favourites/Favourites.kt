/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     Favourites.kt Copyrighted by Yamin Siahmargooei at 2023/8/26
 *     Favourites.kt Last modified at 2023/8/26
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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer

import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.composable.CrudContent
import io.github.yamin8000.owl.ui.favouritesDataStore
import io.github.yamin8000.owl.util.viewModelFactory
import kotlinx.coroutines.launch

@Composable
internal fun FavouritesContent(
    onFavouritesItemClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val vm: FavouritesViewModel = viewModel(factory = viewModelFactory {
        initializer {
            FavouritesViewModel(context.favouritesDataStore)
        }
    })

    val list = vm.favourites.collectAsState().value.toList()
    CrudContent(
        title = stringResource(R.string.favourites),
        items = list,
        onBackClick = onBackClick,
        onRemoveAll = { vm.scope.launch { vm.removeAll() } },
        onRemoveSingle = { index -> vm.scope.launch { vm.remove(list[index]) } },
        onItemClick = { index -> onFavouritesItemClick(list[index]) }
    )
}