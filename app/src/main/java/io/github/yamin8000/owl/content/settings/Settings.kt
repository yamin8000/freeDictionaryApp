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
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.composable.*
import io.github.yamin8000.owl.ui.theme.PreviewTheme
import io.github.yamin8000.owl.util.speak
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    onThemeChanged: (ThemeSetting) -> Unit,
    onBackClick: () -> Unit
) {
    val state = rememberSettingsState()
    val scope = LocalLifecycleOwner.current.lifecycleScope

    var englishLanguages by remember { mutableStateOf(listOf<Locale>()) }
    var textToSpeech: TextToSpeech? by remember { mutableStateOf(null) }

    TtsAwareFeature(
        ttsLanguageLocaleTag = state.ttsLang.value,
        onTtsReady = { tts ->
            textToSpeech = tts
            englishLanguages = tts.availableLanguages.filter {
                it.language == Locale.ENGLISH.language
            }
        }
    )

    ScaffoldWithTitle(
        title = stringResource(id = R.string.settings),
        onBackClick = onBackClick
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                GeneralSettings(
                    isVibrating = state.isVibrating.value,
                    isVibratingChange = { state.scope.launch { state.updateVibrationSetting(it) } }
                )
            }
            item {
                ThemeChanger(state.themeSetting.value) { newTheme ->
                    state.scope.launch { state.updateThemeSetting(newTheme) }
                    onThemeChanged(newTheme)
                }
            }
            item {
                PersianText(
                    text = stringResource(R.string.tts_language),
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            items(englishLanguages) {
                TtsLanguageItem(
                    modifier = Modifier.fillMaxWidth(),
                    localeTag = it.toLanguageTag(),
                    isSelected = it.toLanguageTag() == state.ttsLang.value,
                    onClick = { tag ->
                        scope.launch {
                            state.updateTtsLang(tag)
                            textToSpeech?.speak(Locale.forLanguageTag(tag).displayName)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun GeneralSettings(
    isVibrating: Boolean,
    isVibratingChange: (Boolean) -> Unit
) {
    SettingsItemCard(
        modifier = Modifier.fillMaxWidth(),
        title = stringResource(R.string.general),
        content = {
            SwitchWithText(
                caption = stringResource(R.string.vibrate_on_scroll),
                checked = isVibrating,
                onCheckedChange = isVibratingChange
            )
        }
    )
}

@Composable
fun TtsLanguageItem(
    modifier: Modifier = Modifier,
    localeTag: String,
    isSelected: Boolean = false,
    onClick: (String) -> Unit
) {
    val colors =
        if (isSelected) CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        else CardDefaults.outlinedCardColors()
    OutlinedCard(
        modifier = modifier.clickable(
            interactionSource = MutableInteractionSource(),
            indication = LocalIndication.current,
            onClick = { onClick(localeTag) },
        ),
        colors = colors
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PersianText(
                text = Locale.forLanguageTag(localeTag).displayName,
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
private fun Preview() {
    PreviewTheme {
        SettingsContent(
            onThemeChanged = {},
            onBackClick = {}
        )
    }
}