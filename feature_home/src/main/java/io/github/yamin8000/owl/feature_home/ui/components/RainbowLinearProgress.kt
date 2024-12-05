/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     RainbowLinearProgress.kt Copyrighted by Yamin Siahmargooei at 2024/12/5
 *     RainbowLinearProgress.kt Last modified at 2024/12/5
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_home.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_home.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
internal fun RainbowLinearProgress(
    modifier: Modifier = Modifier
) {
    fun randomBeam(): Int = (16..255).random()
    val colors = buildList {
        repeat((5..20).random()) {
            add(androidx.compose.ui.graphics.Color(randomBeam(), randomBeam(), randomBeam()))
        }
    }
    var color by remember { mutableStateOf(colors.first()) }
    LaunchedEffect(Unit) {
        while (true) {
            color = colors.random()
            delay(250)
        }
    }
    LinearProgressIndicator(
        modifier = modifier.fillMaxWidth(),
        color = color
    )
}