/*
 *     freeDictionaryApp/freeDictionaryApp.datastore.main
 *     SettingsRepository.kt Copyrighted by Yamin Siahmargooei at 2024/8/19
 *     SettingsRepository.kt Last modified at 2024/8/19
 *     This file is part of freeDictionaryApp/freeDictionaryApp.datastore.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.datastore.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.datastore.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.datastore.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.github.yamin8000.owl.datastore.domain.model.SettingsKeys
import io.github.yamin8000.owl.datastore.domain.model.ThemeType
import io.github.yamin8000.owl.datastore.domain.repository.BaseDatastoreRepository
import io.github.yamin8000.owl.datastore.domain.repository.SettingsRepository

class SettingsDatastoreRepository(
    private val datastore: DataStore<Preferences>
) : SettingsRepository, BaseDatastoreRepository by BasicDatastoreRepository(datastore) {

    override suspend fun getTheme(): ThemeType {
        return ThemeType.toType(getString(SettingsKeys.THEME))
    }

    override suspend fun setTheme(theme: ThemeType) {
        setString(SettingsKeys.THEME, theme.toString())
    }

    override suspend fun getTtsLang(): String? {
        return getString(SettingsKeys.TTS_LANG)
    }

    override suspend fun setTtsLang(ttsLang: String) {
        setString(SettingsKeys.TTS_LANG, ttsLang)
    }

    override suspend fun getIsVibrating(): Boolean {
        return getBool(SettingsKeys.IS_VIBRATING) != false
    }

    override suspend fun setIsVibrating(value: Boolean) {
        setBool(SettingsKeys.IS_VIBRATING, value)
    }

    override suspend fun getIsStartingBlank(): Boolean {
        return getBool(SettingsKeys.IS_STARTING_BLANK) != false
    }

    override suspend fun setIsStartingBlank(value: Boolean) {
        setBool(SettingsKeys.IS_STARTING_BLANK, value)
    }
}