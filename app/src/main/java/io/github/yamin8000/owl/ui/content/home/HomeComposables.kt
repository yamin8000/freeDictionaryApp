/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     HomeComposables.kt Copyrighted by Yamin Siahmargooei at 2023/8/26
 *     HomeComposables.kt Last modified at 2023/8/26
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

package io.github.yamin8000.owl.ui.content.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ShortText
import androidx.compose.material.icons.automirrored.twotone.TextSnippet
import androidx.compose.material.icons.twotone.Category
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material.icons.twotone.RecordVoiceOver
import androidx.compose.material.icons.twotone.Share
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.data.model.Meaning
import io.github.yamin8000.owl.ui.composable.ClickableIcon
import io.github.yamin8000.owl.ui.composable.CopyAbleRippleTextWithIcon
import io.github.yamin8000.owl.ui.composable.HighlightText
import io.github.yamin8000.owl.ui.composable.SpeakableRippleTextWithIcon
import io.github.yamin8000.owl.ui.composable.TtsAwareContent
import io.github.yamin8000.owl.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.ui.theme.defaultGradientBorder
import io.github.yamin8000.owl.util.speak

@Composable
internal fun WordDefinitionsList(
    word: String,
    localeTag: String,
    listState: ScrollState,
    meanings: List<Meaning>,
    onWordChipClick: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(listState)
            .padding(16.dp),
        content = {
            meanings.forEach { meaning ->
                MeaningCard(
                    word = word,
                    localeTag = localeTag,
                    meaning = meaning,
                    onWordChipClick = onWordChipClick
                )
            }
        }
    )
}

@Composable
internal fun WordCard(
    localeTag: String,
    word: String,
    pronunciation: String?,
    onAddToFavourite: () -> Unit,
    onShareWord: () -> Unit
) {
    OutlinedCard(
        shape = DefaultCutShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .indication(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
            ),
        content = {
            Row(
                content = {
                    Column(
                        modifier = Modifier
                            .weight(2f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.Start,
                        content = {
                            WordText(word, localeTag)
                            if (pronunciation != null) {
                                PronunciationText(
                                    localeTag = localeTag,
                                    pronunciation = pronunciation,
                                    word = word
                                )
                            }
                        }
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.End,
                        content = {
                            ClickableIcon(
                                imageVector = Icons.TwoTone.Favorite,
                                contentDescription = stringResource(R.string.favourites),
                                onClick = onAddToFavourite
                            )
                            ClickableIcon(
                                imageVector = Icons.TwoTone.Share,
                                contentDescription = stringResource(R.string.share),
                                onClick = onShareWord
                            )
                        }
                    )
                }
            )
        }
    )
}

@Composable
internal fun WordText(
    word: String,
    localeTag: String
) {
    SpeakableRippleTextWithIcon(
        text = word,
        imageVector = Icons.AutoMirrored.TwoTone.ShortText,
        localeTag = localeTag
    )
}

@Composable
internal fun PronunciationText(
    localeTag: String,
    pronunciation: String,
    word: String
) {
    TtsAwareContent(
        ttsLanguageLocaleTag = localeTag,
        content = { ttsEngine ->
            CopyAbleRippleTextWithIcon(
                text = pronunciation,
                imageVector = Icons.TwoTone.RecordVoiceOver,
                onClick = { ttsEngine.speak(word) }
            )
        }
    )
}

@Composable
internal fun WordExampleText(
    word: String,
    example: String,
    localeTag: String,
    onDoubleClick: ((String) -> Unit)? = null
) {
    SpeakableRippleTextWithIcon(
        content = { HighlightText(fullText = example, highlightedText = word) },
        text = example,
        title = stringResource(R.string.example),
        imageVector = Icons.AutoMirrored.TwoTone.TextSnippet,
        localeTag = localeTag,
        onDoubleClick = onDoubleClick
    )
}

@Composable
internal fun WordDefinitionText(
    word: String,
    definition: String,
    localeTag: String,
    onDoubleClick: ((String) -> Unit)? = null
) {
    SpeakableRippleTextWithIcon(
        content = { HighlightText(fullText = definition, highlightedText = word) },
        text = definition,
        title = stringResource(R.string.definition),
        imageVector = Icons.AutoMirrored.TwoTone.ShortText,
        localeTag = localeTag,
        onDoubleClick = onDoubleClick
    )
}

@Composable
internal fun WordTypeText(
    type: String,
    localeTag: String,
    onDoubleClick: ((String) -> Unit)? = null
) {
    SpeakableRippleTextWithIcon(
        text = type,
        title = stringResource(R.string.type),
        imageVector = Icons.TwoTone.Category,
        localeTag = localeTag,
        onDoubleClick = onDoubleClick
    )
}

@Composable
internal fun MeaningCard(
    word: String,
    localeTag: String,
    meaning: Meaning,
    onWordChipClick: (String) -> Unit
) {
    Card(
        shape = DefaultCutShape,
        modifier = Modifier.fillMaxWidth(),
        border = defaultGradientBorder(),
        content = {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                content = {
                    WordTypeText(
                        meaning.partOfSpeech,
                        localeTag,
                        onDoubleClick = onWordChipClick
                    )
                    meaning.definitions.forEach { definition ->
                        WordDefinitionText(
                            word,
                            definition.definition,
                            localeTag,
                            onDoubleClick = onWordChipClick
                        )
                        if (definition.example != null) {
                            WordExampleText(
                                word = word,
                                example = definition.example,
                                localeTag = localeTag,
                                onDoubleClick = onWordChipClick
                            )
                        }
                        definition.antonyms.forEach { antonym ->
                            SpeakableRippleTextWithIcon(
                                text = antonym,
                                title = stringResource(R.string.antonym),
                                imageVector = Icons.AutoMirrored.TwoTone.TextSnippet,
                                localeTag = localeTag
                            )
                        }
                        definition.synonyms.forEach { synonym ->
                            SpeakableRippleTextWithIcon(
                                text = synonym,
                                title = stringResource(R.string.synonym),
                                imageVector = Icons.AutoMirrored.TwoTone.TextSnippet,
                                localeTag = localeTag
                            )
                        }
                    }
                }
            )
        }
    )
}