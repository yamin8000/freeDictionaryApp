/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     About.kt Copyrighted by Yamin Siahmargooei at 2024/5/9
 *     About.kt Last modified at 2024/5/6
 *     This file is part of freeDictionaryApp/freeDictionaryApp.app.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
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

package io.github.yamin8000.owl.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import io.github.yamin8000.owl.BuildConfig
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.components.Ripple
import io.github.yamin8000.owl.common.ui.components.ScaffoldWithTitle
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.strings.R
import io.github.yamin8000.owl.R as RApp

@Composable
internal fun AboutContent(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    ScaffoldWithTitle(
        modifier = modifier,
        title = stringResource(R.string.about),
        onBackClick = onBackClick,
        content = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(Sizes.Medium),
                content = {
                    val uriHandler = LocalUriHandler.current
                    val sourceUri = stringResource(R.string.github_source)
                    val freeDictionaryUri = stringResource(R.string.free_dictionary_link)
                    val licenseUri = stringResource(R.string.license_link)

                    Ripple(
                        onClick = { uriHandler.openUri(licenseUri) },
                        content = {
                            Image(
                                painter = painterResource(id = RApp.drawable.ic_gplv3),
                                contentDescription = stringResource(id = R.string.gplv3_image_description),
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.FillWidth,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                            )
                        }
                    )
                    AppText(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = stringResource(R.string.version_name, BuildConfig.VERSION_NAME)
                    )
                    Spacer(modifier = Modifier.padding(bottom = Sizes.Large))
                    AppText(
                        text = stringResource(id = R.string.license_header),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Ripple(
                        onClick = { uriHandler.openUri(sourceUri) },
                        content = {
                            Text(
                                text = sourceUri,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    )
                    AppText(
                        text = stringResource(id = R.string.about_app),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Ripple(
                        onClick = { uriHandler.openUri(freeDictionaryUri) },
                        content = {
                            Text(
                                text = freeDictionaryUri,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    )
                }
            )
        }
    )
}