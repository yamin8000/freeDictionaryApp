/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     RandomWordWidget.kt Copyrighted by Yamin Siahmargooei at 2024/9/26
 *     RandomWordWidget.kt Last modified at 2024/9/26
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

package io.github.yamin8000.owl.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.material3.ColorProviders
import androidx.glance.text.Text
import io.github.yamin8000.owl.common.ui.theme.darkColors
import io.github.yamin8000.owl.common.ui.theme.lightColors

class RandomWordWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme(
                colors = ColorProviders(
                    light = lightColors,
                    dark = darkColors
                ),
                content = {
                    Text("Hello There!")
                }
            )
        }
    }
}