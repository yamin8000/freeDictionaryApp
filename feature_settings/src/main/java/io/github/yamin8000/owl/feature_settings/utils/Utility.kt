/*
 *     freeDictionaryApp/freeDictionaryApp.feature_settings.main
 *     Utility.kt Copyrighted by Yamin Siahmargooei at 2026/7/26
 *     Utility.kt Last modified at 2026/7/26
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_settings.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
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

package io.github.yamin8000.owl.feature_settings.utils

import android.content.Context
import io.github.yamin8000.owl.feature_settings.ui.SettingsTab
import io.github.yamin8000.owl.strings.R

object Utility {

    fun SettingsTab.resourceName(context: Context): String {
        return when (this) {
            SettingsTab.General -> context.getString(R.string.general)
            SettingsTab.Advanced -> context.getString(R.string.advanced)
        }
    }
}