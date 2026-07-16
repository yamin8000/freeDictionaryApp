/*
 *     freeDictionaryApp/freeDictionaryApp.common.main
 *     AppCard.kt Copyrighted by Yamin Siahmargooei at 2026/6/26
 *     AppCard.kt Last modified at 2026/6/26
 *     This file is part of freeDictionaryApp/freeDictionaryApp.common.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
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

package io.github.yamin8000.owl.common.ui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import io.github.yamin8000.owl.common.ui.theme.DefaultCutShape

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    shape: Shape = DefaultCutShape,
    content: @Composable BoxScope.() -> Unit,
) {
    BoxWithMovingGradientBorder(
        modifier = modifier,
        shape = shape,
        content = {
            Surface(
                shape = shape,
                content = { content() }
            )
        }
    )
}