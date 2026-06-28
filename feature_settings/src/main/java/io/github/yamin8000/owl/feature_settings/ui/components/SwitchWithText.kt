/*
 *     freeDictionaryApp/freeDictionaryApp.feature_settings.main
 *     Components.kt Copyrighted by Yamin Siahmargooei at 2024/8/19
 *     Components.kt Last modified at 2024/8/18
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_settings.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_settings.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_settings.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_settings.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.theme.AppPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme

@AppPreview
@Composable
private fun Preview() {
    PreviewTheme {
        var checked by remember { mutableStateOf(true) }
        SwitchWithText(
            caption = "some settingssssssssssssssssssssssssssssssssssssssssssssssssssssssss",
            checked = checked,
            onCheckedChange = { checked = it }
        )
    }
}

@Composable
internal fun SwitchWithText(
    caption: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val hapticFeedback = LocalHapticFeedback.current
    val feedbackType = remember(checked) {
        if (checked) HapticFeedbackType.ToggleOff
        else HapticFeedbackType.ToggleOn
    }
    Box(
        modifier = modifier.clickable(
            role = Role.Switch,
            onClick = {
                hapticFeedback.performHapticFeedback(feedbackType)
                onCheckedChange(!checked)
            }
        ),
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                content = {
                    AppText(
                        text = caption,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(5f)
                    )
                    Box(
                        modifier = Modifier.weight(1f),
                        content = {
                            val icon = remember(checked) {
                                if (checked) Icons.Filled.Check else Icons.Filled.Close
                            }
                            Switch(
                                checked = checked,
                                onCheckedChange = onCheckedChange,
                                modifier = Modifier.align(Alignment.CenterEnd),
                                thumbContent = {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        modifier = Modifier.size(SwitchDefaults.IconSize),
                                    )
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}