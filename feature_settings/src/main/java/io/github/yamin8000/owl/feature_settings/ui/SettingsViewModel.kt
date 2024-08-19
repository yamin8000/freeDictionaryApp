/*
 *     freeDictionaryApp/freeDictionaryApp.feature_settings.main
 *     SettingsViewModel.kt Copyrighted by Yamin Siahmargooei at 2024/8/19
 *     SettingsViewModel.kt Last modified at 2024/8/19
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_settings.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_settings.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_settings.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.yamin8000.owl.datastore.domain.usecase.settings.SettingUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val useCases: SettingUseCases
) : ViewModel() {
    private val scope = viewModelScope

    private var _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        scope.launch {
            _state.update {
                it.copy(
                    themeType = useCases.getTheme(),
                    ttsLang = useCases.getTTS(),
                    isVibrating = useCases.getVibration(),
                    isStartingBlank = useCases.getStartingBlank()
                )
            }
        }
    }

    fun updateTtsLang(
        newTtsLang: String
    ) {
        /*scope.launch {
            _ttsLang.value = newTtsLang
            settings.setString(Constants.TTS_LANG, newTtsLang)
        }*/
    }

    /*fun updateThemeSetting(
        newTheme: ThemeSetting
    ) {
        *//*scope.launch {
            _themeSetting.value = newTheme
            settings.setString(Constants.THEME, newTheme.name)
        }*//*
    }*/

    fun updateVibrationSetting(
        newVibrationSetting: Boolean
    ) {
        /*scope.launch {
            _isVibrating.value = newVibrationSetting
            settings.setBool(Constants.IS_VIBRATING, newVibrationSetting)
        }*/
    }

    fun updateStartingBlank(
        isStartingWithBlank: Boolean
    ) {
        /*scope.launch {
            _isStartingBlank.value = isStartingWithBlank
            settings.setBool(Constants.IS_STARTING_BLANK, isStartingWithBlank)
        }*/
    }
}