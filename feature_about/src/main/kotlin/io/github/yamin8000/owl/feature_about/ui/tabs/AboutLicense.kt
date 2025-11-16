/*
 *     freeDictionaryApp/freeDictionaryApp.feature_about.main
 *     AboutLicense.kt Copyrighted by Yamin Siahmargooei at 2025/11/16
 *     AboutLicense.kt Last modified at 2025/11/16
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

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.components.Ripple
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.strings.R
import io.github.yamin8000.owl.feature_about.R.drawable as R_drawable

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewTheme {
        AboutLicense()
    }
}

@Composable
internal fun AboutLicense(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Sizes.Medium, Alignment.CenterVertically),
        content = {
            val uriHandler = LocalUriHandler.current
            val sourceUri = stringResource(R.string.github_source)
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
        }
    )
}