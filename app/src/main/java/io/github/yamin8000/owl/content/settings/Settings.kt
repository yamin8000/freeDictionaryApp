/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     Settings.kt Copyrighted by Yamin Siahmargooei at 2023/8/26
 *     Settings.kt Last modified at 2023/8/26
 *     This file is part of freeDictionaryApp/freeDictionaryApp.app.main.
 *     Copyright (C) 2023  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.content.settings

import android.os.Build
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.DisplaySettings
import androidx.compose.material.icons.twotone.Language
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.composable.PersianText
import io.github.yamin8000.owl.ui.composable.ScaffoldWithTitle
import io.github.yamin8000.owl.ui.composable.SettingsItem
import io.github.yamin8000.owl.ui.composable.SettingsItemCard
import io.github.yamin8000.owl.ui.composable.SwitchItem
import io.github.yamin8000.owl.ui.composable.TtsAwareFeature
import io.github.yamin8000.owl.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.util.speak
import kotlinx.coroutines.launch
import java.util.Locale

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
            val availableLanguages = tts.availableLanguages ?: setOf(Locale.ENGLISH)
            textToSpeech = tts
            englishLanguages = availableLanguages.filter {
                it.language == Locale.ENGLISH.language
            }
        }
    )

    ScaffoldWithTitle(
        title = stringResource(id = R.string.settings),
        onBackClick = onBackClick
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                GeneralSettings(
                    isVibrating = state.isVibrating.value,
                    isVibratingChange = { state.scope.launch { state.updateVibrationSetting(it) } },
                    isStartingBlank = state.isStartingWithBlank.value,
                    isStartingBlankChanged = { state.scope.launch { state.updateStartingBlank(it) } }
                )
                ThemeSetting(state.themeSetting.value) { newTheme ->
                    state.scope.launch { state.updateThemeSetting(newTheme) }
                    onThemeChanged(newTheme)
                }
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
        )
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
            currentTtsTag = currentTtsTag,
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
    currentTtsTag: String,
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
                            localeTag = it.toLanguageTag(),
                            isSelected = it.toLanguageTag() == currentTtsTag
                        ) { tag ->
                            onLanguageSelected(tag)
                            onDismiss()
                        }
                    }
                }
            )
        }
    )
}

@Composable
fun GeneralSettings(
    isVibrating: Boolean,
    isVibratingChange: (Boolean) -> Unit,
    isStartingBlank: Boolean,
    isStartingBlankChanged: (Boolean) -> Unit
) {
    SettingsItemCard(
        modifier = Modifier.fillMaxWidth(),
        title = stringResource(R.string.general),
        content = {
            SwitchItem(
                imageVector = Icons.TwoTone.Language,
                caption = stringResource(R.string.vibrate_on_scroll),
                checked = isVibrating,
                onCheckedChange = isVibratingChange
            )
            SwitchItem(
                imageVector = Icons.TwoTone.Search,
                caption = stringResource(R.string.start_blank_search),
                checked = isStartingBlank,
                onCheckedChange = isStartingBlankChanged
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TtsLanguageItem(
    localeTag: String,
    isSelected: Boolean,
    onClick: ((String) -> Unit)? = null
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = DefaultCutShape,
        onClick = { onClick?.invoke(localeTag) },
        enabled = !isSelected
    ) {
        PersianText(
            text = Locale.forLanguageTag(localeTag).displayName,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun ThemeSetting(
    currentTheme: ThemeSetting,
    onCurrentThemeChange: (ThemeSetting) -> Unit
) {
    var isShowingThemeDialog by remember { mutableStateOf(false) }

    SettingsItemCard(
        title = stringResource(R.string.theme),
        content = {
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
                        contentDescription = stringResource(R.string.theme)
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
    )
}

@Composable
fun ThemeChangerDialog(
    currentTheme: ThemeSetting,
    onCurrentThemeChange: (ThemeSetting) -> Unit,
    onDismiss: () -> Unit
) {
    val themes = remember { ThemeSetting.entries.toTypedArray() }
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
                    .fillMaxWidth(),
                content = {
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
                                ),
                            content = {
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
                        )
                    }
                }
            )
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