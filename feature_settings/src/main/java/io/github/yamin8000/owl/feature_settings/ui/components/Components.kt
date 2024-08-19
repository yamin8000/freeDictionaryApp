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

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowDropDownCircle
import androidx.compose.material.icons.twotone.Category
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.yamin8000.owl.common.ui.components.PersianText
import io.github.yamin8000.owl.common.ui.theme.MyPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme

@Composable
internal fun SettingsItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = LocalIndication.current,
            onClick = onClick,
        ),
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                        //Extra padding for increasing touch area
                        modifier = Modifier.padding(vertical = 8.dp),
                        content = content
                    )
                    Icon(
                        imageVector = Icons.TwoTone.ArrowDropDownCircle,
                        contentDescription = null
                    )
                }
            )
        }
    )
}

@MyPreview
@Composable
private fun SwitchItemPreview() {
    PreviewTheme {
        SwitchItem(
            imageVector = Icons.TwoTone.Category,
            caption = "Item on/off",
            checked = false,
            onCheckedChange = {}
        )
    }
}

@Composable
internal fun SwitchItem(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    caption: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        content = {
            Icon(
                imageVector = imageVector,
                contentDescription = caption,
                tint = MaterialTheme.colorScheme.secondary
            )
            SwitchWithText(
                caption = caption,
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    )
}

@Composable
internal fun SwitchWithText(
    modifier: Modifier = Modifier,
    caption: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current
    val onClick = remember(checked, onCheckedChange) {
        {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            onCheckedChange(!checked)
        }
    }
    Box(
        modifier = modifier.clickable(
            role = Role.Switch,
            onClick = onClick
        ),
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                content = {
                    PersianText(caption)
                    Switch(
                        checked = checked,
                        onCheckedChange = null
                    )
                }
            )
        }
    )
}