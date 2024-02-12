/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     Utility.kt Copyrighted by Yamin Siahmargooei at 2023/8/26
 *     Utility.kt Last modified at 2023/8/26
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

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.InitializerViewModelFactoryBuilder
import io.github.yamin8000.owl.BuildConfig
import java.util.Locale

/**
 * Finds current activity instance or null
 */
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

/**
 * Returns current [Locale] of the device
 */
@Suppress("DEPRECATION")
fun getCurrentLocale(
    context: Context
): Locale {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.resources.configuration.locales.get(0)
    } else context.resources.configuration.locale
}

/**
 * Extension method for [TextToSpeech] that
 * calls [TextToSpeech.speak] with some predefined parameters
 */
fun TextToSpeech.speak(
    text: String
) {
    speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
}

/**
 * Prints [message] to logcat if app is in debug build
 */
fun log(
    message: String
) {
    if (BuildConfig.DEBUG)
        Log.d(Constants.LOG_TAG, message)
}

/**
 * This method sanitize words from the given [data] set
 * by removing unnecessary characters like white spaces and numbers and etc and
 * making them lowercase and filtering out blank entries
 */
fun sanitizeWords(
    data: Set<String>
): Set<String> = data.asSequence()
    .map { it.replace(Constants.NOT_WORD_CHARS_REGEX, "") }
    .map { it.lowercase() }
    .filter { it.isNotBlank() }
    .toSet()

/**
 * General Purpose factory for [androidx.lifecycle.ViewModel]
 */
inline fun viewModelFactory(
    builder: InitializerViewModelFactoryBuilder.() -> Unit
): ViewModelProvider.Factory = InitializerViewModelFactoryBuilder().apply(builder).build()