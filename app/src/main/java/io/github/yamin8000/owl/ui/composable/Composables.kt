/*
 *     Owl: an android app for Owlbot Dictionary API
 *     Composables.kt Created by Yamin Siahmargooei at 2022/7/3
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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.github.yamin8000.owl.model.Definition
import org.intellij.lang.annotations.JdkConstants

@Composable
fun AddDefinitionCard(definition: Definition) {
    DefinitionCard(
        definition = definition.definition,
        imageUrl = definition.imageUrl,
        type = definition.type,
        example = definition.example,
        emoji = definition.emoji
    )
}

@Composable
fun DefinitionCard(
    definition: String,
    imageUrl: String?,
    type: String?,
    example: String?,
    emoji: String?
) {
    Card(
        shape = RoundedCornerShape(25.dp),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
    ) {
        Column() {
            AsyncImage(
                model = imageUrl,
                contentDescription = definition,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth
            )
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (type != null)
                    Text(type)
                Text(definition)
                if (example != null)
                    Text(example)
                if (emoji != null)
                    Text(emoji)
            }
        }
    }
}

@Composable
fun ClickableIcon(
    iconPainter: Painter,
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            painter = iconPainter,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun ButtonWithIcon(
    onClick: () -> Unit,
    iconPainter: Painter,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScope: @Composable RowScope.() -> Unit = {
        Icon(iconPainter, contentDescription)
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        PersianText(contentDescription)
    }
) {
    Button(onClick = onClick, modifier = modifier, content = contentScope)
}

@Composable
fun RippleText(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(),
            onClick = onClick
        )
    )
}