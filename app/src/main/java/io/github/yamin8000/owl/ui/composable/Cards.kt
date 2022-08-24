/*
 *     Owl: an android app for Owlbot Dictionary API
 *     Cards.kt Created by Yamin Siahmargooei at 2022/8/24
 *     This file is part of Owl.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Owl is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Owl is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Owl.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.ui.composable

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.model.Definition
import io.github.yamin8000.owl.model.Word

class DefinitionProvider : PreviewParameterProvider<Definition> {
    override val values: Sequence<Definition> = listOf(
        Definition(
            "noun",
            "a word definition",
            "some word in a sentence",
            "",
            "\uD83D\uDE02"
        )
    ).asSequence()
}

class WordProvider : PreviewParameterProvider<Word> {
    @Suppress("SpellCheckingInspection")
    override val values: Sequence<Word> = listOf(
        Word("word", "wurd", listOf())
    ).asSequence()
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun RemovableCard(
    @PreviewParameter(TextProvider::class)
    item: String,
    onClick: () -> Unit = { },
    onLongClick: (() -> Unit)? = null
) {
    Card(
        shape = CutCornerShape(15.dp)
    ) {
        RippleText(
            modifier = Modifier.padding(8.dp),
            content = {
                Text(
                    text = item,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            },
            onClick = onClick,
            onLongClick = onLongClick
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun WordCard(
    @PreviewParameter(WordProvider::class)
    word: Word,
    onClick: () -> Unit = {},
    onAddToFavouriteClick: () -> Unit = {},
    onShareWordClick: () -> Unit = {}
) {
    OutlinedCard(
        shape = CutCornerShape(15.dp),
        modifier = Modifier
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = onClick
            )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TtsReadyComposable { ttsEngine ->
                    WordText(word.word, ttsEngine)
                }
                if (word.pronunciation != null)
                    PronunciationText(
                        word.pronunciation,
                        word.word
                    )
            }
            Row {
                ClickableIcon(
                    iconPainter = painterResource(id = R.drawable.ic_favorites),
                    contentDescription = stringResource(id = R.string.favourites)
                ) { onAddToFavouriteClick() }
                ClickableIcon(
                    iconPainter = painterResource(id = R.drawable.ic_share),
                    contentDescription = stringResource(R.string.share)
                ) { onShareWordClick() }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefinitionCard(
    @PreviewParameter(DefinitionProvider::class)
    definition: Definition
) {
    Card(
        shape = CutCornerShape(15.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TtsReadyComposable { ttsEngine ->
                    if (definition.type != null)
                        WordTypeText(definition.type, ttsEngine)
                    WordDefinitionText(definition.definition, ttsEngine)
                    if (definition.example != null)
                        WordExampleText(definition.example, ttsEngine)
                    if (definition.emoji != null)
                        WordEmojiText(definition.emoji, ttsEngine)
                }
            }
            if (!definition.imageUrl.isNullOrBlank())
                AsyncImage(
                    model = definition.imageUrl,
                    contentDescription = definition.definition,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )
        }
    }
}