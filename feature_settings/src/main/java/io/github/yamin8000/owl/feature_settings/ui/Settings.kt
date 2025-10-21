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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.yamin8000.owl.common.ui.components.ScaffoldWithTitle
import io.github.yamin8000.owl.common.ui.theme.PreviewTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.datastore.domain.model.ThemeType
import io.github.yamin8000.owl.feature_settings.ui.components.GeneralSettings
import io.github.yamin8000.owl.feature_settings.ui.components.theme.ThemeSetting
import io.github.yamin8000.owl.feature_settings.ui.components.tts.TtsLanguageSetting
import io.github.yamin8000.owl.strings.R
import java.util.Locale
import kotlin.random.Random

@PreviewScreenSizes
@PreviewFontScale
@Composable
private fun Preview() {
    PreviewTheme {
        SettingsContent(
            isVibrating = Random.nextBoolean(),
            onVibrationChange = {},
            isStartingBlank = Random.nextBoolean(),
            onStartingBlankChange = {},
            theme = ThemeType.entries().random(),
            onUpdateTheme = {},
            ttsLang = "en-us",
            onTtsLangChange = {},
            englishLanguages = listOf(),
            onThemeChanged = {},
            onBackClick = {}
        )
    }
}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    vm: SettingsViewModel = hiltViewModel(),
    onThemeChanged: (ThemeType) -> Unit,
    onBackClick: () -> Unit
) {
    val state = vm.state.collectAsStateWithLifecycle().value

    SettingsContent(
        modifier = modifier,
        isVibrating = state.isVibrating,
        onVibrationChange = { vm.onEvent(SettingsEvent.UpdateVibrationState(it)) },
        isStartingBlank = state.isStartingBlank,
        onStartingBlankChange = { vm.onEvent(SettingsEvent.UpdateStartingBlankState(it)) },
        theme = state.theme,
        onUpdateTheme = { vm.onEvent(SettingsEvent.UpdateTheme(it)) },
        ttsLang = state.ttsLang,
        onTtsLangChange = { vm.onEvent(SettingsEvent.UpdateTtsLangState(it)) },
        englishLanguages = state.englishLanguages,
        onThemeChanged = onThemeChanged,
        onBackClick = onBackClick
    )
}

@Composable
internal fun SettingsContent(
    isVibrating: Boolean,
    onVibrationChange: (Boolean) -> Unit,
    isStartingBlank: Boolean,
    onStartingBlankChange: (Boolean) -> Unit,
    theme: ThemeType,
    onUpdateTheme: (ThemeType) -> Unit,
    ttsLang: String,
    onTtsLangChange: (String) -> Unit,
    englishLanguages: List<Locale>,
    onThemeChanged: (ThemeType) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScaffoldWithTitle(
        modifier = modifier,
        title = stringResource(R.string.settings),
        onBackClick = onBackClick,
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    Sizes.Medium,
                    Alignment.CenterVertically
                ),
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = Sizes.Large),
                content = {
                    GeneralSettings(
                        isVibrating = isVibrating,
                        onVibratingChange = onVibrationChange,
                        isStartingBlank = isStartingBlank,
                        onStartingBlankChange = onStartingBlankChange
                    )
                    ThemeSetting(
                        theme = theme,
                        onThemeChanged = { newTheme ->
                            onUpdateTheme(newTheme)
                            onThemeChanged(newTheme)
                        }
                    )
                    TtsLanguageSetting(
                        currentTtsTag = ttsLang,
                        languages = englishLanguages,
                        onTtsTagChange = onTtsLangChange
                    )
                }
            )
        }
    )
}