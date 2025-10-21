/*
 *     freeDictionaryApp/freeDictionaryApp.feature_settings.main
 *     TtsLanguageItem.kt Copyrighted by Yamin Siahmargooei at 2025/2/7
 *     TtsLanguageItem.kt Last modified at 2025/2/7
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

package io.github.yamin8000.owl.feature_settings.ui.components.tts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.common.ui.theme.MyPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import java.util.Locale

@MyPreview
@Composable
private fun Preview() {
    PreviewTheme {
        TtsLanguageItem(
            localeTag = "en-us",
            isSelected = false,
            onClick = {}
        )
    }
}

@Composable
internal fun TtsLanguageItem(
    localeTag: String,
    isSelected: Boolean,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = DefaultCutShape,
        onClick = { onClick(localeTag) },
        enabled = !isSelected,
        content = {
            AppText(
                modifier = Modifier.padding(Sizes.Large),
                text = Locale.forLanguageTag(localeTag).displayName,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}