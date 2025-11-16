/*
 *     freeDictionaryApp/freeDictionaryApp.feature_about.main
 *     AboutInfo.kt Copyrighted by Yamin Siahmargooei at 2025/11/16
 *     AboutInfo.kt Last modified at 2025/11/16
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

package io.github.yamin8000.owl.feature_about.ui.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.components.Ripple
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.strings.R

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewTheme {
        AboutInfo(
            installedVersionName = "1.0.0",
            latestVersionName = "1.2.0",
            description = "This is a sample description of the app."
        )
    }
}

@Composable
internal fun AboutInfo(
    installedVersionName: String,
    latestVersionName: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Sizes.Medium, Alignment.CenterVertically),
        content = {
            AppText(
                maxLines = 2,
                text = stringResource(
                    R.string.version_name,
                    installedVersionName
                )
            )
            AppText(
                text = stringResource(R.string.latest_version, latestVersionName),
                maxLines = 2
            )
            AppText(
                text = description,
                modifier = Modifier.fillMaxWidth()
            )
            AppText(
                text = stringResource(id = R.string.about_app),
                modifier = Modifier.fillMaxWidth()
            )
            val uriHandler = LocalUriHandler.current
            val freeDictionaryUri = stringResource(R.string.free_dictionary_link)
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