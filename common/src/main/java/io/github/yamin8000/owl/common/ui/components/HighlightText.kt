/*
 *     freeDictionaryApp/freeDictionaryApp.common.main
 *     HighlightText.kt Copyrighted by Yamin Siahmargooei at 2025/1/16
 *     HighlightText.kt Last modified at 2025/1/16
 *     This file is part of freeDictionaryApp/freeDictionaryApp.common.main.
 *     Copyright (C) 2025  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.common.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.common.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.common.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import io.github.yamin8000.owl.common.ui.theme.MyPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme

@MyPreview
@Composable
private fun Preview() {
    PreviewTheme {
        HighlightText(
            fullText = "eating",
            highlightedText = "ing"
        )
    }
}

@Composable
fun HighlightText(
    modifier: Modifier = Modifier,
    fullText: String,
    highlightedText: String,
    highlightedTextStyle: SpanStyle = SpanStyle(
        fontWeight = FontWeight.ExtraBold,
        textDecoration = TextDecoration.Underline
    )
) {
    if (highlightedText.isNotBlank() && fullText.contains(highlightedText, true)) {
        val start = remember(fullText, highlightedText) {
            fullText.indexOf(highlightedText, 0, true)
        }
        val end = remember(start, highlightedText) {
            start + highlightedText.length
        }

        val text = remember(fullText, start, end) {
            buildAnnotatedString {
                append(fullText)
                addStyle(highlightedTextStyle, start, end)
            }
        }

        Text(
            modifier = modifier,
            text = text
        )
    } else {
        Text(
            text = fullText,
            modifier = modifier
        )
    }
}