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

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.yamin8000.owl.BuildConfig
import io.github.yamin8000.owl.common.ui.components.PersianText
import io.github.yamin8000.owl.common.ui.components.Ripple
import io.github.yamin8000.owl.common.ui.components.ScaffoldWithTitle
import io.github.yamin8000.owl.strings.R
import io.github.yamin8000.owl.R as RApp

/*@MyPreview
@Composable
private fun AboutContentPreview() {
    PreviewTheme {
        AboutContent { }
    }
}*/

@OptIn(ExperimentalMaterial3Api::class)
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
                verticalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    val uriHandler = LocalUriHandler.current
                    val sourceUri = stringResource(R.string.github_source)
                    val freeDictionaryUri = stringResource(R.string.free_dictionary_link)
                    val licenseUri = stringResource(R.string.license_link)

                    var state by remember { mutableStateOf(false) }

                    if (state) {
                        ModalBottomSheet(
                            onDismissRequest = { state = false },
                            containerColor = MaterialTheme.colorScheme.background,
                            dragHandle = {},
                            content = {
                                Box(
                                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                                    content = {
                                        Column {
                                            Text("Test")
                                            Text("Test")
                                            Text("Test")
                                            Text("Test")
                                            Text("Test")
                                            Text("Test")
                                            Text("Test")
                                        }
                                    }
                                )
                            }
                        )
                    }

                    Ripple(
                        onClick = {
                            state = true
                            //uriHandler.openUri(licenseUri)
                        },
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
                    PersianText(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = stringResource(R.string.version_name, BuildConfig.VERSION_NAME)
                    )
                    Spacer(modifier = Modifier.padding(bottom = 16.dp))
                    PersianText(
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
                    PersianText(
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun Test() {
    var state by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        sheetState.expand()
    }

    ModalBottomSheet(
        onDismissRequest = { state = false },
        containerColor = MaterialTheme.colorScheme.background,
        sheetState = sheetState,
        content = {
            Text("Test")
            Text("Test")
            Text("Test")
            Text("Test")
            Text("Test")
            Text("Test")
            Text("Test")
        }
    )

}