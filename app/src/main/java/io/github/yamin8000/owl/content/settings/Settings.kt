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
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.composable.PersianText
import io.github.yamin8000.owl.ui.composable.SettingsItemCard
import io.github.yamin8000.owl.ui.composable.SurfaceWithTitle
import io.github.yamin8000.owl.ui.composable.TtsAwareComposable
import io.github.yamin8000.owl.ui.theme.PreviewTheme
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun SettingsContent(
    onThemeChanged: (ThemeSetting) -> Unit
) {
    val settingsState = rememberSettingsState()

    SurfaceWithTitle(
        title = stringResource(id = R.string.settings)
    ) {
        ThemeChanger(settingsState.themeSetting.value) { newTheme ->
            settingsState.coroutineScope.launch { settingsState.updateThemeSetting(newTheme) }
            onThemeChanged(newTheme)
        }

        TtsLanguagesCard(
            currentLanguage = Locale.US,
            onLanguageChange = {

            }
        )
    }
}

@Composable
fun TtsLanguagesCard(
    currentLanguage: Locale?,
    onLanguageChange: (Locale) -> Unit
) {
    TtsAwareComposable(
        content = { tts ->
            SettingsItemCard(
                columnModifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.tts_language)
            ) {
                if (tts.availableLanguages?.isEmpty() != true && currentLanguage != null) {

                    val longestText = tts.availableLanguages.map { it.displayName }
                        .maxBy { it.length }

                    SubcomposeLayout { constraints ->
                        val width = constraints.maxWidth / 2

                        val height = subcompose("viewToMeasure") {
                            TtsLanguageItem(
                                displayName = longestText,
                                modifier = Modifier.width(width.toDp())
                            )
                        }.first().measure(Constraints()).height.toDp()

                        val content = subcompose("content") {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                content = {
                                    items(tts.availableLanguages.toList()) { item ->
                                        TtsLanguageItem(
                                            displayName = item.displayName,
                                            modifier = Modifier.requiredHeight(height * 1.25f)
                                        )
                                    }
                                }
                            )
                        }.first().measure(constraints)
                        layout(content.width, content.height) {
                            content.place(0, 0)
                        }
                    }
                } else CircularProgressIndicator()
            }
        })
}

@Composable
fun TtsLanguageItem(
    modifier: Modifier = Modifier,
    displayName: String
) {
    OutlinedCard(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            //modifier = Modifier.aspectRatio(1f)
        ) {
            Text(
                text = displayName,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun ThemeChanger(
    currentTheme: ThemeSetting,
    onCurrentThemeChange: (ThemeSetting) -> Unit
) {
    SettingsItemCard(
        title = stringResource(R.string.theme)
    ) {
        val themes = ThemeSetting.values()
        Row(
            modifier = Modifier
                .selectableGroup()
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
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
                        text = stringResource(theme.persianNameStringResource),
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    RadioButton(
                        selected = (theme == currentTheme),
                        onClick = null,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        if (currentTheme == ThemeSetting.System && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            DynamicThemeNotice()
    }
}

@Composable
fun DynamicThemeNotice() {
    PersianText(
        text = stringResource(R.string.dynamic_theme_notice),
        textAlign = TextAlign.Justify
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun LanguageItemPreview() {
    PreviewTheme { TtsLanguageItem(displayName = Locale.US.displayName) }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun Preview() {
    PreviewTheme { SettingsContent {} }
}