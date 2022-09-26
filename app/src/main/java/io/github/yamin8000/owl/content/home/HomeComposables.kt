/*
 *     Owl: an android app for Owlbot Dictionary API
 *     HomeComposables.kt Created by Yamin Siahmargooei at 2022/9/20
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

package io.github.yamin8000.owl.content.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.model.Definition
import io.github.yamin8000.owl.model.Word
import io.github.yamin8000.owl.ui.util.DynamicThemePrimaryColorsFromImage
import io.github.yamin8000.owl.ui.composable.ClickableIcon
import io.github.yamin8000.owl.ui.composable.CopyAbleRippleTextWithIcon
import io.github.yamin8000.owl.ui.composable.SpeakableRippleTextWithIcon
import io.github.yamin8000.owl.ui.composable.TtsReadyComposable
import io.github.yamin8000.owl.ui.util.rememberDominantColorState
import io.github.yamin8000.owl.util.TtsEngine

@Composable
internal fun WordDefinitionsList(
    listState: LazyListState,
    searchResult: List<Definition>
) {
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            items(searchResult) { definition ->
                DynamicColorDefinitionCard(definition)
            }
        })
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun WordCard(
    word: Word,
    onClick: () -> Unit,
    onAddToFavourite: () -> Unit,
    onShareWord: () -> Unit
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
                if (word.pronunciation != null) {
                    PronunciationText(
                        word.pronunciation,
                        word.word
                    )
                }
            }
            Row {
                ClickableIcon(
                    iconPainter = painterResource(id = R.drawable.ic_favorites),
                    contentDescription = stringResource(id = R.string.favourites),
                    onClick = { onAddToFavourite() }
                )
                ClickableIcon(
                    iconPainter = painterResource(id = R.drawable.ic_share),
                    contentDescription = stringResource(R.string.share),
                    onClick = { onShareWord() }
                )
            }
        }
    }
}

@Composable
internal fun WordText(
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
internal fun PronunciationText(
    pronunciation: String,
    word: String
) {
    TtsReadyComposable { ttsEngine ->
        CopyAbleRippleTextWithIcon(
            text = pronunciation,
            iconPainter = painterResource(id = R.drawable.ic_person_voice)
        ) {
            ttsEngine.speak(word)
        }
    }
}

@Composable
internal fun WordEmojiText(
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
internal fun WordExampleText(
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
internal fun WordDefinitionText(
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
internal fun WordTypeText(
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
internal fun DynamicColorDefinitionCard(
    definition: Definition
) {
    val dominantColorState = rememberDominantColorState()
    if (definition.imageUrl != null) {
        DynamicThemePrimaryColorsFromImage(dominantColorState) {
            LaunchedEffect(definition) {
                dominantColorState.updateColorsFromImageUrl(definition.imageUrl)
            }
            DefinitionCard(
                definition = definition,
                cardColors = CardDefaults.cardColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    } else {
        dominantColorState.reset()
        DefinitionCard(definition)
    }
}

@Composable
internal fun DefinitionCard(
    definition: Definition,
    cardColors: CardColors = CardDefaults.cardColors()
) {
    Card(
        shape = CutCornerShape(15.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = cardColors
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
            if (!definition.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = definition.imageUrl,
                    contentDescription = definition.definition,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}