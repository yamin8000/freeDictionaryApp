/*
 *     freeDictionaryApp/freeDictionaryApp.feature_floating.main
 *     OverlayWindowState.kt Copyrighted by Yamin Siahmargooei at 2024/9/5
 *     OverlayWindowState.kt Last modified at 2024/9/5
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_floating.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_floating.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_floating.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_overlay.ui

import androidx.compose.runtime.Immutable
import io.github.yamin8000.owl.search.domain.model.Meaning
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class OverlayWindowState(
    val isSearching: Boolean = false,
    val searchTerm: String = "",
    val meanings: ImmutableList<Meaning> = persistentListOf(),
    val word: String = "",
    val phonetic: String = ""
)
