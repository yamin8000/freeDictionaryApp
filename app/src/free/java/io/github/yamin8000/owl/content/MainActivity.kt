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
            shareData = shareData,
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