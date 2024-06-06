/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     Settings.kt Copyrighted by Yamin Siahmargooei at 2024/5/9
 *     Settings.kt Last modified at 2024/5/6
 *     This file is part of freeDictionaryApp/freeDictionaryApp.app.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
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

package io.github.yamin8000.owl.ui.content.settings

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.DisplaySettings
import androidx.compose.material.icons.twotone.Language
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.composable.PersianText
import io.github.yamin8000.owl.ui.composable.ScaffoldWithTitle
import io.github.yamin8000.owl.ui.composable.SettingsItemCard
import io.github.yamin8000.owl.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.ui.theme.MyPreview
import io.github.yamin8000.owl.ui.theme.PreviewTheme
import io.github.yamin8000.owl.util.TTS
import java.util.Locale

@MyPreview
@Composable
private fun SettingsContentPreview() {
    PreviewTheme {
        SettingsContent(
            isVibrating = false,
            onVibratingChange = {},
            isStartingBlank = false,
            onStartingBlankChange = {},
            themeSetting = ThemeSetting.System,
            onThemeSettingChange = {},
            onSystemThemeChange = {},
            ttsTag = "English",
            onTtsTagChange = {},
            onBackClick = {}
        )
    }
}

@Composable
internal fun SettingsContent(
    modifier: Modifier = Modifier,
    isVibrating: Boolean,
    onVibratingChange: (Boolean) -> Unit,
    isStartingBlank: Boolean,
    onStartingBlankChange: (Boolean) -> Unit,
    themeSetting: ThemeSetting,
    onThemeSettingChange: (ThemeSetting) -> Unit,
    onSystemThemeChange: (ThemeSetting) -> Unit,
    ttsTag: String,
    onTtsTagChange: (String) -> Unit,
    onBackClick: () -> Unit
) {
    ScaffoldWithTitle(
        modifier = modifier,
        title = stringResource(id = R.string.settings),
        onBackClick = onBackClick,
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp),
                content = {
                    GeneralSettings(
                        isVibrating = isVibrating,
                        onVibratingChange = onVibratingChange,
                        isStartingBlank = isStartingBlank,
                        onStartingBlankChange = onStartingBlankChange
                    )
                    ThemeSetting(
                        currentTheme = themeSetting,
                        onCurrentThemeChange = { newTheme ->
                            onThemeSettingChange(newTheme)
                            onSystemThemeChange(newTheme)
                        }
                    )
                    TtsLanguageSetting(
                        currentTtsTag = ttsTag,
                        onTtsTagChange = onTtsTagChange
                    )
                }
            )
        }
    )
}

@Composable
private fun TtsLanguageSetting(
    modifier: Modifier = Modifier,
    currentTtsTag: String,
    onTtsTagChange: (String) -> Unit
) {
    SettingsItemCard(
        modifier = modifier,
        title = stringResource(R.string.tts_language),
        content = {
            var isDialogShown by remember { mutableStateOf(false) }
            val showDialog = remember { { isDialogShown = true } }
            SettingsItem(
                onClick = showDialog,
                content = {
                    Icon(imageVector = Icons.TwoTone.Language, contentDescription = null)
                    PersianText(Locale.forLanguageTag(currentTtsTag).displayName)

                    val context = LocalContext.current
                    var englishLanguages by remember { mutableStateOf(listOf<Locale>()) }

                    LaunchedEffect(currentTtsTag) {
                        englishLanguages = TTS(context, Locale.forLanguageTag(currentTtsTag))
                            .getTts()
                            ?.availableLanguages
                            ?.filter { it.language == Locale.ENGLISH.language }
                            ?: listOf()
                    }

                    val hideDialog = remember { { isDialogShown = false } }
                    val onLanguageSelect: (String) -> Unit = remember {
                        {
                            onTtsTagChange(it)
                            isDialogShown = false
                        }
                    }

                    if (isDialogShown) {
                        TtsLanguagesDialog(
                            currentTtsTag = currentTtsTag,
                            languages = englishLanguages,
                            onDismiss = hideDialog,
                            onLanguageSelect = onLanguageSelect
                        )
                    }
                }
            )
        }
    )
}

@Composable
private fun TtsLanguagesDialog(
    modifier: Modifier = Modifier,
    currentTtsTag: String,
    languages: List<Locale>,
    onLanguageSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
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
                            isSelected = it.toLanguageTag() == currentTtsTag,
                            onClick = onLanguageSelect
                        )
                    }
                }
            )
        }
    )
}

@Composable
private fun GeneralSettings(
    modifier: Modifier = Modifier,
    isVibrating: Boolean,
    onVibratingChange: (Boolean) -> Unit,
    isStartingBlank: Boolean,
    onStartingBlankChange: (Boolean) -> Unit
) {
    SettingsItemCard(
        modifier = modifier.fillMaxWidth(),
        title = stringResource(R.string.general),
        content = {
            SwitchItem(
                imageVector = Icons.TwoTone.Language,
                caption = stringResource(R.string.vibrate_on_scroll),
                checked = isVibrating,
                onCheckedChange = onVibratingChange
            )
            SwitchItem(
                imageVector = Icons.TwoTone.Search,
                caption = stringResource(R.string.start_blank_search),
                checked = isStartingBlank,
                onCheckedChange = onStartingBlankChange
            )
        }
    )
}

@Composable
private fun TtsLanguageItem(
    modifier: Modifier = Modifier,
    localeTag: String,
    isSelected: Boolean,
    onClick: (String) -> Unit
) {
    val onItemClick = remember(localeTag) { { onClick(localeTag) } }
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = DefaultCutShape,
        onClick = onItemClick,
        enabled = !isSelected,
        content = {
            PersianText(
                text = Locale.forLanguageTag(localeTag).displayName,
                modifier = Modifier.padding(16.dp)
            )
        }
    )
}

@Composable
private fun ThemeSetting(
    modifier: Modifier = Modifier,
    currentTheme: ThemeSetting,
    onCurrentThemeChange: (ThemeSetting) -> Unit
) {
    var isShowingDialog by remember { mutableStateOf(false) }
    val onDismissDialog = remember { { isShowingDialog = false } }
    val onShowDialog = remember { { isShowingDialog = true } }

    SettingsItemCard(
        modifier = modifier,
        title = stringResource(R.string.theme),
        content = {
            if (isShowingDialog) {
                ThemeChangerDialog(
                    currentTheme = currentTheme,
                    onCurrentThemeChange = onCurrentThemeChange,
                    onDismiss = onDismissDialog
                )
            }
            SettingsItem(
                onClick = onShowDialog,
                content = {
                    Icon(
                        imageVector = Icons.TwoTone.DisplaySettings,
                        contentDescription = stringResource(R.string.theme)
                    )
                    PersianText(
                        text = stringResource(currentTheme.persianNameStringResource),
                    )
                }
            )
            if (currentTheme == ThemeSetting.System && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                DynamicThemeNotice()
        }
    )
}

@Composable
private fun ThemeChangerDialog(
    modifier: Modifier = Modifier,
    currentTheme: ThemeSetting,
    onCurrentThemeChange: (ThemeSetting) -> Unit,
    onDismiss: () -> Unit
) {
    val themes = remember { ThemeSetting.entries.toTypedArray() }
    AlertDialog(
        modifier = modifier,
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
                        val onThemeClick = remember(theme) {
                            {
                                onCurrentThemeChange(theme)
                                onDismiss()
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.Start),
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = theme == currentTheme,
                                    role = Role.RadioButton,
                                    onClick = onThemeClick
                                ),
                            content = {
                                RadioButton(
                                    selected = theme == currentTheme,
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
private fun DynamicThemeNotice(
    modifier: Modifier = Modifier,
) {
    PersianText(
        modifier = modifier,
        text = stringResource(R.string.dynamic_theme_notice),
        textAlign = TextAlign.Justify
    )
}