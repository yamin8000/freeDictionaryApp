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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.yamin8000.owl.common.ui.components.EmptyList
import io.github.yamin8000.owl.common.ui.components.ScaffoldWithTitle
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
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
            }.toImmutableList()
        )
    }
}

@Composable
fun <T> CrudContent(
    title: String,
    items: ImmutableList<T>,
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
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        Sizes.Medium,
                        Alignment.CenterVertically
                    ),
                    content = {
                        item {
                            RemoveAllContent(
                                modifier = Modifier.fillMaxWidth(),
                                onRemoveAllClick = onRemoveAll
                            )
                        }
                        items(
                            items = items,
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

