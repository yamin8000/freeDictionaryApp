/*
 *     freeDictionaryApp/freeDictionaryApp.common.main
 *     LocaleUtils.kt Copyrighted by Yamin Siahmargooei at 2024/8/18
 *     LocaleUtils.kt Last modified at 2024/8/18
 *     This file is part of freeDictionaryApp/freeDictionaryApp.common.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.common.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.common.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.common.ui.util

import android.content.Context
import android.os.Build
import java.util.Locale

object LocaleUtils {
    /** Returns current [Locale] of the device */
    @Suppress("DEPRECATION")
    fun getCurrentLocale(
        context: Context
    ): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else context.resources.configuration.locale
    }
}