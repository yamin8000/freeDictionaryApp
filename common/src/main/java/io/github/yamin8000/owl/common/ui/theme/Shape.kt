/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     Shape.kt Copyrighted by Yamin Siahmargooei at 2024/5/9
 *     Shape.kt Last modified at 2024/3/23
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

package io.github.yamin8000.owl.common.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

val DefaultCutShape = CutCornerShape(8.dp)

@Composable
fun defaultGradientBorder(): BorderStroke {
    return BorderStroke(
        1.dp,
        Brush.verticalGradient(
            listOf(
                MaterialTheme.colorScheme.tertiary,
                MaterialTheme.colorScheme.primary
            )
        )
    )
}