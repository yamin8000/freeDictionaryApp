/*
 *     freeDictionaryApp/freeDictionaryApp.feature_about.main
 *     About.kt Copyrighted by Yamin Siahmargooei at 2025/11/16
 *     About.kt Last modified at 2025/11/16
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_about.main.
 *     Copyright (C) 2025  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_about.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_about.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_about.ui

import android.content.res.Configuration
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.components.Ripple
import io.github.yamin8000.owl.common.ui.components.ScaffoldWithTitle
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.strings.R
import io.github.yamin8000.owl.feature_about.R.drawable as R_drawable

@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    PreviewTheme {
        AboutScreen(
            versionName = "1.0.0",
            onBackClick = {}
        )
    }
}

@Composable
fun AboutScreen(
    onBackClick: () -> Unit,
    versionName: String,
    modifier: Modifier = Modifier,
    vm: AboutViewModel = hiltViewModel()
) {
    val state = vm.state.collectAsStateWithLifecycle()

    ScaffoldWithTitle(
        modifier = modifier,
        title = stringResource(R.string.about),
        onBackClick = onBackClick,
        content = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    Sizes.Medium,
                    Alignment.CenterVertically
                ),
                content = {
                    val uriHandler = LocalUriHandler.current
                    val sourceUri = stringResource(R.string.github_source)
                    val freeDictionaryUri = stringResource(R.string.free_dictionary_link)
                    val licenseUri = stringResource(R.string.license_link)

                    Ripple(
                        onClick = { uriHandler.openUri(licenseUri) },
                        content = {
                            val config = LocalConfiguration.current
                            val fill = remember(config) {
                                if (config.orientation == Configuration.ORIENTATION_PORTRAIT) 1f
                                else .5f
                            }
                            Image(
                                painter = painterResource(id = R_drawable.ic_gplv3),
                                contentDescription = stringResource(id = R.string.gplv3_image_description),
                                contentScale = ContentScale.FillWidth,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
                                modifier = Modifier.fillMaxWidth(fill)
                            )
                        }
                    )
                    AppText(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = stringResource(R.string.version_name, versionName)
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