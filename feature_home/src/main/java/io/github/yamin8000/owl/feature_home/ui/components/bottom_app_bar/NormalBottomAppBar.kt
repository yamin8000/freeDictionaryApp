/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     NormalBottomAppBar.kt Copyrighted by Yamin Siahmargooei at 2025/1/16
 *     NormalBottomAppBar.kt Last modified at 2025/1/16
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_home.main.
 *     Copyright (C) 2025  Yamin Siahmargooei
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

package io.github.yamin8000.owl.feature_home.ui.components.bottom_app_bar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Clear
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import io.github.yamin8000.owl.common.ui.components.ClickableIcon
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.theme.MyPreview
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.strings.R

@MyPreview
@Composable
private fun Preview() {
    PreviewTheme {
        NormalBottomAppBar(
            onSearch = {},
            onSearchTermChange = {},
            searchTerm = "free"
        )
    }
}

@Composable
internal fun NormalBottomAppBar(
    searchTerm: String,
    onSearchTermChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        modifier = modifier,
        content = {
            TextField(
                singleLine = true,
                shape = CutCornerShape(topEnd = Sizes.Medium, topStart = Sizes.Medium),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Sizes.Large),
                label = {
                    AppText(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.search)
                    )
                },
                placeholder = {
                    AppText(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.search_hint),
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                leadingIcon = {
                    ClickableIcon(
                        enabled = searchTerm.isNotBlank(),
                        imageVector = Icons.TwoTone.Clear,
                        contentDescription = stringResource(R.string.clear),
                        onClick = { onSearchTermChange("") }
                    )
                },
                trailingIcon = {
                    ClickableIcon(
                        enabled = searchTerm.isNotBlank(),
                        imageVector = Icons.TwoTone.Search,
                        contentDescription = stringResource(R.string.search),
                        onClick = onSearch
                    )
                },
                value = searchTerm,
                onValueChange = onSearchTermChange,
                keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search,
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words
                )
            )
        }
    )
}