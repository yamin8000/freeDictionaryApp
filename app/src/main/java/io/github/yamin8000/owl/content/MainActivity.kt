/*
 *     Owl: an android app for Owlbot Dictionary API 
 *     MainActivity.kt Created by Yamin Siahmargooei at 2022/9/20
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

package io.github.yamin8000.owl.content

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.yamin8000.owl.content.favourites.FavouritesContent
import io.github.yamin8000.owl.content.history.HistoryContent
import io.github.yamin8000.owl.content.home.HomeContent
import io.github.yamin8000.owl.content.settings.SettingsContent
import io.github.yamin8000.owl.content.settings.ThemeSetting
import io.github.yamin8000.owl.ui.navigation.Nav
import io.github.yamin8000.owl.ui.theme.OwlTheme
import io.github.yamin8000.owl.util.Constants
import io.github.yamin8000.owl.util.DataStoreHelper

val Context.settings: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainContent() }
    }

    @Composable
    private fun MainContent() {
        var theme by remember { mutableStateOf(ThemeSetting.System) }

        LaunchedEffect(Unit) {
            val dataStore = DataStoreHelper(settings)
            theme = ThemeSetting.valueOf(
                dataStore.getString(Constants.theme) ?: ThemeSetting.System.name
            )
        }

        OwlTheme(
            isDarkTheme = isDarkTheme(theme, isSystemInDarkTheme()),
            isDynamicColor = theme == ThemeSetting.System
        ) {
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
                        onInfoClick = { navController.navigate(Nav.Routes.about) },
                        onSettingsClick = { navController.navigate(Nav.Routes.settings) }
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

                composable(Nav.Routes.settings) {
                    SettingsContent { newTheme -> theme = newTheme }
                }
            }
        }
    }

    private fun isDarkTheme(
        themeSetting: ThemeSetting,
        isSystemInDarkTheme: Boolean
    ): Boolean {
        if (themeSetting == ThemeSetting.Light) return false
        if (themeSetting == ThemeSetting.System) return isSystemInDarkTheme
        if (themeSetting == ThemeSetting.Dark) return true
        return false
    }
}