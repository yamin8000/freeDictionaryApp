package io.github.yamin8000.owl.search.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.TextSnippet
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import io.github.yamin8000.owl.common.ui.components.AppOutlinedCard
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.components.SpeakableRippleTextWithIcon
import io.github.yamin8000.owl.common.ui.theme.AppPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.common.ui.theme.defaultGradientBorder
import io.github.yamin8000.owl.search.domain.model.Meaning
import io.github.yamin8000.owl.search.ui.components.texts.WordDefinitionText
import io.github.yamin8000.owl.search.ui.components.texts.WordExampleText
import io.github.yamin8000.owl.search.ui.components.texts.WordTypeText
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
                    onWordChipClick = {}
                )
            }
        )
    }
}

@Composable
fun MeaningCard(
    word: String,
    meaning: Meaning,
    modifier: Modifier = Modifier,
    onWordChipClick: ((String) -> Unit)? = null
) {
    AppOutlinedCard(
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
                    WordTypeText(
                        type = meaning.partOfSpeech,
                        onDoubleClick = onWordChipClick
                    )
                    meaning.definitions.forEachIndexed { index, (definition, examples, synonyms, antonyms) ->
                        WordDefinitionText(
                            word = word,
                            definition = definition,
                            onDoubleClick = onWordChipClick
                        )
                        examples.forEach { example ->
                            WordExampleText(
                                word = word,
                                example = example,
                                onDoubleClick = onWordChipClick
                            )
                        }
                        if (synonyms.isNotEmpty()) {
                            SpeakableRippleTextWithIcon(
                                text = synonyms.joinToString(", "),
                                imageVector = Icons.AutoMirrored.TwoTone.TextSnippet,
                                title = stringResource(R.string.synonym),
                            )
                        }
                        if (antonyms.isNotEmpty()) {
                            SpeakableRippleTextWithIcon(
                                text = antonyms.joinToString(", "),
                                imageVector = Icons.AutoMirrored.TwoTone.TextSnippet,
                                title = stringResource(R.string.antonym),
                            )
                        }
                        if (index < meaning.definitions.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.border(border = defaultGradientBorder())
                            )
                        }
                    }
                }
            )
        }
    )
}