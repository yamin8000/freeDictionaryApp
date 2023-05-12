/*
 *     Owl: an android app for Owlbot Dictionary API
 *     Cards.kt Created by Yamin Siahmargooei at 2022/8/24
 *     This file is part of Owl.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Owl is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Owl is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Owl.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.yamin8000.owl.ui.theme.DefaultCutShape

@Composable
fun SettingsItemCard(
    modifier: Modifier = Modifier,
    columnModifier: Modifier = Modifier,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start,
        content = {
            PersianText(
                text = title,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Card(
                modifier = modifier,
                shape = DefaultCutShape,
                content = {
                    Column(
                        modifier = columnModifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = { content() }
                    )
                }
            )
        },
    )
}

@Composable
fun RemovableCard(
    item: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    Card(
        shape = DefaultCutShape,
        content = {
            Ripple(
                modifier = Modifier.padding(8.dp),
                onClick = onClick,
                onLongClick = { isMenuExpanded = true },
                content = {
                    Text(
                        text = item,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    )
                }
            )
            DeleteMenu(
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