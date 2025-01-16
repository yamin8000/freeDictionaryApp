/*
 *     freeDictionaryApp/freeDictionaryApp.feature_settings.main
 *     Settings.kt Copyrighted by Yamin Siahmargooei at 2024/8/19
 *     Settings.kt Last modified at 2024/8/18
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_settings.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_settings.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_settings.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_settings.ui

import android.content.Context
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.yamin8000.owl.common.ui.components.PersianText
import io.github.yamin8000.owl.common.ui.components.ScaffoldWithTitle
import io.github.yamin8000.owl.common.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.datastore.domain.model.ThemeType
import io.github.yamin8000.owl.feature_settings.ui.components.SettingsItem
import io.github.yamin8000.owl.feature_settings.ui.components.SettingsItemCard
import io.github.yamin8000.owl.feature_settings.ui.components.SwitchItem
import io.github.yamin8000.owl.strings.R
import java.util.Locale

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    vm: SettingsViewModel = hiltViewModel(),
    onThemeChanged: (ThemeType) -> Unit,
    onBackClick: () -> Unit
) {
    val state = vm.state.collectAsStateWithLifecycle().value
    ScaffoldWithTitle(
        modifier = modifier,
        title = stringResource(id = R.string.settings),
        onBackClick = onBackClick,
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Sizes.Medium),
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = Sizes.Large),
                content = {
                    GeneralSettings(
                        isVibrating = state.isVibrating,
                        onVibratingChange = { vm.onEvent(SettingsEvent.UpdateVibrationState(it)) },
                        isStartingBlank = state.isStartingBlank,
                        onStartingBlankChange = {
                            vm.onEvent(SettingsEvent.UpdateStartingBlankState(it))
                        }
                    )
                    ThemeSetting(
                        theme = state.theme,
                        onThemeChanged = { newTheme ->
                            vm.onEvent(SettingsEvent.UpdateTheme(newTheme))
                            onThemeChanged(newTheme)
                        }
                    )
                    TtsLanguageSetting(
                        currentTtsTag = state.ttsLang,
                        languages = state.englishLanguages,
                        onTtsTagChange = { vm.onEvent(SettingsEvent.UpdateTtsLangState(it)) }
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
    languages: List<Locale>,
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
                    PersianText(
                        Locale.forLanguageTag(
                            currentTtsTag
                        ).displayName
                    )

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
                            languages = languages,
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
        //title = { PersianText(stringResource(R.string.tts_language)) },
        icon = { Icon(imageVector = Icons.TwoTone.Language, contentDescription = null) },
        confirmButton = {/*ignored*/ },
        text = {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Sizes.Medium),
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
                modifier = Modifier.padding(Sizes.Large)
            )
        }
    )
}

@Composable
private fun ThemeSetting(
    modifier: Modifier = Modifier,
    theme: ThemeType,
    onThemeChanged: (ThemeType) -> Unit
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
                    currentTheme = theme,
                    onCurrentThemeChange = onThemeChanged,
                    onDismiss = onDismissDialog
                )
            }
            SettingsItem(
                onClick = onShowDialog,
                content = {
                    val context = LocalContext.current
                    Icon(
                        imageVector = Icons.TwoTone.DisplaySettings,
                        contentDescription = stringResource(R.string.theme)
                    )
                    PersianText(text = theme.toStringResource(context))
                }
            )
            if (theme == ThemeType.System && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                DynamicThemeNotice()
        }
    )
}

@Composable
private fun ThemeChangerDialog(
    modifier: Modifier = Modifier,
    currentTheme: ThemeType,
    onCurrentThemeChange: (ThemeType) -> Unit,
    onDismiss: () -> Unit
) {
    val themes = remember { ThemeType.entries() }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { PersianText(stringResource(R.string.theme)) },
        icon = { Icon(imageVector = Icons.TwoTone.DisplaySettings, contentDescription = null) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(Sizes.Large)
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
                                val context = LocalContext.current
                                RadioButton(
                                    selected = theme == currentTheme,
                                    onClick = null,
                                    modifier = Modifier.padding(start = Sizes.Medium)
                                )
                                PersianText(
                                    text = theme.toStringResource(context),
                                    modifier = Modifier.padding(vertical = Sizes.Large)
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

private fun ThemeType.toStringResource(
    context: Context
) = when (this) {
    ThemeType.Dark -> context.getString(R.string.theme_dark)
    ThemeType.Darker -> context.getString(R.string.theme_oled)
    ThemeType.Light -> context.getString(R.string.theme_light)
    ThemeType.System -> context.getString(R.string.theme_system)
}