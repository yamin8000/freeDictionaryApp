/*
 *     Owl: an android app for Owlbot Dictionary API
 *     SettingsState.kt Created by Yamin Siahmargooei at 2022/9/20
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

package io.github.yamin8000.owl.content.settings

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import io.github.yamin8000.owl.content.settingsDataStore
import io.github.yamin8000.owl.util.Constants
import io.github.yamin8000.owl.util.DataStoreHelper
import kotlinx.coroutines.launch
import java.util.Locale

class SettingsState(
    context: Context,
    val scope: LifecycleCoroutineScope,
    val themeSetting: MutableState<ThemeSetting>,
    var ttsLang: MutableState<String>,
    val isVibrating: MutableState<Boolean>
) {
    private val dataStore = DataStoreHelper(context.settingsDataStore)

    init {
        scope.launch {
            themeSetting.value = ThemeSetting.valueOf(
                dataStore.getString(Constants.THEME) ?: ThemeSetting.System.name
            )
            ttsLang.value = dataStore.getString(Constants.TTS_LANG) ?: Locale.US.toLanguageTag()
            isVibrating.value = dataStore.getBool(Constants.IS_VIBRATING) ?: true
        }
    }

    suspend fun updateTtsLang(
        newTtsLang: String
    ) {
        ttsLang.value = newTtsLang
        dataStore.setString(Constants.TTS_LANG, newTtsLang)
    }

    suspend fun updateThemeSetting(
        newTheme: ThemeSetting
    ) {
        themeSetting.value = newTheme
        dataStore.setString(Constants.THEME, newTheme.name)
    }

    suspend fun updateVibrationSetting(
        newVibrationSetting: Boolean
    ) {
        isVibrating.value = newVibrationSetting
        dataStore.setBool(Constants.IS_VIBRATING, newVibrationSetting)
    }
}

@Composable
fun rememberSettingsState(
    context: Context = LocalContext.current,
    coroutineScope: LifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope,
    themeSetting: MutableState<ThemeSetting> = rememberSaveable { mutableStateOf(ThemeSetting.System) },
    ttsLang: MutableState<String> = rememberSaveable { mutableStateOf(Locale.US.toLanguageTag()) },
    isVibrating: MutableState<Boolean> = rememberSaveable { mutableStateOf(true) }
) = remember(context, themeSetting, coroutineScope, ttsLang, isVibrating) {
    SettingsState(context, coroutineScope, themeSetting, ttsLang, isVibrating)
}