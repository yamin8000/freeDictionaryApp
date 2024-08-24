/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     HomeEvent.kt Copyrighted by Yamin Siahmargooei at 2024/8/18
 *     HomeEvent.kt Last modified at 2024/8/18
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_home.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_home.ui

sealed interface HomeEvent {
    data object RandomWord : HomeEvent
    data class NewSearch(val searchTerm: String? = null) : HomeEvent
    data class OnTermChanged(val term: String) : HomeEvent
    data object OnShareData : HomeEvent
    data object CancelSearch : HomeEvent
    data object OnCheckInternet : HomeEvent
    data class OnAddToFavourite(val word: String) : HomeEvent
}