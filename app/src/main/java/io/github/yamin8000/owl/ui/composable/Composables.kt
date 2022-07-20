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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun DefinitionCard(
    word: String,
    imagePainter: Painter,
    type: String,
    definition: String,
    example: String,
    emoji: String
) {
    Card(
        shape = RoundedCornerShape(25.dp),
        modifier = Modifier.padding(vertical = 8.dp),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = imagePainter,
                contentDescription = word,
                modifier = Modifier.fillMaxWidth()
            )
            Text(type, modifier = Modifier.fillMaxWidth())
            Text(definition, modifier = Modifier.fillMaxWidth())
            Text(example, modifier = Modifier.fillMaxWidth())
            Text(emoji, modifier = Modifier.fillMaxWidth())
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