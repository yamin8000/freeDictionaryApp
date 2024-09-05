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
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.yamin8000.karlancer.feature_history.ui.HistoryScreen
import io.github.yamin8000.owl.datastore.domain.model.ThemeType
import io.github.yamin8000.owl.feature_favourites.FavouritesScreen
import io.github.yamin8000.owl.feature_home.di.HomeViewModelFactory
import io.github.yamin8000.owl.feature_home.ui.HomeScreen
import io.github.yamin8000.owl.feature_home.ui.HomeViewModel
import io.github.yamin8000.owl.feature_settings.ui.SettingsScreen
import io.github.yamin8000.owl.ui.navigation.Nav

internal class MainActivity : BaseActivity() {

    private var intentSearch: String? = null

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        intentSearch = intent.getStringExtra("Search")

        showContent {
            Scaffold {
                MainNav(onThemeChanged = { appTheme = it })
            }
        }
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