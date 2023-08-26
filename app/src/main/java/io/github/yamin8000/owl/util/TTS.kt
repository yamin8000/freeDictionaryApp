/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     TTS.kt Copyrighted by Yamin Siahmargooei at 2023/8/26
 *     TTS.kt Last modified at 2023/8/26
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

package io.github.yamin8000.owl.util

import android.content.Context
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.resume

class TTS(
    private val context: Context,
    private val locale: Locale = Locale.US,
) {
    private lateinit var tts: TextToSpeech
    private var ttsLang: Int = 0

    suspend fun getTts(): TextToSpeech? {
        return suspendCancellableCoroutine { continuation ->
            tts = TextToSpeech(context) {
                if (it == TextToSpeech.SUCCESS) {
                    ttsLang = tts.setLanguage(locale)
                    if (ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        continuation.resume(null)
                    } else continuation.resume(tts)
                } else continuation.resume(null)
            }
        }
    }
}