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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material.icons.twotone.RecordVoiceOver
import androidx.compose.material.icons.twotone.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.yamin8000.owl.common.ui.components.AppOutlinedCard
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.components.ClickableIcon
import io.github.yamin8000.owl.common.ui.theme.AppPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.strings.R

@AppPreview
@Composable
private fun Preview() {
    PreviewTheme {
        Column(
            modifier = Modifier.padding(Sizes.Large),
            verticalArrangement = Arrangement.spacedBy(Sizes.Large),
            content = {
                WordCard(
                    word = listOf("Word", "a-moderately-long-word").random(),
                    onWordTextToSpeech = {},
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
    modifier: Modifier = Modifier,
    onWordTextToSpeech: ((String) -> Unit)?,
    onAddToFavourite: (() -> Unit)?,
    onShareWord: (() -> Unit)?
) {
    AppOutlinedCard(
        modifier = modifier,
        border = BorderStroke(Sizes.xxSmall, MaterialTheme.colorScheme.tertiary),
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Sizes.Small)
                    .padding(horizontal = Sizes.Large),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                content = {
                    AppText(text = word)
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
                    if (onWordTextToSpeech != null) {
                        ClickableIcon(
                            imageVector = Icons.TwoTone.RecordVoiceOver,
                            contentDescription = "",
                            onClick = { onWordTextToSpeech(word) }
                        )
                    }
                }
            )
        }
    )
}