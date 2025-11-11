/*
 *     freeDictionaryApp/freeDictionaryApp.common.main
 *     ClickableIcon.kt Copyrighted by Yamin Siahmargooei at 2025/2/7
 *     ClickableIcon.kt Last modified at 2025/2/7
 *     This file is part of freeDictionaryApp/freeDictionaryApp.common.main.
 *     Copyright (C) 2025  Yamin Siahmargooei
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

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

@Composable
fun ClickableIcon(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val haptic = LocalHapticFeedback.current
    IconButton(
        modifier = modifier,
        enabled = enabled,
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
            onClick()
        },
        content = {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
            )
        }
    )
}