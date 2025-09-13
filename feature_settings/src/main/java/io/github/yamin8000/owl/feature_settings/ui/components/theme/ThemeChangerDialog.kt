/*
 *     freeDictionaryApp/freeDictionaryApp.feature_settings.main
 *     ThemeChangerDialog.kt Copyrighted by Yamin Siahmargooei at 2025/2/7
 *     ThemeChangerDialog.kt Last modified at 2025/2/7
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

package io.github.yamin8000.owl.feature_settings.ui.components.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.DisplaySettings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.theme.MyPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.datastore.domain.model.ThemeType
import io.github.yamin8000.owl.feature_settings.ui.utils.Utility.toStringResource
import io.github.yamin8000.owl.strings.R

@MyPreview
@Composable
private fun Preview() {
    PreviewTheme {
        ThemeChangerDialog(
            currentTheme = ThemeType.entries().random(),
            onCurrentThemeChange = {},
            onDismiss = {}
        )
    }
}

@Composable
internal fun ThemeChangerDialog(
    currentTheme: ThemeType,
    onCurrentThemeChange: (ThemeType) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val themes = remember { ThemeType.entries() }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { AppText(stringResource(R.string.theme)) },
        icon = { Icon(imageVector = Icons.TwoTone.DisplaySettings, contentDescription = null) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    Sizes.Medium,
                    Alignment.CenterVertically
                ),
                modifier = Modifier
                    .padding(Sizes.Large)
                    .selectableGroup()
                    .fillMaxWidth(),
                content = {
                    themes.forEach { theme ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(
                                Sizes.xSmall,
                                Alignment.Start
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = theme == currentTheme,
                                    role = Role.RadioButton,
                                    onClick = {
                                        onCurrentThemeChange(theme)
                                        onDismiss()
                                    }
                                ),
                            content = {
                                val context = LocalContext.current
                                RadioButton(
                                    modifier = Modifier.padding(start = Sizes.Medium),
                                    selected = theme == currentTheme,
                                    onClick = null
                                )
                                AppText(
                                    modifier = Modifier.padding(vertical = Sizes.Large),
                                    text = theme.toStringResource(context)
                                )
                            }
                        )
                    }
                }
            )
        }
    )
}