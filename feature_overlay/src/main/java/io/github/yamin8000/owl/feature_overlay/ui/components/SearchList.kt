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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.search.domain.model.Meaning
import io.github.yamin8000.owl.search.ui.components.MeaningCard
import io.github.yamin8000.owl.search.ui.components.WordCard
import java.util.UUID
import kotlin.random.Random

@Composable
private fun Preview() {
    PreviewTheme {
        SearchList(
            isSearching = Random.nextBoolean(),
            word = "Word",
            phonetic = "Phonetic",
            meanings = Meaning.mockList()
        )
    }
}

@Composable
internal fun SearchList(
    isSearching: Boolean,
    word: String,
    phonetic: String,
    meanings: List<Meaning>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Sizes.Medium, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            if (isSearching) {
                item {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                item {
                    WordCard(
                        word = word,
                        pronunciation = phonetic
                    )
                }
                items(
                    items = meanings,
                    key = { item -> "meaning-${item.id ?: UUID.randomUUID()}" },
                    itemContent = { meaning ->
                        MeaningCard(
                            word = word,
                            meaning = meaning
                        )
                    }
                )
            }
        }
    )
}