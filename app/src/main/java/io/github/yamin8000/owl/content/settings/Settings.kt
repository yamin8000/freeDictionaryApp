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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.DisplaySettings
import androidx.compose.material.icons.twotone.Language
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
import androidx.lifecycle.lifecycleScope
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.composable.*
import io.github.yamin8000.owl.ui.theme.DefaultCutShape
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
                ThemeSetting(state.themeSetting.value) { newTheme ->
                    state.scope.launch { state.updateThemeSetting(newTheme) }
                    onThemeChanged(newTheme)
                }
            }
            item {
                TtsLanguageSetting(
                    languages = englishLanguages,
                    currentTtsTag = state.ttsLang.value,
                    onTtsTagChanged = { tag ->
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
fun TtsLanguageSetting(
    currentTtsTag: String,
    languages: List<Locale>,
    onTtsTagChanged: (String) -> Unit
) {
    var isDialogShown by remember { mutableStateOf(false) }

    if (isDialogShown) {
        TtsLanguagesDialog(
            languages = languages,
            onLanguageSelected = onTtsTagChanged,
            onDismiss = { isDialogShown = false }
        )
    }

    SettingsItemCard(
        title = stringResource(R.string.tts_language),
        content = {
            SettingsItem(
                onClick = { isDialogShown = true },
                content = {
                    Icon(imageVector = Icons.TwoTone.Language, contentDescription = null)
                    PersianText(Locale.forLanguageTag(currentTtsTag).displayName)
                }
            )
        }
    )
}

@Composable
fun TtsLanguagesDialog(
    languages: List<Locale>,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { PersianText(stringResource(R.string.tts_language)) },
        icon = { Icon(imageVector = Icons.TwoTone.Language, contentDescription = null) },
        confirmButton = {/*ignored*/ },
        text = {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    items(languages) {
                        TtsLanguageItem(
                            modifier = Modifier.fillMaxWidth(),
                            localeTag = it.toLanguageTag(),
                            onClick = { tag ->
                                onLanguageSelected(tag)
                                onDismiss()
                            }
                        )
                    }
                }
            )
        }
    )
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.TwoTone.Language, contentDescription = null)
                SwitchWithText(
                    caption = stringResource(R.string.vibrate_on_scroll),
                    checked = isVibrating,
                    onCheckedChange = isVibratingChange
                )
            }
        }
    )
}

@Composable
fun TtsLanguageItem(
    modifier: Modifier = Modifier,
    localeTag: String,
    onClick: ((String) -> Unit)? = null
) {
    OutlinedCard(
        shape = DefaultCutShape,
        modifier = modifier.clickable(
            interactionSource = MutableInteractionSource(),
            indication = LocalIndication.current,
            onClick = { onClick?.invoke(localeTag) },
        )
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
fun ThemeSetting(
    currentTheme: ThemeSetting,
    onCurrentThemeChange: (ThemeSetting) -> Unit
) {
    var isShowingThemeDialog by remember { mutableStateOf(false) }

    SettingsItemCard(
        title = stringResource(R.string.theme)
    ) {
        if (isShowingThemeDialog) {
            ThemeChangerDialog(
                currentTheme = currentTheme,
                onCurrentThemeChange = onCurrentThemeChange,
                onDismiss = { isShowingThemeDialog = false }
            )
        }
        SettingsItem(
            onClick = { isShowingThemeDialog = true },
            content = {
                Icon(
                    imageVector = Icons.TwoTone.DisplaySettings,
                    contentDescription = ""
                )
                PersianText(
                    text = stringResource(currentTheme.persianNameStringResource),
                    modifier = Modifier.padding()
                )
            }
        )
        if (currentTheme == ThemeSetting.System && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            DynamicThemeNotice()
    }
}

@Composable
fun ThemeChangerDialog(
    currentTheme: ThemeSetting,
    onCurrentThemeChange: (ThemeSetting) -> Unit,
    onDismiss: () -> Unit
) {
    val themes = remember { ThemeSetting.values() }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { /*ignored*/ },
        title = { PersianText(stringResource(R.string.theme)) },
        icon = { Icon(imageVector = Icons.TwoTone.DisplaySettings, contentDescription = null) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(16.dp)
                    .selectableGroup()
                    .fillMaxWidth()
            ) {
                themes.forEach { theme ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.Start),
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (theme == currentTheme),
                                role = Role.RadioButton,
                                onClick = {
                                    onCurrentThemeChange(theme)
                                    onDismiss()
                                }
                            )
                    ) {
                        RadioButton(
                            selected = (theme == currentTheme),
                            onClick = null,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        PersianText(
                            text = stringResource(theme.persianNameStringResource),
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }
            }
        }
    )
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