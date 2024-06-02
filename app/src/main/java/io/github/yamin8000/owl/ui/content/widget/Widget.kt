/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     Widget.kt Copyrighted by Yamin Siahmargooei at 2024/6/2
 *     Widget.kt Last modified at 2024/6/2
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

package io.github.yamin8000.owl.ui.content.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Row
import androidx.glance.text.Text
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.ui.content.MainActivity

internal class Widget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                Row(
                    modifier = GlanceModifier.background(GlanceTheme.colors.secondary).clickable(
                        actionStartActivity<MainActivity>(
                            actionParametersOf(
                                ActionParameters.Key<String>("search") to "test"
                            )
                        )
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    content = {
                        Image(
                            provider = ImageProvider(R.drawable.ic_launcher_foreground),
                            contentDescription = context.getString(R.string.app_name)
                        )
                        Text("Some Text!")
                    }
                )
            }
        }
    }
}