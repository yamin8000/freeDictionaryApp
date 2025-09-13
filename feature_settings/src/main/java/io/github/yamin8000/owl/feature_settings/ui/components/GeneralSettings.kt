/*
 *     freeDictionaryApp/freeDictionaryApp.feature_settings.main
 *     GeneralSettings.kt Copyrighted by Yamin Siahmargooei at 2025/2/7
 *     GeneralSettings.kt Last modified at 2025/2/7
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

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Language
import androidx.compose.material.icons.twotone.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.yamin8000.owl.common.ui.theme.MyPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.strings.R

@MyPreview
@Composable
private fun Preview() {
    PreviewTheme {
        GeneralSettings(
            isVibrating = true,
            onVibratingChange = {},
            isStartingBlank = true,
            onStartingBlankChange = {}
        )
    }
}

@Composable
internal fun GeneralSettings(
    isVibrating: Boolean,
    onVibratingChange: (Boolean) -> Unit,
    isStartingBlank: Boolean,
    onStartingBlankChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingsItemCard(
        modifier = modifier.fillMaxWidth(),
        title = stringResource(R.string.general),
        content = {
            SwitchItem(
                imageVector = Icons.TwoTone.Language,
                caption = stringResource(R.string.vibrate_on_scroll),
                checked = isVibrating,
                onCheckedChange = onVibratingChange
            )
            SwitchItem(
                imageVector = Icons.TwoTone.Search,
                caption = stringResource(R.string.start_blank_search),
                checked = isStartingBlank,
                onCheckedChange = onStartingBlankChange
            )
        }
    )
}