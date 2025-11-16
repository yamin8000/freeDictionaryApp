/*
 *     freeDictionaryApp/freeDictionaryApp.feature_about.main
 *     ContributorItem.kt Copyrighted by Yamin Siahmargooei at 2025/11/17
 *     ContributorItem.kt Last modified at 2025/11/17
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

package io.github.yamin8000.owl.feature_about.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.common.util.randomColor
import io.github.yamin8000.owl.feature_about.R

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewTheme {
        ContributorItem(
            profileUrl = "https://github.com/yamin8000",
            avatarUrl = "https://avatars.githubusercontent.com/u/9919?s=200&v=4",
            username = "yamin8000",
            borderColor = randomColor()
        )
    }
}

@Composable
internal fun ContributorItem(
    profileUrl: String,
    avatarUrl: String,
    username: String,
    borderColor: Color,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = modifier.clickable(
            onClick = { uriHandler.openUri(profileUrl) }
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Sizes.Small, Alignment.CenterVertically),
        content = {
            AsyncImage(
                model = avatarUrl,
                contentDescription = username,
                placeholder = painterResource(R.drawable.image_48px),
                fallback = painterResource(R.drawable.broken_image_48px),
                modifier = Modifier
                    .size(Sizes.xxLarge * 1.5f)
                    .clip(CircleShape)
                    .border(
                        width = Sizes.Small,
                        color = borderColor,
                        shape = CircleShape
                    )
            )
            AppText(
                text = username,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}