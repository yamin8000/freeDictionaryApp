/*
 *     freeDictionaryApp/freeDictionaryApp.feature_settings.main
 *     TtsEngineDialog.kt Copyrighted by Yamin Siahmargooei at 2026/11/11
 *     TtsEngineDialog.kt Last modified at 2026/11/11
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_settings.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
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

package io.github.yamin8000.owl.feature_settings.ui.components.tts

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.theme.AppTheme
import io.github.yamin8000.owl.common.ui.theme.Sizes

@Composable
internal fun TtsEngineDialog(
    engine: String,
    engines: String,
    language: String,
    languages: String,
    voice: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        content = {
            TtsEngineDialogContent(
                engine = engine,
                engines = engines,
                language = language,
                languages = languages,
                voice = voice
            )
        }
    )
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        TtsEngineDialogContent(
            engine = "engine",
            engines = "engines",
            language = "language",
            languages = "languages",
            voice = "voice"
        )
    }
}

@Composable
private fun TtsEngineDialogContent(
    engine: String,
    engines: String,
    language: String,
    languages: String,
    voice: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        content = {
            AppText(
                modifier = Modifier
                    .padding(Sizes.Large)
                    .verticalScroll(rememberScrollState()),
                text = buildString {
                    appendLine("Default engine:\n$engine")
                    appendLine()
                    appendLine("Engines:\n$engines")
                    appendLine()
                    appendLine("Default language:\n$language")
                    appendLine()
                    appendLine("Default voice:\n$voice")
                    appendLine()
                    appendLine("Languages:\n$languages")
                }
            )
        }
    )
}