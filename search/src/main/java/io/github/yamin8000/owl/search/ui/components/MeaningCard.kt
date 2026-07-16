package io.github.yamin8000.owl.search.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ShortText
import androidx.compose.material.icons.automirrored.twotone.TextSnippet
import androidx.compose.material.icons.twotone.Category
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import io.github.yamin8000.owl.common.ui.components.AppCard
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.components.HighlightText
import io.github.yamin8000.owl.common.ui.components.TextWithIcon
import io.github.yamin8000.owl.common.ui.theme.AppPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.search.domain.model.Meaning
import io.github.yamin8000.owl.strings.R

@AppPreview
@Composable
private fun Preview() {
    PreviewTheme {
        Column(
            modifier = Modifier.padding(Sizes.Large),
            content = {
                MeaningCard(
                    word = LoremIpsum(1).values.first(),
                    meaning = Meaning.mock(),
                    onExpandText = {},
                    onTextToSpeech = {}
                )
            }
        )
    }
}

@Composable
fun MeaningCard(
    word: String,
    meaning: Meaning,
    onExpandText: (String) -> Unit,
    onTextToSpeech: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(
        modifier = modifier.fillMaxWidth(),
        content = {
            Column(
                modifier = Modifier.padding(Sizes.Medium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    Sizes.Medium,
                    Alignment.CenterVertically
                ),
                content = {
                    if (meaning.language != null) {
                        AppText(
                            text = meaning.language,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    TextWithIcon(
                        modifier = Modifier.fillMaxWidth(),
                        text = meaning.partOfSpeech,
                        imageVector = Icons.TwoTone.Category,
                        title = stringResource(R.string.type),
                        onClick = { onTextToSpeech(meaning.partOfSpeech) }
                    )
                    meaning.definitions.forEachIndexed { index, (definition, examples, synonyms, antonyms) ->
                        TextWithIcon(
                            modifier = Modifier.fillMaxWidth(),
                            text = definition,
                            imageVector = Icons.AutoMirrored.TwoTone.ShortText,
                            title = stringResource(R.string.definition),
                            onClick = { onTextToSpeech(definition) },
                            onDoubleClick = { onExpandText(definition) },
                            content = {
                                HighlightText(
                                    fullText = definition,
                                    highlightedText = word
                                )
                            }
                        )

                        examples.forEach { example ->
                            TextWithIcon(
                                modifier = Modifier.fillMaxWidth(),
                                text = example,
                                imageVector = Icons.AutoMirrored.TwoTone.TextSnippet,
                                title = stringResource(R.string.example),
                                onClick = { onTextToSpeech(example) },
                                onDoubleClick = { onExpandText(example) },
                                content = {
                                    HighlightText(
                                        fullText = example,
                                        highlightedText = word
                                    )
                                }
                            )
                        }
                        if (synonyms.isNotEmpty()) {
                            val joined = remember(synonyms) {
                                synonyms.joinToString(", ")
                            }
                            TextWithIcon(
                                modifier = Modifier.fillMaxWidth(),
                                text = joined,
                                imageVector = Icons.AutoMirrored.TwoTone.TextSnippet,
                                title = stringResource(R.string.synonym),
                                onClick = { onTextToSpeech(joined) },
                                onDoubleClick = { onExpandText(joined) }
                            )
                        }
                        if (antonyms.isNotEmpty()) {
                            val joined = remember(antonyms) {
                                antonyms.joinToString(", ")
                            }
                            TextWithIcon(
                                modifier = Modifier.fillMaxWidth(),
                                text = joined,
                                imageVector = Icons.AutoMirrored.TwoTone.TextSnippet,
                                title = stringResource(R.string.antonym),
                                onClick = { onTextToSpeech(joined) },
                                onDoubleClick = { onExpandText(joined) }
                            )
                        }
                        if (index < meaning.definitions.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.background(color = MaterialTheme.colorScheme.tertiary)
                            )
                        }
                    }
                }
            )
        }
    )
}