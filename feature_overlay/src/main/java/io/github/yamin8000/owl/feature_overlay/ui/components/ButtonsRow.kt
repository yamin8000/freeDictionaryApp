/*
 *     freeDictionaryApp/freeDictionaryApp.feature_overlay.main
 *     ButtonsRow.kt Copyrighted by Yamin Siahmargooei at 2025/2/7
 *     ButtonsRow.kt Last modified at 2025/2/7
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_overlay.main.
 *     Copyright (C) 2025  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_overlay.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_overlay.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_overlay.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.strings.R

@Composable
internal fun ButtonsRow(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    navigateToApp: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Sizes.Large, Alignment.CenterHorizontally),
        content = {
            Button(
                onClick = onDismissRequest,
                content = { Text(stringResource(R.string.close)) }
            )
            Button(
                onClick = navigateToApp,
                content = { Text(stringResource(R.string.more_in_app)) }
            )
        }
    )
}