/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     ExpandedTextDialog.kt Copyrighted by Yamin Siahmargooei at 2026/7/16
 *     ExpandedTextDialog.kt Last modified at 2026/7/16
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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import io.github.yamin8000.owl.common.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
private fun Preview() {
    PreviewTheme {
        ExpandedTextDialogContent(
            onWordClicked = {},
            words = persistentListOf(),
            text = ""
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExpandedTextDialog(
    onDismissRequest: () -> Unit,
    onWordClicked: (String) -> Unit,
    words: ImmutableList<String>,
    text: String,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        content = {
            ExpandedTextDialogContent(
                modifier = modifier,
                words = words,
                text = text,
                onWordClicked = {
                    onWordClicked(it)
                    onDismissRequest()
                }
            )
        }
    )
}

@Composable
private fun ExpandedTextDialogContent(
    onWordClicked: (String) -> Unit,
    words: ImmutableList<String>,
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = DefaultCutShape,
        content = {
            LazyVerticalGrid(
                modifier = Modifier.padding(Sizes.Large),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(
                    Sizes.Small,
                    Alignment.CenterVertically
                ),
                horizontalArrangement = Arrangement.spacedBy(
                    Sizes.Small,
                    Alignment.CenterHorizontally
                ),
                content = {
                    item(
                        span = { GridItemSpan(2) },
                        content = {
                            val selectionColors = TextSelectionColors(
                                handleColor = MaterialTheme.colorScheme.secondary,
                                backgroundColor = MaterialTheme.colorScheme.onSecondary
                            )
                            CompositionLocalProvider(
                                values = arrayOf(LocalTextSelectionColors provides selectionColors),
                                content = { SelectionContainer(content = { Text(text = text) }) }
                            )
                        }
                    )
                    items(
                        items = words,
                        itemContent = { item ->
                            ElevatedSuggestionChip(
                                shape = DefaultCutShape,
                                border = BorderStroke(
                                    Sizes.xxSmall,
                                    MaterialTheme.colorScheme.tertiary
                                ),
                                onClick = { onWordClicked(item) },
                                label = {
                                    Text(
                                        text = item,
                                        overflow = TextOverflow.Ellipsis,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        modifier = Modifier
                                            .padding(Sizes.Medium)
                                            .fillMaxSize()
                                            .basicMarquee()
                                    )
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}