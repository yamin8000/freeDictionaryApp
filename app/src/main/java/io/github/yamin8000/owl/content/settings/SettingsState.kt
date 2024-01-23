/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     SettingsState.kt Copyrighted by Yamin Siahmargooei at 2023/8/26
 *     SettingsState.kt Last modified at 2023/8/26
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

internal class SettingsState(
    context: Context,
    val scope: LifecycleCoroutineScope,
    val themeSetting: MutableState<ThemeSetting>,
    var ttsLang: MutableState<String>,
    val isVibrating: MutableState<Boolean>,
    val isStartingWithBlank: MutableState<Boolean>
) {
    private val dataStore = DataStoreHelper(context.settingsDataStore)

    init {
        scope.launch {
            themeSetting.value = ThemeSetting.valueOf(
                dataStore.getString(Constants.THEME) ?: ThemeSetting.System.name
            )
            ttsLang.value = dataStore.getString(Constants.TTS_LANG) ?: Locale.US.toLanguageTag()
            isVibrating.value = dataStore.getBool(Constants.IS_VIBRATING) ?: true
            isStartingWithBlank.value = dataStore.getBool(Constants.IS_STARTING_BLANK) ?: true
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

    suspend fun updateStartingBlank(
        isStartingWithBlank: Boolean
    ) {
        this.isStartingWithBlank.value = isStartingWithBlank
        dataStore.setBool(Constants.IS_STARTING_BLANK, isStartingWithBlank)
    }
}

@Composable
internal fun rememberSettingsState(
    context: Context = LocalContext.current,
    coroutineScope: LifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope,
    themeSetting: MutableState<ThemeSetting> = rememberSaveable { mutableStateOf(ThemeSetting.System) },
    ttsLang: MutableState<String> = rememberSaveable { mutableStateOf(Locale.US.toLanguageTag()) },
    isVibrating: MutableState<Boolean> = rememberSaveable { mutableStateOf(true) },
    isStartingWithBlank: MutableState<Boolean> = rememberSaveable { mutableStateOf(true) }
) = remember(context, themeSetting, coroutineScope, ttsLang, isVibrating, isStartingWithBlank) {
    SettingsState(context, coroutineScope, themeSetting, ttsLang, isVibrating, isStartingWithBlank)
}