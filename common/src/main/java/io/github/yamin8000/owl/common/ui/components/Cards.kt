/*
 *     freeDictionaryApp/freeDictionaryApp.common.main
 *     Cards.kt Copyrighted by Yamin Siahmargooei at 2024/8/18
 *     Cards.kt Last modified at 2024/8/17
 *     This file is part of freeDictionaryApp/freeDictionaryApp.common.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
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
import androidx.compose.ui.unit.dp
import io.github.yamin8000.owl.common.ui.theme.DefaultCutShape

@Composable
fun RemovableCard(
    modifier: Modifier = Modifier,
    item: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    OutlinedCard(
        modifier = modifier,
        shape = DefaultCutShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
        content = {
            var isMenuExpanded by remember { mutableStateOf(false) }
            val showMenu = remember { { isMenuExpanded = true } }
            val hideMenu = remember { { isMenuExpanded = false } }
            Ripple(
                onClick = onClick,
                onLongClick = showMenu,
                content = {
                    Text(
                        text = item,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    )
                }
            )
            val haptic = LocalHapticFeedback.current
            val onDelete = remember {
                {
                    isMenuExpanded = false
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onLongClick()
                }
            }
            DeleteMenu(
                expanded = isMenuExpanded,
                onDismiss = hideMenu,
                onDelete = onDelete
            )
        }
    )
}