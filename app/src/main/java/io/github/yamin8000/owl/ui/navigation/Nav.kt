/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     Nav.kt Copyrighted by Yamin Siahmargooei at 2024/8/24
 *     Nav.kt Last modified at 2024/8/18
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

package io.github.yamin8000.owl.ui.navigation

/** Navigation singleton */
object Nav {
    sealed interface Route {
        data object Home : Route
        data object History : Route
        data object Settings : Route
        data object About : Route
        data object Favourites : Route

        operator fun invoke(): String {
            return this.toString()
        }
    }

    sealed interface Arguments {
        data object Search : Arguments

        operator fun invoke(): String {
            return this.toString()
        }
    }
}