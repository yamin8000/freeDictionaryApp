/*
 *     freeDictionaryApp/freeDictionaryApp.search.main
 *     SearchComponents.kt Copyrighted by Yamin Siahmargooei at 2024/9/5
 *     SearchComponents.kt Last modified at 2024/9/5
 *     This file is part of freeDictionaryApp/freeDictionaryApp.search.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ShortText
import androidx.compose.material.icons.automirrored.twotone.TextSnippet
import androidx.compose.material.icons.twotone.Category
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material.icons.twotone.RecordVoiceOver
import androidx.compose.material.icons.twotone.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.yamin8000.owl.common.ui.components.ClickableIcon
import io.github.yamin8000.owl.common.ui.components.HighlightText
import io.github.yamin8000.owl.common.ui.components.SpeakableRippleTextWithIcon
import io.github.yamin8000.owl.common.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.common.ui.theme.defaultGradientBorder
import io.github.yamin8000.owl.search.domain.model.Meaning
import io.github.yamin8000.owl.strings.R

@Composable
fun WordCard(
    modifier: Modifier = Modifier,
    word: String,
    pronunciation: String?,
    onAddToFavourite: (() -> Unit)? = null,
    onShareWord: (() -> Unit)? = null
) {
    OutlinedCard(
        shape = DefaultCutShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
        modifier = modifier
            .fillMaxWidth()
            .indication(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
            ),
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                content = {
                    Column(
                        modifier = Modifier
                            .weight(2f)
                            .padding(Sizes.Large),
                        verticalArrangement = Arrangement.spacedBy(Sizes.Small),
                        horizontalAlignment = Alignment.Start,
                        content = {
                            WordText(word)
                            if (!pronunciation.isNullOrBlank()) {
                                PronunciationText(
                                    word = word,
                                    pronunciation = pronunciation,
                                )
                            }
                        }
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(Sizes.Large),
                        verticalArrangement = Arrangement.spacedBy(Sizes.Medium),
                        horizontalAlignment = Alignment.End,
                        content = {
                            if (onAddToFavourite != null) {
                                ClickableIcon(
                                    imageVector = Icons.TwoTone.Favorite,
                                    contentDescription = stringResource(R.string.favourites),
                                    onClick = onAddToFavourite
                                )
                            }
                            if (onShareWord != null) {
                                ClickableIcon(
                                    imageVector = Icons.TwoTone.Share,
                                    contentDescription = stringResource(R.string.share),
                                    onClick = onShareWord
                                )
                            }
                        }
                    )
                }
            )
        }
    )
}

@Composable
internal fun WordText(
    word: String
) {
    SpeakableRippleTextWithIcon(
        text = word,
        imageVector = Icons.AutoMirrored.TwoTone.ShortText,
    )
}

@Composable
internal fun PronunciationText(
    pronunciation: String,
    word: String,
) {
    SpeakableRippleTextWithIcon(
        text = pronunciation,
        ttsText = word,
        imageVector = Icons.TwoTone.RecordVoiceOver,
    )
}

@Composable
fun MeaningCard(
    modifier: Modifier = Modifier,
    word: String,
    meaning: Meaning,
    onWordChipClick: ((String) -> Unit)? = null
) {
    OutlinedCard(
        shape = DefaultCutShape,
        modifier = modifier.fillMaxWidth(),
        border = defaultGradientBorder(),
        content = {
            Column(
                modifier = Modifier.padding(Sizes.Large),
                verticalArrangement = Arrangement.spacedBy(Sizes.Large),
                content = {
                    WordTypeText(
                        meaning.partOfSpeech,
                        onDoubleClick = onWordChipClick
                    )
                    meaning.definitions.forEach { (definition, example, synonyms, antonyms) ->
                        WordDefinitionText(
                            word = word,
                            definition = definition,
                            onDoubleClick = onWordChipClick
                        )
                        if (example != null) {
                            WordExampleText(
                                word = word,
                                example = example,
                                onDoubleClick = onWordChipClick
                            )
                        }
                        antonyms.forEach { antonym ->
                            SpeakableRippleTextWithIcon(
                                text = antonym,
                                imageVector = Icons.AutoMirrored.TwoTone.TextSnippet,
                                title = stringResource(R.string.antonym),
                            )
                        }
                        synonyms.forEach { synonym ->
                            SpeakableRippleTextWithIcon(
                                text = synonym,
                                imageVector = Icons.AutoMirrored.TwoTone.TextSnippet,
                                title = stringResource(R.string.synonym),
                            )
                        }
                    }
                }
            )
        }
    )
}

@Composable
internal fun WordExampleText(
    word: String,
    example: String,
    onDoubleClick: ((String) -> Unit)? = null
) {
    SpeakableRippleTextWithIcon(
        text = example,
        imageVector = Icons.AutoMirrored.TwoTone.TextSnippet,
        title = stringResource(R.string.example),
        content = { HighlightText(fullText = example, highlightedText = word) },
        onDoubleClick = onDoubleClick,
    )
}

@Composable
internal fun WordDefinitionText(
    word: String,
    definition: String,
    onDoubleClick: ((String) -> Unit)? = null
) {
    SpeakableRippleTextWithIcon(
        text = definition,
        imageVector = Icons.AutoMirrored.TwoTone.ShortText,
        title = stringResource(R.string.definition),
        content = { HighlightText(fullText = definition, highlightedText = word) },
        onDoubleClick = onDoubleClick,
    )
}

@Composable
internal fun WordTypeText(
    type: String,
    onDoubleClick: ((String) -> Unit)? = null
) {
    SpeakableRippleTextWithIcon(
        text = type,
        imageVector = Icons.TwoTone.Category,
        title = stringResource(R.string.type),
        onDoubleClick = onDoubleClick,
    )
}