/*
 *     Owl: an android app for Owlbot Dictionary API
 *     TtsEngine.kt Created by Yamin Siahmargooei at 2022/1/21
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

package io.github.yamin8000.owl.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.widget.Toast
import com.orhanobut.logger.Logger
import java.util.*

class TtsEngine(
    private val context: Context,
    private val speakText: String = " "
) {

    private lateinit var tts: TextToSpeech
    private var ttsLang: Int = 0

    private var isTtsReady = true

    init {
        tts = TextToSpeech(context) {
            if (it == TextToSpeech.SUCCESS) {
                Logger.d("TTS init SUCCESS")
                ttsLang = tts.setLanguage(Locale.US)
                if (ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                    languageNotSupported()
                    isTtsReady = false
                }
            } else {
                Logger.d("TTS init Failed!")
                isTtsReady = false
                //toast(context.getString(R.string.tts_init_failed))
            }
            if (isTtsReady && speakText != "") speak(speakText)
        }
    }

    fun speak(text: String) {
        if (isTtsReady) {
            val result = tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            if (result == TextToSpeech.ERROR) ttsError()
        } else ttsNotReady()
    }

    private fun ttsNotReady() {
        //toast(context.getString(R.string.tts_not_ready))
    }

    private fun languageNotSupported() {
        //toast(context.getString(R.string.tts_english_not_supported))
    }

    private fun ttsError() {
        //toast(context.getString(R.string.tts_failed))
    }

    private fun toast(
        text: String,
        length: Int = Toast.LENGTH_SHORT
    ) {
        Toast.makeText(context, text, length).show()
    }
}