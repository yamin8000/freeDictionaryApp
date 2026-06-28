/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     PhoneticList.kt Copyrighted by Yamin Siahmargooei at 2026/6/26
 *     PhoneticList.kt Last modified at 2026/6/26
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_home.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.yamin8000.owl.common.ui.theme.AppPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.search.domain.model.Phonetic
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.util.UUID

@AppPreview
@Composable
private fun Preview() {
    PreviewTheme {
        PhoneticList(
            phonetics = Phonetic.mockList().toImmutableList(),
            onPlayAudio = {}
        )
    }
}

@Composable
internal fun PhoneticList(
    phonetics: ImmutableList<Phonetic>,
    onPlayAudio: (String) -> Unit
) {
    BoxWithConstraints(
        content = {
            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    Sizes.Small,
                    Alignment.CenterHorizontally
                ),
                content = {
                    items(
                        items = phonetics,
                        key = { phonetic -> phonetic.id ?: UUID.randomUUID() },
                        itemContent = { phonetic ->
                            PhoneticCard(
                                modifier = Modifier.width(maxWidth.div(2f) - Sizes.Small),
                                text = phonetic.text,
                                audio = phonetic.audio,
                                sourceUrl = phonetic.sourceUrl,
                                license = phonetic.license,
                                onPlayAudio = onPlayAudio
                            )
                        }
                    )
                }
            )
        }
    )
}