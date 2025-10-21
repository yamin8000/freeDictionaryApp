/*
 *     freeDictionaryApp/freeDictionaryApp.common.main
 *     RemovableCard.kt Copyrighted by Yamin Siahmargooei at 2025/2/7
 *     RemovableCard.kt Last modified at 2024/9/5
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

package io.github.yamin8000.owl.common.ui.components.crud

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import io.github.yamin8000.owl.common.ui.components.DeleteMenu
import io.github.yamin8000.owl.common.ui.components.Ripple
import io.github.yamin8000.owl.common.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.common.ui.theme.Sizes

@Composable
fun RemovableCard(
    item: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier,
        shape = DefaultCutShape,
        border = BorderStroke(Sizes.xxSmall, MaterialTheme.colorScheme.tertiary),
        content = {
            var isMenuExpanded by remember { mutableStateOf(false) }
            Ripple(
                onClick = onClick,
                onLongClick = { isMenuExpanded = true },
                content = {
                    Text(
                        text = item,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(vertical = Sizes.Medium)
                            .padding(horizontal = Sizes.Small)
                            .fillMaxWidth()
                    )
                }
            )
            val haptic = LocalHapticFeedback.current
            DeleteMenu(
                item = item,
                expanded = isMenuExpanded,
                onDismiss = { isMenuExpanded = false },
                onDelete = {
                    isMenuExpanded = false
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onLongClick()
                }
            )
        }
    )
}