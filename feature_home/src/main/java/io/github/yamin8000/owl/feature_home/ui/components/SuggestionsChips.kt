/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     SuggestionsChips.kt Copyrighted by Yamin Siahmargooei at 2024/12/5
 *     SuggestionsChips.kt Last modified at 2024/12/5
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_home.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.yamin8000.owl.common.ui.components.HighlightText
import io.github.yamin8000.owl.common.ui.theme.defaultGradientBorder

@Composable
internal fun SuggestionsChips(
    modifier: Modifier = Modifier,
    searchTerm: String,
    suggestions: List<String>,
    onSuggestionClick: (String) -> Unit,
) {
    if (suggestions.isNotEmpty()) {
        LazyRow(
            modifier = modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            content = {
                items(
                    items = suggestions,
                    itemContent = {
                        val onClick = remember { { onSuggestionClick(it) } }
                        ElevatedSuggestionChip(
                            border = defaultGradientBorder(),
                            onClick = onClick,
                            label = {
                                HighlightText(
                                    fullText = it,
                                    highlightedText = searchTerm
                                )
                            }
                        )
                    }
                )
            }
        )
    }
}