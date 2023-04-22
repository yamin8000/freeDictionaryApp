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

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import io.github.yamin8000.owl.content.favourites.FavouritesContent
import io.github.yamin8000.owl.content.history.HistoryContent
import io.github.yamin8000.owl.content.home.HomeContent
import io.github.yamin8000.owl.content.settings.SettingsContent
import io.github.yamin8000.owl.content.settings.ThemeSetting
import io.github.yamin8000.owl.db.AppDatabase
import io.github.yamin8000.owl.ui.navigation.Nav
import io.github.yamin8000.owl.ui.theme.OwlTheme
import io.github.yamin8000.owl.util.Constants
import io.github.yamin8000.owl.util.Constants.db
import io.github.yamin8000.owl.util.DataStoreHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FreeMainActivity : ComponentActivity() {

    private val scope = CoroutineScope(Dispatchers.Main)

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scope.launch {
            db = createDb()
            val shareData = handleShareData()
            val theme = getCurrentTheme()
            setContent { Scaffold { MainContent(theme, shareData) } }
        }
    }

    private fun createDb() = Room.databaseBuilder(
        this,
        AppDatabase::class.java,
        "db"
    ).build()

    private fun handleShareData(): String? {
        return if (intent.type == "text/plain") {
            when (intent.action) {
                Intent.ACTION_TRANSLATE, Intent.ACTION_DEFINE, Intent.ACTION_SEND -> {
                    intent.getStringExtra(Intent.EXTRA_TEXT)
                }

                Intent.ACTION_PROCESS_TEXT -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT)
                    else null
                }

                else -> null
            }
        } else null
    }

    private suspend fun getCurrentTheme() = ThemeSetting.valueOf(
        DataStoreHelper(settingsDataStore).getString(Constants.THEME) ?: ThemeSetting.System.name
    )
}

@Composable
private fun MainContent(
    currentTheme: ThemeSetting,
    shareData: String?
) {
    var theme by remember { mutableStateOf(currentTheme) }

    OwlTheme(
        isDarkTheme = isDarkTheme(theme, isSystemInDarkTheme()),
        isDynamicColor = theme == ThemeSetting.System
    ) {

        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "${Nav.Routes.Home}/{${Nav.Arguments.Search}}"
        ) {
            composable("${Nav.Routes.Home}/{${Nav.Arguments.Search}}") {
                var searchTerm = it.arguments?.getString(Nav.Arguments.Search.toString())
                if (searchTerm == null && shareData != null) searchTerm = shareData.toString()
                HomeContent(
                    searchTerm = searchTerm,
                    onFavouritesClick = { navController.navigate(Nav.Routes.Favourites.toString()) },
                    onHistoryClick = { navController.navigate(Nav.Routes.History.toString()) },
                    onInfoClick = { navController.navigate(Nav.Routes.About.toString()) },
                    onSettingsClick = { navController.navigate(Nav.Routes.Settings.toString()) }
                )
            }

            composable(Nav.Routes.About.toString()) {
                AboutContent { navController.popBackStack() }
            }

            composable(Nav.Routes.Favourites.toString()) {
                FavouritesContent(
                    onFavouritesItemClick = { favourite -> navController.navigate("${Nav.Routes.Home}/${favourite}") },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Nav.Routes.History.toString()) {
                HistoryContent(
                    onHistoryItemClick = { history -> navController.navigate("${Nav.Routes.Home}/${history}") },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Nav.Routes.Settings.toString()) {
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