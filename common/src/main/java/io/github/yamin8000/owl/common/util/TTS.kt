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

package io.github.yamin8000.owl.common.util

import android.content.Context
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

/** A helper/wrapper for [TextToSpeech] */
class TTS(
    private val context: Context,
    private val languageTag: String
) {
    private var tts: TextToSpeech? = null

    suspend fun createEngine(
        tag: String = languageTag
    ): TextToSpeech? = suspendCancellableCoroutine { continuation ->
        tts = TextToSpeech(context) {
            if (it == TextToSpeech.SUCCESS) {
                tts?.language = Locale.forLanguageTag(tag)
                log("TTS init success!")
                continuation.resume(tts)
            } else {
                log("TTS init failed!")
                continuation.resume(null)
            }
        }
    }

    /**
     * Extension method for [TextToSpeech] that calls [TextToSpeech.speak] with
     * some predefined parameters
     */
    suspend fun speak(text: String) {
        if (tts == null) {
            tts = createEngine()
        }

        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    suspend fun languages(): List<Locale> {
        if (tts == null) {
            tts = createEngine()
        }

        return tts?.availableLanguages?.filterNotNull()?.sortedBy { it.displayName } ?: listOf()
    }
}