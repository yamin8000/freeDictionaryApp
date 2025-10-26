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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.fastRoundToInt
import io.github.yamin8000.owl.common.ui.components.EmptyList
import io.github.yamin8000.owl.common.ui.components.ScaffoldWithTitle
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import java.util.UUID
import kotlin.random.Random
import kotlin.random.nextInt

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewTheme {
        CrudContent(
            title = "Title",
            onBackClick = {},
            onRemoveAll = {},
            onRemoveSingle = { _ -> },
            onItemClick = {},
            items = buildList {
                repeat(Random.nextInt(0..100)) {
                    add("Item")
                }
            }
        )
    }
}

@Composable
fun <T> CrudContent(
    title: String,
    items: List<T>,
    onBackClick: () -> Unit,
    onRemoveAll: () -> Unit,
    onRemoveSingle: (T) -> Unit,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    itemDisplayProvider: (T) -> String = { it.toString() },
    emptyContent: (@Composable () -> Unit) = {
        EmptyList(
            modifier = Modifier.fillMaxWidth()
        )
    }
) {
    ScaffoldWithTitle(
        modifier = modifier,
        title = title,
        onBackClick = onBackClick,
        content = {
            val isNotEmpty by remember(items.size) { mutableStateOf(items.isNotEmpty()) }
            if (isNotEmpty) {
                val window = LocalWindowInfo.current
                val density = LocalDensity.current
                val windowWidth = remember(window, density) {
                    window.containerSize.width
                }

                val textMeasurer = rememberTextMeasurer()
                val columns = remember(items, window, density) {
                    when (items.size) {
                        0, 1 -> 1
                        else -> {
                            val maxItem = items.map(itemDisplayProvider).maxBy { it.length }
                            val maxItemWidth =
                                textMeasurer.measure(maxItem).size.width * 2 * density.fontScale
                            (windowWidth / maxItemWidth).fastRoundToInt()
                        }
                    }
                }

                LazyVerticalStaggeredGrid(
                    contentPadding = PaddingValues(bottom = Sizes.Medium),
                    verticalItemSpacing = Sizes.Medium,
                    horizontalArrangement = Arrangement.spacedBy(
                        Sizes.Medium,
                        Alignment.CenterHorizontally
                    ),
                    columns = StaggeredGridCells.Fixed(columns),
                    content = {
                        item(
                            span = StaggeredGridItemSpan.FullLine,
                            content = { RemoveAllContent(onRemoveAllClick = onRemoveAll) }
                        )
                        items(
                            span = { StaggeredGridItemSpan.SingleLane },
                            items = items,
                            key = { UUID.randomUUID() },
                            itemContent = { item ->
                                CrudItem(
                                    item = itemDisplayProvider(item),
                                    onClick = { onItemClick(item) },
                                    onLongClick = { onRemoveSingle(item) }
                                )
                            }
                        )
                    }
                )
            } else emptyContent()
        }
    )
}

