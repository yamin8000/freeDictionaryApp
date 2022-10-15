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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val Context.historyDataStore: DataStore<Preferences> by preferencesDataStore(name = "history")
val Context.favouritesDataStore: DataStore<Preferences> by preferencesDataStore(name = "favourites")

class MainActivity : ComponentActivity() {

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scope.launch {
            val theme = getCurrentTheme()
            setContent { MainContent(theme) }
        }
    }

    private suspend fun getCurrentTheme() = ThemeSetting.valueOf(
        DataStoreHelper(settingsDataStore).getString(Constants.theme) ?: ThemeSetting.System.name
    )
}

@Composable
private fun MainContent(
    currentTheme: ThemeSetting
) {
    var theme by remember { mutableStateOf(currentTheme) }

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

            composable(Nav.Routes.about) {
                AboutContent { navController.popBackStack() }
            }

            composable(Nav.Routes.favourites) {
                FavouritesContent(
                    onFavouritesItemClick = { favourite -> navController.navigate("${Nav.Routes.home}/${favourite}") },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Nav.Routes.history) {
                HistoryContent(
                    onHistoryItemClick = { history -> navController.navigate("${Nav.Routes.home}/${history}") },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Nav.Routes.settings) {
                SettingsContent(
                    onThemeChanged = { newTheme -> theme = newTheme },
                    onBackClick = { navController.popBackStack() }
                )
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