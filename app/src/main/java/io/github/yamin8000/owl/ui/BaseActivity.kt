/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     BaseActivity.kt Copyrighted by Yamin Siahmargooei at 2024/9/5
 *     BaseActivity.kt Last modified at 2024/9/5
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

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dagger.hilt.android.AndroidEntryPoint
import io.github.yamin8000.owl.common.ui.theme.AppTheme
import io.github.yamin8000.owl.datastore.domain.model.ThemeType
import io.github.yamin8000.owl.datastore.domain.usecase.settings.SettingUseCases
import io.github.yamin8000.owl.util.log
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
internal open class BaseActivity : ComponentActivity() {

    @Inject
    lateinit var settings: SettingUseCases

    var appTheme by mutableStateOf<ThemeType>(ThemeType.System)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appTheme = findTheme()
    }

    protected fun showContent(
        content: @Composable () -> Unit,
    ) {
        setContent {
            val isSystemInDarkTheme = (resources.configuration.uiMode
                .and(Configuration.UI_MODE_NIGHT_MASK)) == Configuration.UI_MODE_NIGHT_YES
            AppTheme(
                isDarkTheme = isDarkTheme(appTheme, isSystemInDarkTheme),
                isOledTheme = appTheme == ThemeType.Darker,
                isDynamicColor = appTheme == ThemeType.System,
                content = content
            )
        }
    }

    private fun findTheme(): ThemeType {
        return try {
            runBlocking {
                settings.getTheme()
            }
        } catch (e: InterruptedException) {
            log(e.stackTraceToString())
            ThemeType.System
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
}