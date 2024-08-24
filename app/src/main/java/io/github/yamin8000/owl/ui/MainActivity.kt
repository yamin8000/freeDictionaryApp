/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     MainActivity.kt Copyrighted by Yamin Siahmargooei at 2024/8/18
 *     MainActivity.kt Last modified at 2024/8/18
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

package io.github.yamin8000.owl.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.yamin8000.karlancer.feature_history.ui.HistoryScreen
import io.github.yamin8000.owl.common.ui.theme.AppTheme
import io.github.yamin8000.owl.datastore.domain.model.ThemeType
import io.github.yamin8000.owl.datastore.domain.usecase.settings.SettingUseCases
import io.github.yamin8000.owl.feature_favourites.FavouritesScreen
import io.github.yamin8000.owl.feature_home.di.HomeViewModelFactory
import io.github.yamin8000.owl.feature_home.ui.HomeScreen
import io.github.yamin8000.owl.feature_home.ui.HomeViewModel
import io.github.yamin8000.owl.feature_settings.ui.SettingsScreen
import io.github.yamin8000.owl.ui.navigation.Nav
import io.github.yamin8000.owl.util.log
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {

    private var intentSearch: String? = null

    @Inject
    lateinit var settings: SettingUseCases

    private lateinit var initTheme: ThemeType

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        intentSearch = handleOutsideInputIntent()

        handleTheme()

        setContent {
            val (theme, onThemeChanged) = remember { mutableStateOf(initTheme) }
            AppTheme(
                isDarkTheme = isDarkTheme(theme, isSystemInDarkTheme()),
                isOledTheme = theme == ThemeType.Darker,
                isDynamicColor = theme == ThemeType.System,
                content = {
                    Scaffold {
                        MainNav(onThemeChanged = onThemeChanged)
                    }
                }
            )
        }
    }

    private fun handleTheme() {
        try {
            runBlocking {
                initTheme = settings.getTheme()
            }
        } catch (e: InterruptedException) {
            log(e.stackTraceToString())
        }

        if (!this::initTheme.isInitialized) {
            initTheme = ThemeType.System
        }
    }

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

                else -> {
                    null
                }
            }
        } else {
            null
        }
    }

    private fun isDarkTheme(
        theme: ThemeType,
        isSystemInDarkTheme: Boolean
    ) = when (theme) {
        ThemeType.Light -> false
        ThemeType.System -> isSystemInDarkTheme
        ThemeType.Dark, ThemeType.Darker -> true
    }

    @Composable
    private fun MainNav(
        onThemeChanged: (ThemeType) -> Unit
    ) {
        val start = "${Nav.Route.Home}/{Search}"
        val navController = rememberNavController()
        val onBackClick: () -> Unit = remember { { navController.navigateUp() } }
        NavHost(
            navController = navController,
            startDestination = start,
            enterTransition = { fadeIn(animationSpec = tween(100)) },
            exitTransition = { fadeOut(animationSpec = tween(100)) },
            builder = {
                composable(start) {
                    val argSearch = it.arguments?.getString("Search")
                    HomeScreen(
                        onNavigateToAbout = { navController.navigate(Nav.Route.About()) },
                        onNavigateToSettings = { navController.navigate(Nav.Route.Settings()) },
                        onNavigateToFavourites = { navController.navigate(Nav.Route.Favourites()) },
                        onNavigateToHistory = { navController.navigate(Nav.Route.History()) },
                        vm = hiltViewModel<HomeViewModel, HomeViewModelFactory>(
                            creationCallback = { factory ->
                                factory.create(
                                    intentSearch = intentSearch,
                                    navigationSearch = argSearch
                                )
                            }
                        )
                    )
                }

                composable(Nav.Route.About.toString()) {
                    AboutContent(
                        onBackClick = onBackClick
                    )
                }

                composable(Nav.Route.Favourites()) {
                    FavouritesScreen(
                        onBackClick = onBackClick,
                        onFavouritesItemClick = { navController.navigate("${Nav.Route.Home}/${it}") },
                    )
                }

                composable(Nav.Route.History()) {
                    HistoryScreen(
                        onBackClick = onBackClick,
                        onHistoryItemClick = { navController.navigate("${Nav.Route.Home}/${it}") }
                    )
                }

                composable(Nav.Route.Settings()) {
                    SettingsScreen(
                        onBackClick = onBackClick,
                        onThemeChanged = onThemeChanged
                    )
                }
            }
        )
    }
}