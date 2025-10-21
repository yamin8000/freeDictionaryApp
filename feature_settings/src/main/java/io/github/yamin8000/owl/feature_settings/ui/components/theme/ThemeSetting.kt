/*
 *     freeDictionaryApp/freeDictionaryApp.feature_settings.main
 *     ThemeSetting.kt Copyrighted by Yamin Siahmargooei at 2025/2/7
 *     ThemeSetting.kt Last modified at 2025/2/7
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

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.DisplaySettings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.theme.MyPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.datastore.domain.model.ThemeType
import io.github.yamin8000.owl.feature_settings.ui.components.SettingsItem
import io.github.yamin8000.owl.feature_settings.ui.components.SettingsItemCard
import io.github.yamin8000.owl.feature_settings.ui.utils.Utility.toStringResource
import io.github.yamin8000.owl.strings.R

@MyPreview
@Composable
private fun Preview() {
    PreviewTheme {
        ThemeSetting(
            theme = ThemeType.System,
            onThemeChanged = {}
        )
    }
}

@Composable
internal fun ThemeSetting(
    theme: ThemeType,
    onThemeChanged: (ThemeType) -> Unit,
    modifier: Modifier = Modifier
) {
    var isShowingDialog by remember { mutableStateOf(false) }

    SettingsItemCard(
        modifier = modifier,
        title = stringResource(R.string.theme),
        content = {
            if (isShowingDialog) {
                ThemeChangerDialog(
                    currentTheme = theme,
                    onCurrentThemeChange = onThemeChanged,
                    onDismiss = { isShowingDialog = false }
                )
            }
            SettingsItem(
                onClick = { isShowingDialog = true },
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            Sizes.Small,
                            Alignment.Start
                        ),
                        content = {
                            val context = LocalContext.current
                            Icon(
                                imageVector = Icons.TwoTone.DisplaySettings,
                                contentDescription = null,
                            )
                            AppText(
                                text = theme.toStringResource(context),
                                maxLines = 2
                            )
                        }
                    )
                }
            )
            if (theme == ThemeType.System && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                DynamicThemeNotice()
            }
        }
    )
}