/*
 *     freeDictionaryApp/freeDictionaryApp.feature_overlay.main
 *     OverlayViewModelFactory.kt Copyrighted by Yamin Siahmargooei at 2024/9/5
 *     OverlayViewModelFactory.kt Last modified at 2024/9/5
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_overlay.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_overlay.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_overlay.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_overlay.di

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import io.github.yamin8000.owl.feature_overlay.ui.OverlayWindowViewModel

@AssistedFactory
interface OverlayViewModelFactory {
    fun create(
        @Assisted("intent") intentSearch: String?,
    ): OverlayWindowViewModel
}