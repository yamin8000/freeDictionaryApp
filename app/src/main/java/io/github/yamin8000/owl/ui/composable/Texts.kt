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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
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
import io.github.yamin8000.owl.ui.theme.Samim
import io.github.yamin8000.owl.util.getCurrentLocale
import io.github.yamin8000.owl.util.speak

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
        text,
        modifier,
        color,
        fontSize,
        fontStyle,
        fontWeight,
        localFontFamily,
        letterSpacing,
        textDecoration,
        textAlign,
        lineHeight,
        overflow,
        softWrap,
        maxLines,
        onTextLayout,
        localStyle
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Ripple(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = onClick,
                onLongClick = onLongClick
            ),
        content = { content() }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CopyAbleRippleText(
    text: String,
    textDecoration: TextDecoration = TextDecoration.None,
    onClick: () -> Unit,
    onDoubleClick: ((String) -> Unit)? = null
) {
    val textCopied = stringResource(R.string.text_copied)
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val haptic = LocalHapticFeedback.current

    var isDialogShown by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .clip(CutCornerShape(15.dp))
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = onClick,
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    clipboardManager.setText(AnnotatedString(text))
                    Toast
                        .makeText(context, textCopied, Toast.LENGTH_SHORT)
                        .show()
                },
                onDoubleClick = { isDialogShown = true }
            ),
        content = {
            Text(
                text = text,
                textDecoration = textDecoration,
                modifier = Modifier.padding(8.dp)
            )
        }
    )
    if (isDialogShown) {
        Dialog(
            onDismissRequest = { isDialogShown = false },
            content = {
                Surface {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(8.dp),
                        content = {
                            items(text.split(Regex("\\s+"))) {
                                ElevatedSuggestionChip(
                                    onClick = {
                                        onDoubleClick?.invoke(it)
                                        isDialogShown = false
                                    },
                                    label = {
                                        Text(
                                            text = it.replace(Regex("\\W+"), ""),
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
            }
        )
    }
}

@Composable
fun CopyAbleRippleTextWithIcon(
    text: String,
    imageVector: ImageVector,
    onClick: () -> Unit,
    onDoubleClick: ((String) -> Unit)? = null
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = {
            Icon(imageVector, text)
            CopyAbleRippleText(
                text = text,
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
    onDoubleClick: ((String) -> Unit)? = null
) {
    TtsAwareComposable(
        ttsLanguageLocaleTag = localeTag,
        content = {
            CopyAbleRippleTextWithIcon(
                text = text,
                imageVector = imageVector,
                onClick = { it.speak(text) },
                onDoubleClick = onDoubleClick
            )
        }
    )
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