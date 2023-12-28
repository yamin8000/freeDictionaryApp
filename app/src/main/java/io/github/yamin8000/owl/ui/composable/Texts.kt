/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     Texts.kt Copyrighted by Yamin Siahmargooei at 2023/8/26
 *     Texts.kt Last modified at 2023/8/26
 *     This file is part of freeDictionaryApp/freeDictionaryApp.app.main.
 *     Copyright (C) 2023  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.ui.composable

import android.content.Context
import android.media.AudioManager
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.ui.theme.Samim
import io.github.yamin8000.owl.util.findActivity
import io.github.yamin8000.owl.util.getCurrentLocale
import io.github.yamin8000.owl.util.sanitizeWords
import io.github.yamin8000.owl.util.speak

@Composable
fun HighlightText(
    fullText: String,
    highlightedText: String,
    highlightedTextStyle: SpanStyle = SpanStyle(
        fontWeight = FontWeight.ExtraBold,
        textDecoration = TextDecoration.Underline
    )
) {
    if (highlightedText.isNotBlank() && fullText.contains(highlightedText, true)) {
        val start = fullText.indexOf(highlightedText, 0, true)
        val end = start + highlightedText.length
        Text(
            buildAnnotatedString {
                append(fullText)
                addStyle(highlightedTextStyle, start, end)
            }
        )
    } else Text(fullText)
}

@Composable
fun PersianText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = 14.sp,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    var localStyle = style
    var localFontFamily = fontFamily
    val currentLocale = getCurrentLocale(LocalContext.current)
    if (currentLocale.language == Locale("fa").language) {
        localFontFamily = Samim
        localStyle = LocalTextStyle.current.copy(textDirection = TextDirection.Rtl)
    }
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = localFontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = localStyle
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CopyAbleRippleText(
    text: String,
    onClick: () -> Unit,
    content: @Composable (() -> Unit)? = null,
    onDoubleClick: ((String) -> Unit)? = null
) {
    val textCopied = stringResource(R.string.text_copied)
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val haptic = LocalHapticFeedback.current

    var isDialogShown by remember { mutableStateOf(false) }

    Box(
        content = {
            Box(
                modifier = Modifier.padding(8.dp),
                content = {
                    val selectionColors = TextSelectionColors(
                        handleColor = MaterialTheme.colorScheme.secondary,
                        backgroundColor = MaterialTheme.colorScheme.onSecondary
                    )
                    CompositionLocalProvider(
                        values = arrayOf(LocalTextSelectionColors provides selectionColors),
                        content = {
                            SelectionContainer(
                                content = {
                                    content?.invoke() ?: Text(text)
                                }
                            )
                        }
                    )
                }
            )
        },
        modifier = Modifier
            .clip(DefaultCutShape)
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = onClick,
                onDoubleClick = { isDialogShown = true },
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    clipboardManager.setText(AnnotatedString(text))
                    Toast
                        .makeText(context, textCopied, Toast.LENGTH_SHORT)
                        .show()
                }
            )
    )
    if (isDialogShown) {
        Dialog(
            onDismissRequest = { isDialogShown = false },
            content = {
                Surface(
                    shape = DefaultCutShape,
                    content = {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(8.dp),
                            content = {
                                val words = sanitizeWords(text.split(Regex("\\s+")).toSet())
                                items(words.toList()) {
                                    ElevatedSuggestionChip(
                                        onClick = {
                                            onDoubleClick?.invoke(it)
                                            isDialogShown = false
                                        },
                                        label = {
                                            Text(
                                                text = it,
                                                modifier = Modifier
                                                    .padding(8.dp)
                                                    .fillMaxSize(),
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    )
                                }
                            }
                        )
                    }
                )
            }
        )
    }
}

@Composable
fun CopyAbleRippleTextWithIcon(
    title: String? = null,
    text: String,
    imageVector: ImageVector,
    onClick: () -> Unit,
    content: @Composable (() -> Unit)? = null,
    onDoubleClick: ((String) -> Unit)? = null
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = {
            if (title != null) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(0.25f),
                    content = {
                        Icon(
                            imageVector = imageVector,
                            contentDescription = text
                        )
                        PersianText(title)
                    }
                )
            } else {
                Icon(
                    imageVector = imageVector,
                    contentDescription = text
                )
            }
            CopyAbleRippleText(
                text = text,
                content = content,
                onClick = onClick,
                onDoubleClick = onDoubleClick
            )
        }
    )
}

@Composable
fun SpeakableRippleTextWithIcon(
    text: String,
    imageVector: ImageVector,
    localeTag: String,
    title: String? = null,
    content: @Composable (() -> Unit)? = null,
    onDoubleClick: ((String) -> Unit)? = null
) {
    val context = LocalContext.current
    val audio = context.findActivity()?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val increaseVolumeText = context.getString(R.string.increase_volume_notice)
    TtsAwareContent(
        ttsLanguageLocaleTag = localeTag,
        content = {
            CopyAbleRippleTextWithIcon(
                text = text,
                content = content,
                title = title,
                imageVector = imageVector,
                onDoubleClick = onDoubleClick,
                onClick = {
                    if (audio.getStreamVolume(AudioManager.STREAM_MUSIC) == 0)
                        Toast.makeText(context, increaseVolumeText, Toast.LENGTH_SHORT).show()
                    it.speak(text)
                }
            )
        }
    )
}