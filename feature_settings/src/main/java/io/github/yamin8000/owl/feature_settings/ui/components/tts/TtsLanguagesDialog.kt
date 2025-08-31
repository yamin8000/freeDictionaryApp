/*
 *     freeDictionaryApp/freeDictionaryApp.feature_settings.main
 *     TtsLanguagesDialog.kt Copyrighted by Yamin Siahmargooei at 2025/2/7
 *     TtsLanguagesDialog.kt Last modified at 2025/2/7
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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Language
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.yamin8000.owl.common.ui.theme.MyPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import java.util.Locale

@MyPreview
@Composable
private fun Preview() {
    PreviewTheme {
        TtsLanguagesDialog(
            currentTtsTag = "en-us",
            languages = listOf(),
            onDismiss = {},
            onLanguageSelect = {}
        )
    }
}

@Composable
internal fun TtsLanguagesDialog(
    modifier: Modifier = Modifier,
    currentTtsTag: String,
    languages: List<Locale>,
    onLanguageSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        icon = { Icon(imageVector = Icons.TwoTone.Language, contentDescription = null) },
        confirmButton = {/*ignored*/ },
        text = {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    Sizes.Medium,
                    Alignment.CenterVertically
                ),
                content = {
                    items(languages) {
                        TtsLanguageItem(
                            localeTag = it.toLanguageTag(),
                            isSelected = it.toLanguageTag() == currentTtsTag,
                            onClick = onLanguageSelect
                        )
                    }
                }
            )
        }
    )
}