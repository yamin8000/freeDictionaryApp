/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     EntryItem.kt Copyrighted by Yamin Siahmargooei at 2026/7/13
 *     EntryItem.kt Last modified at 2026/7/13
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_home.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.yamin8000.owl.common.ui.theme.AppPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.search.domain.model.License
import io.github.yamin8000.owl.search.domain.model.Meaning
import io.github.yamin8000.owl.search.domain.model.Phonetic
import io.github.yamin8000.owl.search.ui.components.MeaningCard
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@AppPreview
@Composable
private fun Preview() {
    PreviewTheme {
        EntryItem(
            word = "free",
            license = License.mock(),
            phonetics = Phonetic.mockList().toImmutableList(),
            meanings = Meaning.mockList().toImmutableList(),
            onPlayAudio = {},
            onWordChipClick = {}
        )
    }
}

@Composable
internal fun EntryItem(
    word: String,
    license: License?,
    phonetics: ImmutableList<Phonetic>,
    meanings: ImmutableList<Meaning>,
    onWordChipClick: (String) -> Unit,
    onPlayAudio: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Sizes.Small, Alignment.CenterVertically),
        content = {
            if (license != null) {
                LicenseCard(
                    name = license.name,
                    url = license.url
                )
            }
            if (phonetics.isNotEmpty()) {
                PhoneticList(
                    phonetics = phonetics,
                    onPlayAudio = onPlayAudio
                )
            }
            meanings.forEach { meaning ->
                MeaningCard(
                    word = word,
                    meaning = meaning,
                    onWordChipClick = onWordChipClick
                )
            }
        }
    )
}