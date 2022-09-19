/*
 *     Owl: an android app for Owlbot Dictionary API
 *     MainActivity.kt Created by Yamin Siahmargooei at 2022/8/22
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

package io.github.yamin8000.owl.ui.util

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.yamin8000.owl.content.AboutContent
import io.github.yamin8000.owl.content.favourites.FavouritesContent
import io.github.yamin8000.owl.content.history.HistoryContent
import io.github.yamin8000.owl.content.home.HomeContent
import io.github.yamin8000.owl.ui.util.navigation.Nav
import io.github.yamin8000.owl.ui.util.theme.OwlTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { OwlTheme { MainContent() } }
    }

    @Composable
    private fun MainContent() {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "${Nav.Routes.home}/{${Nav.Arguments.search}}"
        ) {
            composable("${Nav.Routes.home}/{${Nav.Arguments.search}}") {
                HomeContent(
                    searchTerm = it.arguments?.getString(Nav.Arguments.search),
                    onFavouritesClick = { navController.navigate(Nav.Routes.favourites) },
                    onHistoryClick = { navController.navigate(Nav.Routes.history) },
                    onInfoClick = { navController.navigate(Nav.Routes.about) }
                )
            }

            composable(Nav.Routes.about) { AboutContent() }

            composable(Nav.Routes.favourites) {
                FavouritesContent { favourite ->
                    navController.navigate("${Nav.Routes.home}/${favourite}")
                }
            }

            composable(Nav.Routes.history) {
                HistoryContent { history ->
                    navController.navigate("${Nav.Routes.home}/${history}")
                }
            }
        }
    }
}