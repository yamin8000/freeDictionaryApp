/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     SuggestionsChips.kt Copyrighted by Yamin Siahmargooei at 2025/1/16
 *     SuggestionsChips.kt Last modified at 2024/12/5
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_home.main.
 *     Copyright (C) 2025  Yamin Siahmargooei
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

package io.github.yamin8000.owl.feature_home.ui.components.bottom_app_bar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.yamin8000.owl.common.ui.components.HighlightText
import io.github.yamin8000.owl.common.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.common.ui.theme.MyPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.common.ui.theme.defaultGradientBorder

@MyPreview
@Composable
private fun Preview() {
    PreviewTheme {
        SuggestionsChips(
            searchTerm = "ing",
            suggestions = listOf("eating", "drinking", "drink"),
            onSuggestionClick = {}
        )
    }
}

@Composable
internal fun SuggestionsChips(
    modifier: Modifier = Modifier,
    searchTerm: String,
    suggestions: List<String>,
    onSuggestionClick: (String) -> Unit,
) {
    if (suggestions.isNotEmpty()) {
        LazyRow(
            modifier = modifier,
            contentPadding = PaddingValues(Sizes.Small),
            horizontalArrangement = Arrangement.spacedBy(
                Sizes.Medium,
                Alignment.CenterHorizontally
            ),
            content = {
                items(
                    items = suggestions,
                    itemContent = {
                        ElevatedSuggestionChip(
                            shape = DefaultCutShape,
                            border = defaultGradientBorder(1000),
                            onClick = { onSuggestionClick(it) },
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