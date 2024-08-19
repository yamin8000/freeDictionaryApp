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

package io.github.yamin8000.owl.datastore.domain.repository

import io.github.yamin8000.owl.datastore.domain.model.ThemeType

interface SettingsRepository : BaseDatastoreRepository {
    suspend fun getTheme(): ThemeType
    suspend fun setTheme(theme: ThemeType)
    suspend fun getTtsLang(): String?
    suspend fun setTtsLang(ttsLang: String)
    suspend fun getIsVibrating(): Boolean
    suspend fun setIsVibrating(value: Boolean)
    suspend fun getIsStartingBlank(): Boolean
    suspend fun setIsStartingBlank(value: Boolean)
}