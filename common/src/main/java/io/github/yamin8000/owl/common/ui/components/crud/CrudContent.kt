/*
 *     freeDictionaryApp/freeDictionaryApp.common.main
 *     CrudContent.kt Copyrighted by Yamin Siahmargooei at 2025/2/7
 *     CrudContent.kt Last modified at 2025/2/7
 *     This file is part of freeDictionaryApp/freeDictionaryApp.common.main.
 *     Copyright (C) 2025  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.common.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.common.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.common.ui.components.crud

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import io.github.yamin8000.owl.common.ui.components.EmptyList
import io.github.yamin8000.owl.common.ui.components.ScaffoldWithTitle
import io.github.yamin8000.owl.common.ui.theme.Sizes

@Composable
fun <T> CrudContent(
    title: String,
    items: List<T>,
    onBackClick: () -> Unit,
    onRemoveAll: () -> Unit,
    onRemoveSingle: (T) -> Unit,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    emptyContent: (@Composable () -> Unit) = { EmptyList() },
    itemDisplayProvider: (T) -> String = { it.toString() }
) {
    ScaffoldWithTitle(
        modifier = modifier,
        title = title,
        onBackClick = onBackClick,
        content = {
            val isNotEmpty by remember(items) { mutableStateOf(items.isNotEmpty()) }
            if (isNotEmpty) {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier.padding(bottom = Sizes.Large),
                    horizontalArrangement = Arrangement.spacedBy(Sizes.Medium),
                    verticalItemSpacing = Sizes.Medium,
                    columns = StaggeredGridCells.Fixed(2),
                    content = {
                        item(
                            span = StaggeredGridItemSpan.FullLine,
                            content = { RemoveAllContent(onRemoveAllClick = remember { onRemoveAll }) }
                        )
                        items(
                            span = { StaggeredGridItemSpan.SingleLane },
                            count = items.size,
                            key = { index -> items[index].hashCode() },
                            itemContent = { index ->
                                val item by remember(items, index) {
                                    mutableStateOf(items[index])
                                }
                                val onClick = remember { { onItemClick(item) } }
                                val onLongClick = remember { { onRemoveSingle(item) } }
                                CrudItem(
                                    item = itemDisplayProvider(item),
                                    onClick = onClick,
                                    onLongClick = onLongClick
                                )
                            }
                        )
                    }
                )
            } else emptyContent()
        }
    )
}

