/*
 *     freeDictionaryApp/freeDictionaryApp.common.main
 *     Texts.kt Copyrighted by Yamin Siahmargooei at 2024/8/18
 *     Texts.kt Last modified at 2024/8/18
 *     This file is part of freeDictionaryApp/freeDictionaryApp.common.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import io.github.yamin8000.owl.common.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.common.ui.theme.Samim
import io.github.yamin8000.owl.common.ui.theme.defaultGradientBorder
import io.github.yamin8000.owl.common.ui.util.ContextUtils.findActivity
import io.github.yamin8000.owl.common.ui.util.LocaleUtils.getCurrentLocale
import io.github.yamin8000.owl.common.ui.util.StringUtils.sanitizeWords
import io.github.yamin8000.owl.strings.R
import java.util.Locale

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
        val start = fullText.indexOf(highlightedText, 0, true)
        val end = start + highlightedText.length
        Text(
            modifier = modifier,
            text = buildAnnotatedString {
                append(fullText)
                addStyle(highlightedTextStyle, start, end)
            }
        )
    } else {
        Text(
            text = fullText,
            modifier = modifier
        )
    }
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
    val context = LocalContext.current
    var localStyle by remember(style) { mutableStateOf(style) }
    var localFontFamily by remember(fontFamily) { mutableStateOf(fontFamily) }
    val currentLocale = remember(context) { getCurrentLocale(context) }
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
    val onDismissDialog = remember { { isDialogShown = false } }
    val onShowDialog = remember { { isDialogShown = true } }

    val onLongClick = remember(text) {
        {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            clipboardManager.setText(AnnotatedString(text))
            Toast
                .makeText(context, textCopied, Toast.LENGTH_SHORT)
                .show()
        }
    }

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
                            SelectionContainer(content = { content?.invoke() ?: Text(text) })
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
                onDoubleClick = onShowDialog,
                onLongClick = onLongClick
            )
    )
    if (isDialogShown) {
        Dialog(
            onDismissRequest = onDismissDialog,
            content = {
                Surface(
                    shape = DefaultCutShape,
                    content = {
                        val regex = remember { Regex("\\s+") }
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(8.dp),
                            content = {
                                val words = sanitizeWords(text.split(regex).toSet()).toList()
                                items(words) { item ->
                                    val onItemClick = remember(onDoubleClick, item) {
                                        {
                                            onDoubleClick?.invoke(item)
                                            isDialogShown = false
                                        }
                                    }
                                    ElevatedSuggestionChip(
                                        border = defaultGradientBorder(),
                                        onClick = onItemClick,
                                        label = {
                                            Text(
                                                text = item,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier
                                                    .padding(8.dp)
                                                    .fillMaxSize()
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
                            contentDescription = text,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        PersianText(
                            text = title,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                )
            } else {
                Icon(
                    imageVector = imageVector,
                    contentDescription = text,
                    tint = MaterialTheme.colorScheme.secondary
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
    title: String? = null,
    content: @Composable (() -> Unit)? = null,
    onClick: () -> Unit,
    onDoubleClick: ((String) -> Unit)? = null
) {
    val context = LocalContext.current
    val increaseVolumeText = context.getString(R.string.increase_volume_notice)
    val audio = remember {
        context.findActivity()?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    /*val tts = LocalTTS.current
    val onClick = remember(tts, audio, text) {
        {
            if (audio.getStreamVolume(AudioManager.STREAM_MUSIC) == 0)
                Toast.makeText(context, increaseVolumeText, Toast.LENGTH_SHORT).show()
            tts?.speak(text)
            Unit
        }
    }*/
    CopyAbleRippleTextWithIcon(
        text = text,
        content = content,
        title = title,
        imageVector = imageVector,
        onDoubleClick = onDoubleClick,
        onClick = onClick
    )
}