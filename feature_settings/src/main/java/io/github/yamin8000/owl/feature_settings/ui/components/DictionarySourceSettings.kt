/*
 *     freeDictionaryApp/freeDictionaryApp.feature_settings.main
 *     DictionarySourceSettings.kt Copyrighted by Yamin Siahmargooei at 2026/6/24
 *     DictionarySourceSettings.kt Last modified at 2026/6/24
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_settings.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import io.github.yamin8000.owl.common.domain.model.DictionarySource
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.theme.AppPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.strings.R

@AppPreview
@Composable
private fun Preview() {
    PreviewTheme {
        DictionarySourceSettings(
            dictionarySource = DictionarySource.entries.toTypedArray().random(),
            onDictionarySourceChanged = {}
        )
    }
}

@Composable
internal fun DictionarySourceSettings(
    dictionarySource: DictionarySource,
    onDictionarySourceChanged: (DictionarySource) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Sizes.Medium, Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start,
        content = {
            AppText(
                text = stringResource(R.string.dictionary_source),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth(),
                space = Sizes.Large,
                content = {
                    SegmentedButton(
                        selected = dictionarySource == DictionarySource.FreeDictionary,
                        onClick = { onDictionarySourceChanged(DictionarySource.FreeDictionary) },
                        shape = CircleShape,
                        label = {
                            AppText(
                                text = DictionarySource.FreeDictionary.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                    SegmentedButton(
                        selected = dictionarySource == DictionarySource.Wiktionary,
                        onClick = { onDictionarySourceChanged(DictionarySource.Wiktionary) },
                        shape = CircleShape,
                        label = {
                            AppText(
                                text = DictionarySource.Wiktionary.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            )
            AppText(
                text = stringResource(R.string.dictionary_source_note),
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Justify
            )
        }
    )
}