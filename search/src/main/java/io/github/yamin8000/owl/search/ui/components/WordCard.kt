/*
 *     freeDictionaryApp/freeDictionaryApp.search.main
 *     WordCard.kt Copyrighted by Yamin Siahmargooei at 2025/9/8
 *     WordCard.kt Last modified at 2025/8/31
 *     This file is part of freeDictionaryApp/freeDictionaryApp.search.main.
 *     Copyright (C) 2025  Yamin Siahmargooei
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
import io.github.yamin8000.owl.common.ui.components.ClickableIcon
import io.github.yamin8000.owl.common.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.common.ui.theme.MyPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.search.ui.components.texts.PronunciationText
import io.github.yamin8000.owl.search.ui.components.texts.WordText
import io.github.yamin8000.owl.strings.R
import net.datafaker.Faker

@MyPreview
@Composable
private fun Preview() {
    PreviewTheme {
        val faker = Faker()
        Column(
            modifier = Modifier.padding(Sizes.Large),
            verticalArrangement = Arrangement.spacedBy(Sizes.Large),
            content = {
                WordCard(
                    word = faker.word().noun(),
                    pronunciation = faker.word().noun(),
                    onShareWord = {},
                    onAddToFavourite = {}
                )
            }
        )
    }
}

@Composable
fun WordCard(
    word: String,
    pronunciation: String?,
    modifier: Modifier = Modifier,
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
                        modifier = Modifier.weight(3f),
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