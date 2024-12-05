/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     NormalBottomAppBar.kt Copyrighted by Yamin Siahmargooei at 2024/12/5
 *     NormalBottomAppBar.kt Last modified at 2024/12/5
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_home.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
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

package io.github.yamin8000.owl.feature_home.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Clear
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.yamin8000.owl.common.ui.components.ClickableIcon
import io.github.yamin8000.owl.common.ui.components.PersianText
import io.github.yamin8000.owl.strings.R

@Composable
internal fun NormalBottomAppBar(
    modifier: Modifier = Modifier,
    onSearch: () -> Unit,
    onSearchTermChange: (String) -> Unit,
    searchTerm: String
) {
    BottomAppBar(
        modifier = modifier,
        content = {
            val onSearchClick = remember { onSearch }
            val onTermChanged: (String) -> Unit = remember {
                onSearchTermChange
            }
            TextField(
                singleLine = true,
                shape = CutCornerShape(topEnd = 8.dp, topStart = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                label = {
                    PersianText(
                        text = stringResource(R.string.search),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                placeholder = {
                    PersianText(
                        text = stringResource(R.string.search_hint),
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 12.sp
                    )
                },
                leadingIcon = {
                    val onClearClick = remember { { onSearchTermChange("") } }
                    ClickableIcon(
                        enabled = searchTerm.isNotBlank(),
                        imageVector = Icons.TwoTone.Clear,
                        contentDescription = stringResource(R.string.clear),
                        onClick = onClearClick
                    )
                },
                trailingIcon = {
                    ClickableIcon(
                        enabled = searchTerm.isNotBlank(),
                        imageVector = Icons.TwoTone.Search,
                        contentDescription = stringResource(R.string.search),
                        onClick = onSearchClick
                    )
                },
                value = searchTerm,
                onValueChange = onTermChanged,
                keyboardActions = KeyboardActions(onSearch = { onSearchClick() }),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search,
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words
                )
            )
        }
    )
}