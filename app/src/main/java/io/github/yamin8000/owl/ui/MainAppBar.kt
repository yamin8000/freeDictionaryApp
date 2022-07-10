/*
 *     Owl: an android app for Owlbot Dictionary API
 *     MainAppBar.kt Created by Yamin Siahmargooei at 2022/7/10
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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.composable.ClickableIcon
import io.github.yamin8000.owl.ui.composable.PersianText

@Preview
@Composable
fun MainTopBar() {
    TopAppBar(
        elevation = 2.dp,
        contentColor = Color.Black
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painterResource(R.drawable.ic_launcher_foreground),
                "Owl"
            )
            PersianText(
                text = stringResource(R.string.app_name),
                fontSize = 16.sp
            )
            Row {
                ClickableIcon(
                    iconPainter = painterResource(id = R.drawable.ic_settings_applications),
                    contentDescription = ""
                ) {

                }
                ClickableIcon(
                    iconPainter = painterResource(id = R.drawable.ic_contact_support),
                    contentDescription = ""
                ) {

                }
            }
        }
    }
}