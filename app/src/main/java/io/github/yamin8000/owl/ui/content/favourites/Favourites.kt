/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     Favourites.kt Copyrighted by Yamin Siahmargooei at 2024/5/9
 *     Favourites.kt Last modified at 2024/3/23
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

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.composable.CrudContent
import kotlinx.collections.immutable.PersistentList

@Composable
internal fun FavouritesContent(
    favourites: PersistentList<String>,
    onRemoveAll: () -> Unit,
    onRemove: (String) -> Unit,
    onFavouritesItemClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    CrudContent(
        title = stringResource(R.string.favourites),
        items = favourites,
        onBackClick = onBackClick,
        onRemoveAll = onRemoveAll,
        onRemoveSingle = onRemove,
        onItemClick = onFavouritesItemClick
    )
}