/*
 *     freeDictionaryApp/freeDictionaryApp.feature_overlay.main
 *     SearchList.kt Copyrighted by Yamin Siahmargooei at 2025/2/7
 *     SearchList.kt Last modified at 2025/2/7
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_overlay.main.
 *     Copyright (C) 2025  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_overlay.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_overlay.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_overlay.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import io.github.yamin8000.owl.common.ui.theme.MyPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.search.domain.model.Meaning
import io.github.yamin8000.owl.search.ui.components.MeaningCard
import io.github.yamin8000.owl.search.ui.components.WordCard

@MyPreview
@Composable
private fun Preview() {
    PreviewTheme {
        SearchList(
            isSearching = true,
            word = LoremIpsum(1).values.first(),
            phonetic = LoremIpsum(1).values.first(),
            meanings = emptyList()
        )
    }
}

@Composable
internal fun SearchList(
    modifier: Modifier = Modifier,
    isSearching: Boolean,
    word: String,
    phonetic: String,
    meanings: List<Meaning>
) {
    LazyColumn(
        modifier = modifier.padding(Sizes.Large),
        verticalArrangement = Arrangement.spacedBy(Sizes.Medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            if (isSearching) {
                item {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            item {
                WordCard(
                    word = word,
                    pronunciation = phonetic
                )
            }
            items(meanings) { meaning ->
                MeaningCard(
                    word = word,
                    meaning = meaning
                )
            }
        }
    )
}