/*
 *     Owl/Owl.app.main
 *     MainActivity.kt Copyrighted by Yamin Siahmargooei at 2023/4/22
 *     MainActivity.kt Last modified at 2023/4/22
 *     This file is part of Owl/Owl.app.main.
 *     Copyright (C) 2023  Yamin Siahmargooei
 *
 *     Owl/Owl.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Owl/Owl.app.main is distributed in the hope that it will be useful,
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
import android.view.ViewGroup
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import io.github.yamin8000.owl.ad.AdConstants
import io.github.yamin8000.owl.ad.AdHelper
import io.github.yamin8000.owl.ad.TapsellAdContent
import io.github.yamin8000.owl.content.settings.ThemeSetting
import io.github.yamin8000.owl.util.log
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusInitListener
import ir.tapsell.plus.model.AdNetworkError
import ir.tapsell.plus.model.AdNetworks

class MainActivity : CommonActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initTapsellAd()
        setContent {
            var currentTheme by remember { mutableStateOf(theme) }
            MainContent(
                currentTheme = currentTheme,
                content = {
                    var adView by remember { mutableStateOf<ViewGroup?>(null) }
                    var adId: String by remember { mutableStateOf("") }

                    LaunchedEffect(Unit) {
                        adId = AdHelper.requestTapsellAd(this@MainActivity)
                        AdHelper.showTapsellAd(this@MainActivity, adId, adView)
                    }
                    Scaffold {
                        AdMainContent(
                            onCreated = { adView = it },
                            onUpdate = { adView = it },
                            onThemeChanged = { currentTheme = it }
                        )
                    }
                }
            )
        }
    }

    private fun initTapsellAd() {
        TapsellPlus.initialize(this, AdConstants.TAPSELL_KEY, object : TapsellPlusInitListener {
            override fun onInitializeSuccess(ads: AdNetworks?) {
                log(ads?.name ?: "Unknown ad name")
            }

            override fun onInitializeFailed(ads: AdNetworks?, error: AdNetworkError?) {
                log(error?.errorMessage ?: "Unknown tapsell init error")
            }
        })
    }

    @Composable
    private fun AdMainContent(
        onCreated: (ViewGroup) -> Unit,
        onUpdate: (ViewGroup) -> Unit,
        onThemeChanged: (ThemeSetting) -> Unit
    ) {
        Column {
            val navController = rememberNavController()
            val builder = mainNavigationGraph(
                navController = navController,
                outsideInput = outsideInput,
                onThemeChanged = onThemeChanged
            )
            NavHost(
                modifier = Modifier.weight(1f),
                navController = navController,
                graph = remember(startDestination, route, builder) {
                    navController.createGraph(startDestination, route, builder)
                }
            )
            TapsellAdContent(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(4.dp)
                    .fillMaxWidth(),
                onCreated = onCreated,
                onUpdate = onUpdate
            )
        }
    }
}