/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     PhoneticCard.kt Copyrighted by Yamin Siahmargooei at 2026/6/26
 *     PhoneticCard.kt Last modified at 2026/6/26
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_home.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.search.ui.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import io.github.yamin8000.owl.common.ui.components.AppOutlinedCard
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.theme.AppPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.search.domain.model.License
import io.github.yamin8000.owl.search.ui.icons.play_circle
import io.github.yamin8000.owl.strings.R

@AppPreview
@Composable
private fun Preview() {
    PreviewTheme {
        PhoneticCard(
            text = "free",
            audio = "test.mp3",
            sourceUrl = "https://google.com",
            license = License.mock(),
            onPlayAudio = {}
        )
    }
}

@Composable
internal fun PhoneticCard(
    onPlayAudio: (String) -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    audio: String? = null,
    sourceUrl: String? = null,
    license: License? = null
) {
    AppOutlinedCard(
        modifier = modifier,
        content = {
            Column(
                modifier = Modifier.padding(Sizes.Medium),
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            Sizes.Medium,
                            Alignment.CenterHorizontally
                        ),
                        content = {
                            if (!text.isNullOrBlank()) {
                                AppText(
                                    text = text,
                                    maxLines = 1,
                                    modifier = Modifier.basicMarquee()
                                )
                            }

                            if (!audio.isNullOrBlank()) {
                                IconButton(
                                    onClick = { onPlayAudio(audio) },
                                    content = {
                                        Icon(
                                            imageVector = play_circle,
                                            contentDescription = null
                                        )
                                    }
                                )
                            }
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            Sizes.Medium,
                            Alignment.CenterHorizontally
                        ),
                        content = {
                            if (license != null) {
                                AppText(
                                    text = license.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 1,
                                )
                            }

                            if (!sourceUrl.isNullOrBlank()) {
                                val uriHandler = LocalUriHandler.current
                                AppText(
                                    text = stringResource(R.string.see_source),
                                    textDecoration = TextDecoration.Underline,
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 2,
                                    modifier = Modifier.clickable(
                                        onClick = { uriHandler.openUri(sourceUrl) }
                                    )
                                )
                            }
                        }
                    )
                }
            )
        }
    )
}