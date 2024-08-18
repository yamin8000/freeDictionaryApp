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

package io.github.yamin8000.owl.ui.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.yamin8000.owl.ui.theme.MyPreview
import io.github.yamin8000.owl.ui.theme.PreviewTheme

@MyPreview
@Composable
private fun AboutContentPreview() {
    PreviewTheme {
        AboutContent { }
    }
}

@Composable
internal fun AboutContent(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
/*
    ScaffoldWithTitle(
        modifier = modifier,
        title = stringResource(R.string.about),
        onBackClick = onBackClick,
        content = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    val uriHandler = LocalUriHandler.current
                    val sourceUri = stringResource(R.string.github_source)
                    val freeDictionaryUri = stringResource(R.string.free_dictionary_link)
                    val licenseUri = stringResource(R.string.license_link)
                    io.github.yamin8000.owl.coreui.components.Ripple(
                        onClick = { uriHandler.openUri(licenseUri) },
                        content = {
                            Image(
                                painter = painterResource(id = R.drawable.ic_gplv3),
                                contentDescription = stringResource(id = R.string.gplv3_image_description),
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.FillWidth,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                            )
                        }
                    )
                    io.github.yamin8000.owl.coreui.components.PersianText(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = stringResource(R.string.version_name, BuildConfig.VERSION_NAME)
                    )
                    Spacer(modifier = Modifier.padding(bottom = 16.dp))
                    io.github.yamin8000.owl.coreui.components.PersianText(
                        text = stringResource(id = R.string.license_header),
                        modifier = Modifier.fillMaxWidth()
                    )
                    io.github.yamin8000.owl.coreui.components.Ripple(
                        onClick = { uriHandler.openUri(sourceUri) },
                        content = {
                            Text(
                                text = sourceUri,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    )
                    io.github.yamin8000.owl.coreui.components.PersianText(
                        text = stringResource(id = R.string.about_app),
                        modifier = Modifier.fillMaxWidth()
                    )
                    io.github.yamin8000.owl.coreui.components.Ripple(
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
*/
}