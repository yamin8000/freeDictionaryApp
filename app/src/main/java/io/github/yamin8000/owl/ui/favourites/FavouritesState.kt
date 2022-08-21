/*
 *     Owl: an android app for Owlbot Dictionary API
 *     FavouritesProvider.kt Created by Yamin Siahmargooei at 2022/8/21
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

package io.github.yamin8000.owl.ui.favourites

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import io.github.yamin8000.owl.util.favouritesDataStore

class FavouritesState(
    val context: Context
) {
    fun getFavourites() = context.favouritesDataStore.data
}

@Composable
fun rememberFavouritesState(
    context: Context = LocalContext.current
) = remember(context) { FavouritesState(context) }