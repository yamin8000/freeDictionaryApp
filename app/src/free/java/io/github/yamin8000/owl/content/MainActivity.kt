/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     MainActivity.kt Copyrighted by Yamin Siahmargooei at 2023/8/26
 *     MainActivity.kt Last modified at 2023/8/26
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

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import io.github.yamin8000.owl.content.settings.ThemeSetting

class MainActivity : CommonActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var currentTheme by remember { mutableStateOf(theme) }
            MainContent(
                currentTheme = currentTheme,
                content = { Scaffold { FreeMainContent { currentTheme = it } } }
            )
        }
    }

    @Composable
    private fun FreeMainContent(
        onThemeChanged: (ThemeSetting) -> Unit
    ) {
        val navController = rememberNavController()
        val builder = mainNavigationGraph(
            navController = navController,
            outsideInput = outsideInput,
            onThemeChanged = onThemeChanged
        )
        NavHost(
            navController = navController,
            graph = remember(startDestination, route, builder) {
                navController.createGraph(startDestination, route, builder)
            }
        )
    }
}