/*
 *     freeDictionaryApp/freeDictionaryApp.feature_settings.main
 *     TtsLanguageSetting.kt Copyrighted by Yamin Siahmargooei at 2025/2/7
 *     TtsLanguageSetting.kt Last modified at 2025/2/7
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Language
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.theme.MyPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.feature_settings.ui.components.SettingsItem
import io.github.yamin8000.owl.feature_settings.ui.components.SettingsItemCard
import io.github.yamin8000.owl.strings.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.Locale

@MyPreview
@Composable
private fun Preview() {
    PreviewTheme {
        TtsLanguageSetting(
            currentTtsTag = "en-us",
            languages = persistentListOf(),
            onTtsTagChange = {}
        )
    }
}

@Composable
internal fun TtsLanguageSetting(
    currentTtsTag: String,
    languages: ImmutableList<Locale>,
    onTtsTagChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingsItemCard(
        modifier = modifier,
        title = stringResource(R.string.tts_language),
        content = {
            var isDialogShown by remember { mutableStateOf(false) }
            SettingsItem(
                onClick = { isDialogShown = true },
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            Sizes.Small,
                            Alignment.Start
                        ),
                        content = {
                            Icon(
                                imageVector = Icons.TwoTone.Language,
                                contentDescription = null
                            )
                            AppText(
                                text = Locale.forLanguageTag(currentTtsTag).displayName,
                                maxLines = 2
                            )
                        }
                    )

                    if (isDialogShown) {
                        TtsLanguagesDialog(
                            currentTtsTag = currentTtsTag,
                            languages = languages,
                            onDismiss = { isDialogShown = false },
                            onLanguageSelect = {
                                onTtsTagChange(it)
                                isDialogShown = false
                            }
                        )
                    }
                }
            )
        }
    )
}