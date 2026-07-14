/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     LicenseCard.kt Copyrighted by Yamin Siahmargooei at 2026/6/26
 *     LicenseCard.kt Last modified at 2026/6/26
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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Attribution
import androidx.compose.material3.Icon
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
import io.github.yamin8000.owl.strings.R

@AppPreview
@Composable
private fun Preview() {
    PreviewTheme {
        LicenseCard(
            name = "License",
            url = "https://google.com"
        )
    }
}

@Composable
internal fun LicenseCard(
    name: String?,
    url: String?,
    modifier: Modifier = Modifier,
) {
    AppOutlinedCard(
        modifier = modifier,
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Sizes.Medium)
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    Sizes.Medium,
                    Alignment.CenterHorizontally
                ),
                content = {
                    Icon(
                        imageVector = Icons.TwoTone.Attribution,
                        contentDescription = stringResource(R.string.license),
                    )
                    if (name != null) {
                        AppText(
                            text = name,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                    if (url != null) {
                        val uriHandler = LocalUriHandler.current
                        AppText(
                            text = stringResource(R.string.see_source),
                            style = MaterialTheme.typography.bodySmall,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable(onClick = { uriHandler.openUri(url) })
                        )
                    }
                }
            )
        }
    )
}