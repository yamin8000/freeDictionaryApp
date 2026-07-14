/*
 *     freeDictionaryApp/freeDictionaryApp.search.main
 *     SearchList.kt Copyrighted by Yamin Siahmargooei at 2026/7/14
 *     SearchList.kt Last modified at 2026/7/13
 *     This file is part of freeDictionaryApp/freeDictionaryApp.search.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.search.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.search.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.search.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.theme.AppPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.search.domain.model.Entry
import io.github.yamin8000.owl.strings.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.random.Random

@AppPreview
@Composable
private fun Preview() {
    PreviewTheme {
        SearchList(
            isOnline = Random.nextBoolean(),
            word = LoremIpsum(1).values.first(),
            onAddToFavourite = {},
            onShareWord = {},
            onWordChipClick = {},
            onTextToSpeech = {},
            onPlayAudio = {},
            entries = Entry.mockList().toImmutableList(),
            listState = rememberLazyListState()
        )
    }
}

@Composable
fun SearchList(
    isOnline: Boolean,
    word: String,
    onAddToFavourite: (() -> Unit)?,
    onShareWord: (() -> Unit)?,
    onWordChipClick: (String) -> Unit,
    onTextToSpeech: (String) -> Unit,
    onPlayAudio: (String) -> Unit,
    entries: ImmutableList<Entry>,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Sizes.Small, Alignment.CenterVertically),
        contentPadding = PaddingValues(Sizes.Medium),
        content = {
            item(
                key = isOnline,
                content = {
                    AnimatedVisibility(
                        visible = !isOnline,
                        enter = slideInVertically() + fadeIn(),
                        exit = slideOutVertically() + fadeOut(),
                        content = {
                            AppText(
                                modifier = Modifier.padding(Sizes.Small),
                                color = MaterialTheme.colorScheme.error,
                                text = stringResource(R.string.general_net_error)
                            )
                        }
                    )
                }
            )

            if (word.isNotBlank()) {
                item(
                    key = word,
                    content = {
                        WordCard(
                            word = word,
                            onShareWord = onShareWord,
                            onAddToFavourite = onAddToFavourite,
                            onWordTextToSpeech = onTextToSpeech
                        )
                    }
                )
            }

            items(
                items = entries,
                itemContent = { entry ->
                    EntryItem(
                        word = word,
                        license = entry.license,
                        phonetics = entry.phonetics,
                        meanings = entry.meanings,
                        onWordChipClick = onWordChipClick,
                        onPlayAudio = onPlayAudio,
                    )
                }
            )
        }
    )
}