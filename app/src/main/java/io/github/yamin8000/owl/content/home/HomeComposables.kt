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

package io.github.yamin8000.owl.content.home

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Category
import androidx.compose.material.icons.twotone.EmojiEmotions
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material.icons.twotone.RecordVoiceOver
import androidx.compose.material.icons.twotone.Share
import androidx.compose.material.icons.twotone.ShortText
import androidx.compose.material.icons.twotone.TextSnippet
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.model.Definition
import io.github.yamin8000.owl.ui.composable.ClickableIcon
import io.github.yamin8000.owl.ui.composable.CopyAbleRippleTextWithIcon
import io.github.yamin8000.owl.ui.composable.HighlightText
import io.github.yamin8000.owl.ui.composable.SpeakableRippleTextWithIcon
import io.github.yamin8000.owl.ui.composable.TtsAwareContent
import io.github.yamin8000.owl.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.ui.util.DynamicThemePrimaryColorsFromImage
import io.github.yamin8000.owl.ui.util.rememberDominantColorState
import io.github.yamin8000.owl.util.ImmutableHolder
import io.github.yamin8000.owl.util.speak

@Composable
internal fun WordDefinitionsList(
    word: String,
    localeTag: String,
    listState: ScrollState,
    searchResult: ImmutableHolder<List<Definition>>,
    onWordChipClick: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(listState)
            .padding(16.dp),
        content = {
            searchResult.item.forEach { definition ->
                DynamicColorDefinitionCard(
                    word,
                    localeTag,
                    definition,
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
        modifier = Modifier
            .indication(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
            ),
        content = {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    WordText(word, localeTag)
                    if (pronunciation != null) {
                        PronunciationText(
                            localeTag = localeTag,
                            pronunciation = pronunciation,
                            word = word
                        )
                    }
                    Row {
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
        title = stringResource(R.string.word),
        imageVector = Icons.TwoTone.ShortText,
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
                title = stringResource(R.string.pronunciation),
                imageVector = Icons.TwoTone.RecordVoiceOver,
                onClick = { ttsEngine.speak(word) }
            )
        }
    )
}

@Composable
internal fun WordEmojiText(
    emoji: String,
    localeTag: String
) {
    SpeakableRippleTextWithIcon(
        text = emoji,
        title = stringResource(R.string.emoji),
        imageVector = Icons.TwoTone.EmojiEmotions,
        localeTag
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
        imageVector = Icons.TwoTone.TextSnippet,
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
        imageVector = Icons.TwoTone.ShortText,
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
        Icons.TwoTone.Category,
        localeTag,
        onDoubleClick = onDoubleClick
    )
}

@Composable
internal fun DynamicColorDefinitionCard(
    word: String,
    localeTag: String,
    definition: Definition,
    onWordChipClick: (String) -> Unit
) {
    val dominantColorState = rememberDominantColorState()
    if (definition.imageUrl != null) {
        DynamicThemePrimaryColorsFromImage(dominantColorState) {
            LaunchedEffect(definition) {
                dominantColorState.updateColorsFromImageUrl(definition.imageUrl)
            }
            DefinitionCard(
                word = word,
                localeTag = localeTag,
                definition = definition,
                onWordChipClick = onWordChipClick,
                cardColors = CardDefaults.cardColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    } else {
        dominantColorState.reset()
        DefinitionCard(
            word = word,
            localeTag = localeTag,
            definition = definition,
            onWordChipClick = onWordChipClick
        )
    }
}

@Composable
internal fun DefinitionCard(
    word: String,
    localeTag: String,
    definition: Definition,
    cardColors: CardColors = CardDefaults.cardColors(),
    onWordChipClick: (String) -> Unit
) {
    var dialogVisibility by rememberSaveable { mutableStateOf(false) }

    ImageDialog(
        definition = definition,
        dialogVisibility = dialogVisibility,
        dialogVisibilityChange = { dialogVisibility = it }
    )

    Card(
        shape = DefaultCutShape,
        modifier = Modifier.fillMaxWidth(),
        colors = cardColors,
        content = {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                content = {
                    if (definition.type != null) {
                        WordTypeText(
                            definition.type,
                            localeTag,
                            onDoubleClick = onWordChipClick
                        )
                    }
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
                    if (definition.emoji != null)
                        WordEmojiText(definition.emoji, localeTag)
                }
            )
            DefinitionImage(
                enabled = !definition.imageUrl.isNullOrBlank(),
                url = definition.imageUrl,
                content = definition.definition,
                onClick = { dialogVisibility = true }
            )
        }
    )
}

@Composable
private fun ImageDialog(
    dialogVisibility: Boolean,
    dialogVisibilityChange: (Boolean) -> Unit,
    definition: Definition
) {
    if (dialogVisibility) {
        Dialog(
            onDismissRequest = { dialogVisibilityChange(false) },
            content = {
                Surface(
                    modifier = Modifier.wrapContentSize(),
                    shape = DefaultCutShape,
                    content = {
                        DefinitionImage(
                            enabled = !definition.imageUrl.isNullOrBlank(),
                            url = definition.imageUrl,
                            content = definition.definition
                        )
                    }
                )
            }
        )
    }
}

@Composable
internal fun DefinitionImage(
    enabled: Boolean,
    url: String?,
    content: String,
    onClick: (() -> Unit)? = null
) {
    if (enabled) {
        AsyncImage(
            model = url,
            contentDescription = content,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick?.invoke() }
        )
    }
}