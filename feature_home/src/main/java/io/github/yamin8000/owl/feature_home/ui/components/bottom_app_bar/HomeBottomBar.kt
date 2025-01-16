/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     HomeBottomBar.kt Copyrighted by Yamin Siahmargooei at 2025/1/16
 *     HomeBottomBar.kt Last modified at 2024/12/5
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

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.yamin8000.owl.common.ui.theme.MyPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme

@MyPreview
@Composable
private fun MainBottomBarPreview() {
    PreviewTheme {
        MainBottomBar(
            searchTerm = "test",
            suggestionsChips = {
                SuggestionsChips(
                    searchTerm = "test",
                    suggestions = emptyList(),
                    onSuggestionClick = {},
                )
            },
            isSearching = false,
            onSearchTermChange = {},
            onSearch = {},
            onCancel = {}
        )
    }
}

@Composable
internal fun MainBottomBar(
    modifier: Modifier = Modifier,
    searchTerm: String,
    suggestionsChips: @Composable ColumnScope.() -> Unit,
    isSearching: Boolean,
    onSearchTermChange: (String) -> Unit,
    onSearch: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = modifier,
        content = {
            suggestionsChips()
            if (isSearching) {
                RainbowLinearProgress()
            }
            AnimatedContent(
                targetState = isSearching,
                label = "",
                content = { target ->
                    if (!target) {
                        NormalBottomAppBar(
                            onSearch = onSearch,
                            onSearchTermChange = onSearchTermChange,
                            searchTerm = searchTerm
                        )
                    } else BottomAppBarDuringSearch(onCancel = onCancel)
                }
            )
        }
    )
}