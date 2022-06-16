/*
 *     Owl: an android app for Owlbot Dictionary API
 *     MainActivity.kt Created by Yamin Siahmargooei at 2022/6/16
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

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.theme.OwlTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainContent() }
    }

    @Preview(showBackground = true)
    @Composable
    private fun MainContent() {
        OwlTheme {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                color = MaterialTheme.colors.background,
            ) {
                var searchText by remember { mutableStateOf("") }

                Column() {
                    TextField(
                        label = {
                            Text(
                                stringResource(R.string.search),
                                textAlign = TextAlign.Right,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        value = searchText,
                        singleLine = true,
                        onValueChange = {
                            searchText = it
                        })
                }
            }
        }
    }
}