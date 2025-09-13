/*
 *     freeDictionaryApp/freeDictionaryApp.search.main
 *     WordExampleText.kt Copyrighted by Yamin Siahmargooei at 2025/9/8
 *     WordExampleText.kt Last modified at 2025/8/31
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

package io.github.yamin8000.owl.search.ui.components.texts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.TextSnippet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.yamin8000.owl.common.ui.components.HighlightText
import io.github.yamin8000.owl.common.ui.components.SpeakableRippleTextWithIcon
import io.github.yamin8000.owl.strings.R

@Composable
internal fun WordExampleText(
    word: String,
    example: String,
    modifier: Modifier = Modifier,
    onDoubleClick: ((String) -> Unit)? = null
) {
    SpeakableRippleTextWithIcon(
        modifier = modifier,
        text = example,
        imageVector = Icons.AutoMirrored.TwoTone.TextSnippet,
        title = stringResource(R.string.example),
        content = { HighlightText(fullText = example, highlightedText = word) },
        onDoubleClick = onDoubleClick,
    )
}