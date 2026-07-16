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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Language
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.theme.AppPreview
import io.github.yamin8000.owl.common.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.strings.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.util.Locale

@AppPreview
@Composable
private fun Preview() {
    PreviewTheme {
        TtsLanguagesDialogContent(
            currentTtsTag = "",
            languages = Locale.getAvailableLocales().toList().toImmutableList(),
            onLanguageSelect = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TtsLanguagesDialog(
    currentTtsTag: String,
    languages: ImmutableList<Locale>,
    onLanguageSelect: (String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        content = {
            TtsLanguagesDialogContent(
                currentTtsTag = currentTtsTag,
                languages = languages,
                onLanguageSelect = onLanguageSelect
            )
        }
    )
}

@Composable
private fun TtsLanguagesDialogContent(
    currentTtsTag: String,
    languages: ImmutableList<Locale>,
    onLanguageSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = DefaultCutShape,
        content = {
            var filterValue by remember { mutableStateOf("") }
            var filtered by remember { mutableStateOf(languages) }

            LazyColumn(
                modifier = Modifier.padding(Sizes.Large),
                verticalArrangement = Arrangement.spacedBy(Sizes.Small, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    item(
                        content = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(
                                    Sizes.Small,
                                    Alignment.CenterHorizontally
                                ),
                                verticalAlignment = Alignment.CenterVertically,
                                content = {
                                    Icon(
                                        imageVector = Icons.TwoTone.Language,
                                        contentDescription = null
                                    )
                                    AppText(text = stringResource(R.string.tts_language))
                                }
                            )

                        }
                    )
                    item(
                        content = {
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                value = filterValue,
                                label = { AppText(text = "Filter") },
                                onValueChange = {
                                    filterValue = it
                                    filtered = languages.filter { locale ->
                                        locale.displayName.contains(other = it, ignoreCase = true)
                                    }.toImmutableList()
                                },
                            )
                        }
                    )
                    items(
                        items = filtered,
                        key = { it.toLanguageTag() },
                        itemContent = {
                            TtsLanguageItem(
                                localeTag = it.toLanguageTag(),
                                displayName = it.displayName,
                                isSelected = it.toLanguageTag() == currentTtsTag,
                                onClick = onLanguageSelect
                            )
                        }
                    )
                }
            )
        }
    )
}