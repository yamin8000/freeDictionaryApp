/*
 *     Owl: an android app for Owlbot Dictionary API
 *     Cards.kt Created by Yamin Siahmargooei at 2022/8/24
 *     This file is part of Owl.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Owl is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Owl is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Owl.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.ui.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.github.yamin8000.owl.content.home.WordDefinitionText
import io.github.yamin8000.owl.content.home.WordEmojiText
import io.github.yamin8000.owl.content.home.WordExampleText
import io.github.yamin8000.owl.content.home.WordTypeText
import io.github.yamin8000.owl.model.Definition

class DefinitionProvider : PreviewParameterProvider<Definition> {
    override val values: Sequence<Definition> = listOf(
        Definition(
            "noun",
            "a word definition",
            "some word in a sentence",
            "",
            "\uD83D\uDE02"
        )
    ).asSequence()
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun RemovableCard(
    @PreviewParameter(TextProvider::class)
    item: String,
    onClick: () -> Unit = { },
    onLongClick: (() -> Unit)? = null
) {
    Card(
        shape = CutCornerShape(15.dp)
    ) {
        RippleText(
            modifier = Modifier.padding(8.dp),
            content = {
                Text(
                    text = item,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            },
            onClick = onClick,
            onLongClick = onLongClick
        )
    }
}