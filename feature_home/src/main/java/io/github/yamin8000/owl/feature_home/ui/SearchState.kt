/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     SearchState.kt Copyrighted by Yamin Siahmargooei at 2024/8/17
 *     SearchState.kt Last modified at 2024/5/19
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

internal sealed interface SearchState {
    data object RequestSucceed : SearchState
    data class RequestFailed(val code: Int) : SearchState
    data class RequestFinished(val term: String) : SearchState
    data object Unknown : SearchState
    data object Cached : SearchState

    companion object {
        const val CANCEL = 997
        const val EMPTY = 998
        const val UNKNOWN = 999
    }
}