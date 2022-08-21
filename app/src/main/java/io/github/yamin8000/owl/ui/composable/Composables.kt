/*
 *     Owl: an android app for Owlbot Dictionary API
 *     Composables.kt Created by Yamin Siahmargooei at 2022/7/3
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

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.model.Definition
import io.github.yamin8000.owl.model.Word
import io.github.yamin8000.owl.util.TtsEngine

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
    override val values: Sequence<Word> = listOf(
        Word("word", "wurd", listOf())
    ).asSequence()
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
            if (!definition.imageUrl.isNullOrBlank())
                AsyncImage(
                    model = definition.imageUrl,
                    contentDescription = definition.definition,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )
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
        }
    }
}

@Composable
fun WordEmojiText(
    emoji: String,
    ttsEngine: TtsEngine
) {
    SpeakableRippleTextWithIcon(
        emoji,
        painterResource(id = R.drawable.ic_emoji_symbols),
        ttsEngine
    )
}

@Composable
fun WordExampleText(
    example: String,
    ttsEngine: TtsEngine
) {
    SpeakableRippleTextWithIcon(
        example,
        painterResource(id = R.drawable.ic_text_snippet),
        ttsEngine
    )
}

@Composable
fun WordDefinitionText(
    definition: String,
    ttsEngine: TtsEngine
) {
    SpeakableRippleTextWithIcon(
        definition,
        painterResource(id = R.drawable.ic_short_text),
        ttsEngine
    )
}

@Composable
private fun WordTypeText(
    type: String,
    ttsEngine: TtsEngine
) {
    SpeakableRippleTextWithIcon(
        type,
        painterResource(id = R.drawable.ic_category),
        ttsEngine
    )
}

@Composable
fun ClickableIcon(
    iconPainter: Painter,
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            painter = iconPainter,
            contentDescription = contentDescription
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WordCard(
    @PreviewParameter(WordProvider::class)
    word: Word
) {
    Card(
        shape = CutCornerShape(15.dp),
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
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
    }
}

@Composable
private fun WordText(
    word: String,
    ttsEngine: TtsEngine
) {
    SpeakableRippleTextWithIcon(
        word,
        painterResource(id = R.drawable.ic_form_text),
        ttsEngine
    )
}

@Composable
private fun PronunciationText(
    pronunciation: String,
    word: String
) {
    TtsReadyComposable { ttsEngine ->
        RippleTextWithIcon(
            text = pronunciation,
            iconPainter = painterResource(id = R.drawable.ic_person_voice)
        ) {
            ttsEngine.speak(word)
        }
    }
}

@Composable
fun TtsReadyComposable(
    content: @Composable (TtsEngine) -> Unit
) {
    content(TtsEngine(LocalContext.current))
}