/*
 *     Owl/Owl.app.main
 *     AdMainActivity.kt Copyrighted by Yamin Siahmargooei at 2023/4/22
 *     AdMainActivity.kt Last modified at 2023/4/22
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
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import io.github.yamin8000.owl.ad.AdConstants
import io.github.yamin8000.owl.ad.AdHelper
import io.github.yamin8000.owl.ad.TapsellAdContent
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
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusInitListener
import ir.tapsell.plus.model.AdNetworkError
import ir.tapsell.plus.model.AdNetworks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdMainActivity : ComponentActivity() {

    private val scope = CoroutineScope(Dispatchers.Main)

    @Suppress("SameParameterValue")
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scope.launch {
            Constants.db = createDb()
            val shareData = handleShareData()
            val theme = getCurrentTheme()
            initTapsellAd()
            setContent {
                var adView by remember { mutableStateOf<ViewGroup?>(null) }
                var adId: String by remember { mutableStateOf("") }

                LaunchedEffect(Unit) {
                    adId = AdHelper.requestTapsellAd(this@AdMainActivity)
                    AdHelper.showTapsellAd(this@AdMainActivity, adId, adView)
                }
                Scaffold {
                    AdMainContent(
                        currentTheme = theme,
                        shareData = shareData,
                        onCreated = { adView = it },
                        onUpdate = { adView = it },
                    )
                }
            }
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
}

@Composable
fun AdMainContent(
    currentTheme: ThemeSetting,
    shareData: String?,
    onCreated: (ViewGroup) -> Unit,
    onUpdate: (ViewGroup) -> Unit
) {
    var theme by remember { mutableStateOf(currentTheme) }

    OwlTheme(
        isDarkTheme = isDarkTheme(theme, isSystemInDarkTheme()),
        isDynamicColor = theme == ThemeSetting.System
    ) {

        Column {
            val navController = rememberNavController()
            NavHost(
                modifier = Modifier.weight(1f),
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

private fun isDarkTheme(
    themeSetting: ThemeSetting,
    isSystemInDarkTheme: Boolean
): Boolean {
    if (themeSetting == ThemeSetting.Light) return false
    if (themeSetting == ThemeSetting.System) return isSystemInDarkTheme
    if (themeSetting == ThemeSetting.Dark) return true
    return false
}