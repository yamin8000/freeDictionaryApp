/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     MainBottomBar.kt Copyrighted by Yamin Siahmargooei at 2023/8/26
 *     MainBottomBar.kt Last modified at 2023/8/26
 *     This file is part of freeDictionaryApp/freeDictionaryApp.app.main.
 *     Copyright (C) 2023  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.content

import android.content.Context
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Clear
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material.icons.twotone.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.composable.ClickableIcon
import io.github.yamin8000.owl.ui.composable.HighlightText
import io.github.yamin8000.owl.ui.composable.PersianText
import io.github.yamin8000.owl.ui.theme.Samim
import io.github.yamin8000.owl.util.ImmutableHolder
import io.github.yamin8000.owl.util.getCurrentLocale
import kotlinx.coroutines.delay
import java.util.*

@Composable
internal fun MainBottomBar(
    searchTerm: String?,
    suggestions: ImmutableHolder<List<String>>,
    onSuggestionClick: (String) -> Unit,
    isSearching: Boolean,
    onSearchTermChanged: (String) -> Unit,
    onSearch: (String) -> Unit,
    onCancel: () -> Unit
) {
    var searchText by remember { mutableStateOf(searchTerm ?: "") }
    Column {
        if (suggestions.item.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                content = {
                    items(
                        items = suggestions.item,
                        itemContent = {
                            ElevatedSuggestionChip(
                                label = { HighlightText(it, searchText) },
                                onClick = {
                                    onSuggestionClick(it)
                                    searchText = it
                                }
                            )
                        }
                    )
                }
            )
        }
        if (isSearching)
            RainbowLinearProgress()
        Crossfade(
            targetState = isSearching,
            label = "",
            content = { target ->
                if (target) {
                    BottomAppBar(
                        actions = {},
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = onCancel,
                                content = {
                                    Icon(
                                        imageVector = Icons.TwoTone.Stop,
                                        contentDescription = stringResource(R.string.cancel)
                                    )
                                }
                            )
                        }
                    )
                } else {
                    BottomAppBar {
                        TextField(
                            singleLine = true,
                            shape = CutCornerShape(topEnd = 10.dp, topStart = 10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 0.dp, 16.dp, 0.dp),
                            label = {
                                PersianText(
                                    stringResource(R.string.search),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            placeholder = {
                                PersianText(
                                    text = stringResource(R.string.search_hint),
                                    modifier = Modifier.fillMaxWidth(),
                                    fontSize = 12.sp
                                )
                            },
                            leadingIcon = {
                                ClickableIcon(
                                    imageVector = Icons.TwoTone.Clear,
                                    contentDescription = stringResource(R.string.clear),
                                    onClick = { searchText = "" }
                                )
                            },
                            trailingIcon = {
                                ClickableIcon(
                                    imageVector = Icons.TwoTone.Search,
                                    contentDescription = stringResource(R.string.search),
                                    onClick = { onSearch(searchText) }
                                )
                            },
                            value = searchText,
                            onValueChange = {
                                searchText = it
                                onSearchTermChanged(searchText)
                            },
                            textStyle = getTextStyleBasedOnLocale(LocalContext.current),
                            keyboardActions = KeyboardActions(onSearch = { onSearch(searchText) }),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Search,
                                keyboardType = KeyboardType.Text,
                                capitalization = KeyboardCapitalization.Words
                            )
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun RainbowLinearProgress() {
    fun randomBeam(): Int = (16..255).random()
    val colors = buildList {
        repeat((5..10).random()) {
            add(androidx.compose.ui.graphics.Color(randomBeam(), randomBeam(), randomBeam()))
        }
    }
    var color by remember { mutableStateOf(colors.first()) }
    LaunchedEffect(Unit) {
        while (true) {
            color = colors.random()
            delay(250)
        }
    }
    LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth(),
        color = color
    )
}

private fun getTextStyleBasedOnLocale(
    context: Context
): TextStyle {
    return if (getCurrentLocale(context).language == Locale("fa").language) {
        TextStyle(
            fontFamily = Samim,
            textAlign = TextAlign.Right,
            textDirection = TextDirection.Rtl
        )
    } else TextStyle()
}