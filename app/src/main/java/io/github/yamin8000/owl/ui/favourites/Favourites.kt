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

package io.github.yamin8000.owl.ui.favourites

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.composable.PersianText
import io.github.yamin8000.owl.ui.composable.RippleText
import io.github.yamin8000.owl.ui.composable.TextProvider
import io.github.yamin8000.owl.ui.util.navigation.Nav
import io.github.yamin8000.owl.ui.util.theme.OwlTheme
import kotlinx.coroutines.launch

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun FavouritesContent(
    navController: NavHostController? = null,
) {
    val favouritesState = rememberFavouritesState()

    OwlTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PersianText(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(id = R.string.favourites),
                    fontSize = 16.sp
                )
                LazyVerticalGrid(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    columns = GridCells.Fixed(2)
                ) {
                    items(favouritesState.favourites.value.toList()) {
                        FavouriteItem(
                            it,
                            favouritesState
                        ) { favourite -> navController?.navigate("${Nav.Routes.home}/${favourite}") }
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun FavouriteItem(
    @PreviewParameter(TextProvider::class)
    favourite: String,
    favouritesState: FavouritesState = rememberFavouritesState(),
    onClick: ((String) -> Unit)? = null
) {
    Card(
        shape = CutCornerShape(15.dp)
    ) {
        RippleText(
            modifier = Modifier.padding(8.dp),
            content = {
                Text(
                    text = favourite,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            },
            onClick = { onClick?.invoke(favourite) },
            onLongClick = {
                favouritesState.lifeCycleScope.launch {
                    favouritesState.removeFavourite(favourite)
                }
            }
        )
    }
}