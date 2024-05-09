/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     SettingsViewModel.kt Copyrighted by Yamin Siahmargooei at 2024/5/9
 *     SettingsViewModel.kt Last modified at 2024/3/23
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

package io.github.yamin8000.owl.ui.content.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.yamin8000.owl.data.DataStoreRepository
import io.github.yamin8000.owl.util.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

internal class SettingsViewModel(
    private val settings: DataStoreRepository
) : ViewModel() {
    private val scope = viewModelScope

    private var _themeSetting = MutableStateFlow(ThemeSetting.System)
    val themeSetting = _themeSetting.asStateFlow()

    private var _ttsLang = MutableStateFlow(Locale.US.toLanguageTag())
    val ttsLang = _ttsLang.asStateFlow()

    private var _isVibrating = MutableStateFlow(true)
    val isVibrating = _isVibrating.asStateFlow()

    private var _isStartingBlank = MutableStateFlow(true)
    val isStartingBlank = _isStartingBlank.asStateFlow()

    init {
        scope.launch {
            _themeSetting.value = ThemeSetting.valueOf(
                settings.getString(Constants.THEME) ?: ThemeSetting.System.name
            )
            _ttsLang.value = settings.getString(Constants.TTS_LANG) ?: Locale.US.toLanguageTag()
            _isVibrating.value = settings.getBool(Constants.IS_VIBRATING) ?: true
            _isStartingBlank.value = settings.getBool(Constants.IS_STARTING_BLANK) ?: true
        }
    }

    fun updateTtsLang(
        newTtsLang: String
    ) {
        scope.launch {
            _ttsLang.value = newTtsLang
            settings.setString(Constants.TTS_LANG, newTtsLang)
        }
    }

    fun updateThemeSetting(
        newTheme: ThemeSetting
    ) {
        scope.launch {
            _themeSetting.value = newTheme
            settings.setString(Constants.THEME, newTheme.name)
        }
    }

    fun updateVibrationSetting(
        newVibrationSetting: Boolean
    ) {
        scope.launch {
            _isVibrating.value = newVibrationSetting
            settings.setBool(Constants.IS_VIBRATING, newVibrationSetting)
        }
    }

    fun updateStartingBlank(
        isStartingWithBlank: Boolean
    ) {
        scope.launch {
            _isStartingBlank.value = isStartingWithBlank
            settings.setBool(Constants.IS_STARTING_BLANK, isStartingWithBlank)
        }
    }
}