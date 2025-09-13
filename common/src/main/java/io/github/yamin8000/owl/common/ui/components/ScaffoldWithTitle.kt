/*
 *     freeDictionaryApp/freeDictionaryApp.common.main
 *     ScaffoldWithTitle.kt Copyrighted by Yamin Siahmargooei at 2025/2/7
 *     ScaffoldWithTitle.kt Last modified at 2025/2/7
 *     This file is part of freeDictionaryApp/freeDictionaryApp.common.main.
 *     Copyright (C) 2025  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.common.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.common.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.common.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.strings.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWithTitle(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val connection by remember { mutableStateOf(scrollBehavior.nestedScrollConnection) }
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(connection),
        snackbarHost = snackbarHost,
        topBar = {
            Surface(
                shadowElevation = Sizes.Large,
                content = {
                    TopAppBar(
                        scrollBehavior = scrollBehavior,
                        title = {
                            AppText(
                                text = title,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                        },
                        actions = {
                            ClickableIcon(
                                imageVector = Icons.AutoMirrored.TwoTone.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                                onClick = onBackClick
                            )
                        }
                    )
                }
            )
        },
        content = {
            Box(
                content = content,
                modifier = Modifier
                    .padding(it)
                    .padding(
                        start = Sizes.Large,
                        end = Sizes.Large,
                        bottom = 0.dp,
                        top = Sizes.Small
                    )
            )
        }
    )
}