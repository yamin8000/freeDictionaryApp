/*
 *     Owl: an android app for Owlbot Dictionary API
 *     Animations.kt Created by Yamin Siahmargooei at 2022/11/8
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

package io.github.yamin8000.owl.ui.composable

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.theme.PreviewTheme

@Preview
@Composable
fun PreviewSquareFillLoaderAnimation() {
    PreviewTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AnimatedAppIcon()
        }
    }
}

@Composable
fun AnimatedAppIcon(
    isAnimating: Boolean = true
) {
    var size by remember { mutableStateOf(0.dp) }

    LaunchedEffect(Unit) {
        while (isAnimating) {
            animate(
                typeConverter = Dp.VectorConverter,
                initialValue = 64.dp,
                targetValue = 56.dp,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000),
                    repeatMode = RepeatMode.Reverse
                ),
                block = { value, _ -> size = value }
            )
        }
    }

    Icon(
        painter = painterResource(R.drawable.ic_launcher_foreground),
        contentDescription = stringResource(R.string.app_name),
        tint = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.size(size)
    )
}