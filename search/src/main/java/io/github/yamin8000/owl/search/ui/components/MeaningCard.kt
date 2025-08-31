package io.github.yamin8000.owl.search.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.TextSnippet
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import io.github.yamin8000.owl.common.ui.components.SpeakableRippleTextWithIcon
import io.github.yamin8000.owl.common.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.common.ui.theme.MyPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.common.ui.theme.defaultGradientBorder
import io.github.yamin8000.owl.search.domain.model.Meaning
import io.github.yamin8000.owl.search.ui.components.texts.WordDefinitionText
import io.github.yamin8000.owl.search.ui.components.texts.WordExampleText
import io.github.yamin8000.owl.search.ui.components.texts.WordTypeText
import io.github.yamin8000.owl.strings.R

@MyPreview
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
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(Sizes.Large, Alignment.CenterVertically),
                content = {
                    WordTypeText(
                        type = meaning.partOfSpeech,
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