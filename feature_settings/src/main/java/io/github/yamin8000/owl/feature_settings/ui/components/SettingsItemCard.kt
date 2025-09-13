/*
 *     freeDictionaryApp/freeDictionaryApp.feature_settings.main
 *     SettingsItemCard.kt Copyrighted by Yamin Siahmargooei at 2025/2/7
 *     SettingsItemCard.kt Last modified at 2025/2/7
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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.common.ui.theme.MyPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes

@MyPreview
@Composable
private fun Preview() {
    PreviewTheme {
        SettingsItemCard(
            title = "Wifi",
            content = {
                Text(
                    text = "on/off"
                )
            }
        )
    }
}

@Composable
internal fun SettingsItemCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Sizes.Small, Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start,
        content = {
            AppText(
                text = title,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
            OutlinedCard(
                border = BorderStroke(Sizes.xxSmall, MaterialTheme.colorScheme.secondary),
                shape = DefaultCutShape,
                content = {
                    Column(
                        modifier = Modifier.padding(Sizes.Large),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            Sizes.Medium,
                            Alignment.CenterVertically
                        ),
                        content = content
                    )
                }
            )
        }
    )
}