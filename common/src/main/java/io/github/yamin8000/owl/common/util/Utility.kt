/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     Utility.kt Copyrighted by Yamin Siahmargooei at 2024/5/9
 *     Utility.kt Last modified at 2024/3/23
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

package io.github.yamin8000.owl.common.util

import android.util.Log
import androidx.compose.ui.graphics.Color
import io.github.yamin8000.owl.common.BuildConfig
import kotlin.random.Random

/** Prints [message] to logcat if app is in debug build */
fun log(
    message: String
) {
    if (BuildConfig.DEBUG) {
        Log.d(Constants.LOG_TAG, message)
    }
}

fun randomColor(): Color {
    val hue = Random.nextDouble(0.0, 360.0)
    val saturation = Random.nextDouble(50.0, 100.0) / 100f
    val value = Random.nextDouble(75.0, 100.0) / 100f
    val hsv = floatArrayOf(hue.toFloat(), saturation.toFloat(), value.toFloat())
    return Color(android.graphics.Color.HSVToColor(255, hsv))
}