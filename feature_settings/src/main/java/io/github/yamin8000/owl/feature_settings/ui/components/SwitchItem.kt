/*
 *     freeDictionaryApp/freeDictionaryApp.feature_settings.main
 *     SwitchItem.kt Copyrighted by Yamin Siahmargooei at 2025/2/7
 *     SwitchItem.kt Last modified at 2025/2/7
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_settings.main.
 *     Copyright (C) 2025  Yamin Siahmargooei
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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Category
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.yamin8000.owl.common.ui.theme.MyPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes

@MyPreview
@Composable
private fun Preview() {
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
    imageVector: ImageVector,
    caption: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Sizes.Small, Alignment.Start),
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