/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     CommonActivity.kt Copyrighted by Yamin Siahmargooei at 2023/8/26
 *     CommonActivity.kt Last modified at 2023/8/26
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

package io.github.yamin8000.owl.content

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
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
import io.github.yamin8000.owl.util.DataStoreHelper
import io.github.yamin8000.owl.util.log
import kotlinx.coroutines.runBlocking

abstract class CommonActivity : ComponentActivity() {

    protected var outsideInput: String? = null

    protected var theme: ThemeSetting = ThemeSetting.System

    protected val startDestination = "${Nav.Routes.Home}/{${Nav.Arguments.Search}}"

    protected val route: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Constants.db = createDb()
        outsideInput = handleOutsideInputIntent()
        try {
            runBlocking { theme = getCurrentTheme() }
        } catch (e: InterruptedException) {
            log(e.stackTraceToString())
        }
    }

    @Composable
    protected fun MainContent(
        currentTheme: ThemeSetting,
        content: @Composable () -> Unit
    ) {
        OwlTheme(
            isDarkTheme = isDarkTheme(currentTheme, isSystemInDarkTheme()),
            isDynamicColor = currentTheme == ThemeSetting.System,
            content = content
        )
    }

    protected fun mainNavigationGraph(
        navController: NavHostController,
        outsideInput: String?,
        onThemeChanged: (ThemeSetting) -> Unit
    ): NavGraphBuilder.() -> Unit = {
        composable("${Nav.Routes.Home}/{${Nav.Arguments.Search}}") {
            var searchTerm = it.arguments?.getString(Nav.Arguments.Search.toString())
            if (searchTerm == null && outsideInput != null)
                searchTerm = outsideInput.toString()
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
                onThemeChanged = onThemeChanged,
                onBackClick = { navController.popBackStack() }
            )
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

    private fun createDb() = Room.databaseBuilder(
        this,
        AppDatabase::class.java,
        "db"
    ).build()

    private fun handleOutsideInputIntent(): String? {
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