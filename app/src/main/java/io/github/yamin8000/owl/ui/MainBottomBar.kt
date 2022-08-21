/*
 *     Owl: an android app for Owlbot Dictionary API
 *     MainBottomBar.kt Created by Yamin Siahmargooei at 2022/7/10
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.composable.PersianText
import io.github.yamin8000.owl.ui.theme.Samim

data class MainBottomBarCallbacks(
    val onSearch: (String) -> Unit,
    val onTextChanged: (String) -> Unit
)

@Composable
fun MainBottomBar(
    params: MainBottomBarCallbacks
) {
    GenericMainBottomBar(params)
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun GenericMainBottomBar(
    params: MainBottomBarCallbacks? = null,
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PersianText(
            stringResource(id = R.string.search_hint),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        var searchText by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                PersianText(
                    stringResource(R.string.search),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            value = searchText,
            singleLine = true,
            onValueChange = {
                searchText = it
                params?.onTextChanged?.invoke(searchText)
            },
            textStyle = TextStyle(
                fontFamily = Samim,
                textAlign = TextAlign.Right,
                textDirection = TextDirection.Rtl
            ),
            keyboardActions = KeyboardActions(onSearch = {
                params?.onSearch?.invoke(searchText)
            }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
        )
    }
}