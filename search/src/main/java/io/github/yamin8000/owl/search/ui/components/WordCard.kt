package io.github.yamin8000.owl.search.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material.icons.twotone.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import io.github.yamin8000.owl.common.ui.components.ClickableIcon
import io.github.yamin8000.owl.common.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.common.ui.theme.MyPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.search.ui.components.texts.PronunciationText
import io.github.yamin8000.owl.search.ui.components.texts.WordText
import io.github.yamin8000.owl.strings.R

@MyPreview
@Composable
private fun Preview() {
    PreviewTheme {
        Column(
            modifier = Modifier.padding(Sizes.Large),
            verticalArrangement = Arrangement.spacedBy(Sizes.Large),
            content = {
                WordCard(
                    word = LoremIpsum(1).values.first(),
                    pronunciation = LoremIpsum(1).values.first(),
                    onShareWord = {},
                    onAddToFavourite = {}
                )
            }
        )
    }
}

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
        border = BorderStroke(Sizes.xxSmall, MaterialTheme.colorScheme.tertiary),
        modifier = modifier,
        content = {
            Row(
                modifier = Modifier.padding(Sizes.Large),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    Sizes.Large,
                    Alignment.CenterHorizontally
                ),
                content = {
                    Column(
                        modifier = Modifier.weight(2f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(
                            Sizes.Medium,
                            Alignment.CenterVertically
                        ),
                        content = {
                            WordText(
                                word = word
                            )
                            if (!pronunciation.isNullOrBlank()) {
                                PronunciationText(
                                    word = word,
                                    pronunciation = pronunciation,
                                )
                            }
                        }
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(
                            Sizes.Medium,
                            Alignment.CenterVertically
                        ),
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