/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     SettingsViewModel.kt Copyrighted by Yamin Siahmargooei at 2024/1/24
 *     SettingsViewModel.kt Last modified at 2024/1/24
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

internal class SettingsViewModel(
    private val repository: DataStoreRepository
) : ViewModel() {
    val scope = viewModelScope

    private var _themeSetting = MutableStateFlow(ThemeSetting.System)
    val themeSetting = _themeSetting

    private var _ttsLang = MutableStateFlow(Locale.US.toLanguageTag())
    val ttsLang = _ttsLang

    private var _isVibrating = MutableStateFlow(true)
    val isVibrating = _isVibrating

    private var _isStartingWithBlankPage = MutableStateFlow(true)
    val isStartingWithBlankPage = _isStartingWithBlankPage

    init {
        viewModelScope.launch {
            _themeSetting.value = ThemeSetting.valueOf(
                repository.getString(Constants.THEME) ?: ThemeSetting.System.name
            )
            _ttsLang.value = repository.getString(Constants.TTS_LANG) ?: Locale.US.toLanguageTag()
            _isVibrating.value = repository.getBool(Constants.IS_VIBRATING) ?: true
            _isStartingWithBlankPage.value = repository.getBool(Constants.IS_STARTING_BLANK) ?: true
        }
    }

    suspend fun updateTtsLang(
        newTtsLang: String
    ) = withContext(scope.coroutineContext) {
        _ttsLang.value = newTtsLang
        repository.setString(Constants.TTS_LANG, newTtsLang)
    }

    suspend fun updateThemeSetting(
        newTheme: ThemeSetting
    ) = withContext(scope.coroutineContext) {
        _themeSetting.value = newTheme
        repository.setString(Constants.THEME, newTheme.name)
    }

    suspend fun updateVibrationSetting(
        newVibrationSetting: Boolean
    ) = withContext(scope.coroutineContext) {
        _isVibrating.value = newVibrationSetting
        repository.setBool(Constants.IS_VIBRATING, newVibrationSetting)
    }

    suspend fun updateStartingBlank(
        isStartingWithBlank: Boolean
    ) = withContext(scope.coroutineContext) {
        _isStartingWithBlankPage.value = isStartingWithBlank
        repository.setBool(Constants.IS_STARTING_BLANK, isStartingWithBlank)
    }
}