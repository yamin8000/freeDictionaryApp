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

import android.content.ClipData
import android.content.Context
import android.content.res.Configuration
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
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import io.github.yamin8000.owl.common.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.common.ui.theme.defaultGradientBorder
import io.github.yamin8000.owl.common.util.ContextUtils.findActivity
import io.github.yamin8000.owl.common.util.LocalTTS
import io.github.yamin8000.owl.common.util.StringUtils.sanitizeWords
import io.github.yamin8000.owl.strings.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CopyAbleRippleText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onDoubleClick: ((String) -> Unit)? = null,
    content: @Composable (() -> Unit)? = null
) {
    val textCopied = stringResource(R.string.text_copied)
    val context = LocalContext.current
    val clipboardManager = LocalClipboard.current
    val haptic = LocalHapticFeedback.current

    var isDialogShown by remember { mutableStateOf(false) }

    Box(
        content = {
            Box(
                modifier = Modifier.padding(Sizes.Medium),
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
        modifier = modifier
            .clip(DefaultCutShape)
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = onClick,
                onDoubleClick = { isDialogShown = true },
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    clipboardManager.nativeClipboard.setPrimaryClip(
                        ClipData.newPlainText(text, text)
                    )
                    Toast.makeText(context, textCopied, Toast.LENGTH_SHORT)
                        .show()
                }
            )
    )
    if (isDialogShown && onDoubleClick != null) {
        Dialog(
            onDismissRequest = { isDialogShown = false },
            content = {
                Surface(
                    shape = DefaultCutShape,
                    content = {
                        val words = remember {
                            sanitizeWords(text.split(Regex("\\s+")).toSet()).toList()
                        }
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(
                                Sizes.Small,
                                Alignment.CenterVertically
                            ),
                            horizontalArrangement = Arrangement.spacedBy(
                                Sizes.Small,
                                Alignment.CenterHorizontally
                            ),
                            modifier = Modifier.padding(Sizes.Medium),
                            content = {
                                items(words) { item ->
                                    ElevatedSuggestionChip(
                                        border = defaultGradientBorder(),
                                        onClick = {
                                            onDoubleClick(item)
                                            isDialogShown = false
                                        },
                                        label = {
                                            Text(
                                                text = item,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier
                                                    .padding(Sizes.Medium)
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
    text: String,
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    onDoubleClick: ((String) -> Unit)? = null,
    content: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Sizes.Medium, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        content = {
            val iconContent = remember(imageVector, text) {
                movableContentOf {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            if (title != null) {
                val config = LocalConfiguration.current
                val orientation = remember(config) { config.orientation }
                val density = LocalDensity.current
                val titleFraction = remember(density) {
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        .25f * (density.fontScale + 1) / 2
                    } else {
                        .15f * (density.fontScale + 1) / 2
                    }
                }
                Box(
                    modifier = Modifier.fillMaxWidth(titleFraction),
                    content = {
                        val titleContent = remember {
                            movableContentOf {
                                iconContent()
                                AppText(
                                    text = title,
                                    color = MaterialTheme.colorScheme.secondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(
                                    Sizes.Medium,
                                    Alignment.CenterVertically
                                ),
                                content = { titleContent() }
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(
                                    Sizes.Medium,
                                    Alignment.Start
                                ),
                                content = { titleContent() }
                            )
                        }
                    }
                )
            } else {
                iconContent()
            }
            CopyAbleRippleText(
                modifier = Modifier.fillMaxWidth(),
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
    modifier: Modifier = Modifier,
    ttsText: String = text,
    title: String? = null,
    onDoubleClick: ((String) -> Unit)? = null,
    content: @Composable (() -> Unit)? = null
) {
    val context = LocalContext.current
    val increaseVolumeText = context.getString(R.string.increase_volume_notice)
    val audio = remember {
        context.findActivity()?.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
    }

    val tts = LocalTTS.current
    CopyAbleRippleTextWithIcon(
        modifier = modifier,
        text = text,
        content = content,
        title = title,
        imageVector = imageVector,
        onDoubleClick = onDoubleClick,
        onClick = {
            if (audio?.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) {
                Toast.makeText(context, increaseVolumeText, Toast.LENGTH_SHORT).show()
            }
            tts?.speak(ttsText)
            Unit
        }
    )
}