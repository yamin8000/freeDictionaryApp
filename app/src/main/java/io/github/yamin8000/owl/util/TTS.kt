/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     TTS.kt Copyrighted by Yamin Siahmargooei at 2024/5/9
 *     TTS.kt Last modified at 2024/3/23
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

package io.github.yamin8000.owl.util

import android.content.Context
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

private var tts: TextToSpeech? = null

/**
 * A helper/wrapper for [TextToSpeech]
 */
class TTS(
    private val context: Context,
    private val locale: Locale = Locale.US,
) {
    private var ttsLang: Int = 0

    /**
     * This method wraps around classic [TextToSpeech.OnInitListener] callback method
     * and suspends it result with a [CancellableContinuation] coroutine
     */
    suspend fun getTts(): TextToSpeech? {
        return suspendCancellableCoroutine { continuation ->
            if (tts == null) {
                tts = TextToSpeech(context) {
                    if (it == TextToSpeech.SUCCESS) {
                        ttsLang = tts?.setLanguage(locale) ?: 0
                        if (ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                            continuation.resume(null)
                        } else continuation.resume(tts)
                    } else continuation.resume(null)
                }
            } else continuation.resume(tts)
        }
    }
}