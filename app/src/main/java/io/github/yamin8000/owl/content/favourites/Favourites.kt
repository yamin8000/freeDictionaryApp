/*
 *     Owl: an android app for Owlbot Dictionary API
 *     Favourites.kt Created by Yamin Siahmargooei at 2022/8/22
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

package io.github.yamin8000.owl.content.favourites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.composable.EmptyList
import io.github.yamin8000.owl.ui.composable.RemovableCard
import io.github.yamin8000.owl.ui.composable.ScaffoldWithTitle
import io.github.yamin8000.owl.util.list.ListSatiation
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesContent(
    onFavouritesItemClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val state = rememberFavouritesState()

    ScaffoldWithTitle(
        title = stringResource(R.string.favourites),
        onBackClick = onBackClick
    ) {
        when (state.listSatiation) {
            ListSatiation.Empty -> EmptyList()
            ListSatiation.Partial -> {
                FavouritesGrid(
                    favourites = state.favourites.value.toList(),
                    onItemClick = onFavouritesItemClick,
                    onItemLongClick = { favourite ->
                        state.scope.launch { state.removeFavourite(favourite) }
                    }
                )
            }
        }
    }
}

@Composable
fun FavouritesGrid(
    favourites: List<String>,
    onItemClick: (String) -> Unit,
    onItemLongClick: (String) -> Unit
) {
    val span = rememberSaveable { if (favourites.size == 1) 1 else 2 }
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        columns = GridCells.Fixed(span),
        content = {
            items(
                items = favourites,
                itemContent = { favourite ->
                    FavouriteItem(
                        favourite = favourite,
                        onClick = onItemClick,
                        onLongClick = { onItemLongClick(favourite) }
                    )
                }
            )
        }
    )
}

@Composable
private fun FavouriteItem(
    favourite: String,
    onClick: (String) -> Unit,
    onLongClick: () -> Unit
) {
    RemovableCard(
        item = favourite,
        onClick = { onClick(favourite) },
        onLongClick = onLongClick
    )
}