/*
 *     freeDictionaryApp/freeDictionaryApp.feature_settings.main
 *     SettingsState.kt Copyrighted by Yamin Siahmargooei at 2024/8/19
 *     SettingsState.kt Last modified at 2024/8/19
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

import io.github.yamin8000.owl.datastore.domain.model.ThemeType
import java.util.Locale

data class SettingsState(
    val theme: ThemeType = ThemeType.System,
    val ttsLang: String? = null,
    val isVibrating: Boolean = true,
    val isStartingBlank: Boolean = true,
    val englishLanguages: List<Locale> = emptyList()
)
