/*
 *     Owl: an android app for Owlbot Dictionary API
 *     Texts.kt Created by Yamin Siahmargooei at 2022/7/3
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

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.theme.Samim
import io.github.yamin8000.owl.util.TtsEngine

class TextProvider : PreviewParameterProvider<String> {
    override val values = listOf("سلام", "یمین").asSequence()
}

@Preview(showBackground = true)
@Composable
fun PersianText(
    @PreviewParameter(TextProvider::class)
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 14.sp,
    fontFamily: FontFamily = Samim,
    textAlign: TextAlign = TextAlign.Right,
    color: Color = Color.Unspecified
) {
    Text(
        text,
        modifier = modifier,
        fontFamily = fontFamily,
        textAlign = textAlign,
        fontSize = fontSize,
        color = color
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RippleText(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        content()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CopyAbleRippleText(
    text: String,
    textDecoration: TextDecoration = TextDecoration.None,
    onClick: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val textCopied = stringResource(R.string.text_copied)
    Box(
        modifier = Modifier.combinedClickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(),
            onClick = onClick,
            onLongClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                clipboardManager.setText(AnnotatedString(text))
                Toast.makeText(context, textCopied, Toast.LENGTH_SHORT).show()
            }
        )
    ) {
        Text(
            text = text,
            textDecoration = textDecoration,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
fun CopyAbleRippleTextWithIcon(
    text: String,
    iconPainter: Painter,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(iconPainter, text)
        CopyAbleRippleText(text, onClick = onClick)
    }
}

@Composable
fun SpeakableRippleTextWithIcon(
    text: String,
    iconPainter: Painter,
    ttsEngine: TtsEngine
) {
    CopyAbleRippleTextWithIcon(text, iconPainter) {
        ttsEngine.speak(text)
    }
}

@Composable
fun EmptyListErrorText() {
    PersianText(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        text = stringResource(R.string.list_empty),
        color = MaterialTheme.colorScheme.error,
        textAlign = TextAlign.Center
    )
}