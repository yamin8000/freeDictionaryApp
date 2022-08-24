/*
 *     Owl: an android app for Owlbot Dictionary API
 *     About.kt Created by Yamin Siahmargooei at 2022/8/21
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

package io.github.yamin8000.owl.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.composable.CopyAbleRippleText
import io.github.yamin8000.owl.ui.composable.PersianText

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AboutContent(
    navController: NavHostController? = null
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val uriHandler = LocalUriHandler.current
            val sourceUri = stringResource(R.string.github_source)
            val owlBotUri = stringResource(R.string.owl_bot_link)
            Image(
                painterResource(id = R.drawable.ic_gplv3),
                stringResource(id = R.string.gplv3_image_description),
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PersianText(
                    stringResource(id = R.string.license_header),
                    modifier = Modifier.fillMaxWidth()
                )
                CopyAbleRippleText(
                    text = sourceUri,
                    textDecoration = TextDecoration.Underline
                ) { uriHandler.openUri(sourceUri) }
                PersianText(
                    stringResource(id = R.string.about_app),
                    modifier = Modifier.fillMaxWidth()
                )
                CopyAbleRippleText(
                    text = owlBotUri,
                    textDecoration = TextDecoration.Underline
                ) { uriHandler.openUri(owlBotUri) }
            }
        }
    }
}