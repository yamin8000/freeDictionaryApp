/*
 *     freeDictionaryApp/freeDictionaryApp.common.main
 *     TTS.kt Copyrighted by Yamin Siahmargooei at 2024/8/20
 *     TTS.kt Last modified at 2024/8/20
 *     This file is part of freeDictionaryApp/freeDictionaryApp.common.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.common.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.common.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.common.ui.util

import android.content.Context
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

/** A helper/wrapper for [TextToSpeech] */
class TTS(
    private val context: Context,
    private val languageTag: String
) {
    private var tts: TextToSpeech? = null

    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        scope.launch {
            createEngine()
        }
    }

    fun createEngine(tag: String = languageTag) {
        tts = TextToSpeech(context) {
            if (it == TextToSpeech.SUCCESS) {
                tts?.setLanguage(Locale.forLanguageTag(tag))
            }
        }
    }

    /**
     * Extension method for [TextToSpeech] that calls [TextToSpeech.speak] with
     * some predefined parameters
     */
    fun speak(text: String) {
        val current = this.languageTag
        println(current)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun englishLanguages() = tts?.availableLanguages
        ?.filter { it.language == Locale.ENGLISH.language }
        ?: listOf()
}