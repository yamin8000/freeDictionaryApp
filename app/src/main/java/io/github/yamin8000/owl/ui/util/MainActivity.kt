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
import io.github.yamin8000.owl.ui.AboutContent
import io.github.yamin8000.owl.ui.favourites.FavouritesContent
import io.github.yamin8000.owl.ui.history.HistoryContent
import io.github.yamin8000.owl.ui.home.HomeContent
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
                HomeContent(navController, it.arguments?.getString(Nav.Arguments.search))
            }

            composable(Nav.Routes.about) {
                AboutContent(navController)
            }

            composable(Nav.Routes.favourites) {
                FavouritesContent(navController)
            }

            composable(Nav.Routes.history) {
                HistoryContent(navController)
            }
        }
    }
}