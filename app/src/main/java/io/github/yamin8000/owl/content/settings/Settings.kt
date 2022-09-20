/*
 *     Owl: an android app for Owlbot Dictionary API
 *     Settings.kt Created by Yamin Siahmargooei at 2022/9/20
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

package io.github.yamin8000.owl.content.settings

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.composable.PersianText
import io.github.yamin8000.owl.ui.composable.SurfaceWithTitle
import io.github.yamin8000.owl.ui.util.theme.PreviewTheme
import kotlinx.coroutines.launch

@Composable
fun SettingsContent(
    onThemeChanged: (ThemeSetting) -> Unit
) {
    val settingsState = rememberSettingsState()

    SurfaceWithTitle(
        title = stringResource(id = R.string.settings)
    ) {
        ThemeChanger(settingsState.themeSetting.value) { newTheme ->
            settingsState.coroutineScope.launch {
                settingsState.updateThemeSetting(newTheme)
            }
            onThemeChanged(newTheme)
        }
    }
}

@Composable
fun ThemeChanger(
    currentTheme: ThemeSetting,
    onCurrentThemeChange: (ThemeSetting) -> Unit
) {
    Card(
        shape = CutCornerShape(15.dp)
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PersianText(
                text = stringResource(R.string.theme),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
            val themes = ThemeSetting.values()
            Row(
                modifier = Modifier
                    .selectableGroup()
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                themes.forEach { theme ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier
                            .selectable(
                                selected = (theme == currentTheme),
                                onClick = { onCurrentThemeChange(theme) },
                                role = Role.RadioButton
                            )
                    ) {
                        PersianText(
                            text = theme.persianName,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                        RadioButton(
                            selected = (theme == currentTheme),
                            onClick = null,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun Preview() {
    PreviewTheme { SettingsContent {} }
}