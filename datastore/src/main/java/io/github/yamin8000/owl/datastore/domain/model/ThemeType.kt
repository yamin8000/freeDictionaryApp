/*
 *     freeDictionaryApp/freeDictionaryApp.datastore.main
 *     ThemeType.kt Copyrighted by Yamin Siahmargooei at 2024/8/19
 *     ThemeType.kt Last modified at 2024/8/19
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

package io.github.yamin8000.owl.datastore.domain.model

sealed interface ThemeType {
    data object Dark : ThemeType
    data object Light : ThemeType
    data object System : ThemeType
    data object Darker : ThemeType

    companion object {
        fun toType(value: String?): ThemeType {
            return when (value) {
                "Dark" -> Dark
                "Light" -> Light
                "System" -> System
                "Darker" -> Darker
                else -> System
            }
        }

        fun entries() = listOf(Dark, Light, System, Darker)
    }
}