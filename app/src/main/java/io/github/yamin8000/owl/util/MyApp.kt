/*
 *     Owl: an android app for Owlbot Dictionary API
 *     MyApp.kt Created by Yamin Siahmargooei at 2022/1/16
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

@file:Suppress("unused")

package io.github.yamin8000.owl.util

import android.app.Application
import android.util.Log
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import io.github.yamin8000.owl.BuildConfig
import io.github.yamin8000.owl.util.Constants.LOG_TAG

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        try {
            if (BuildConfig.DEBUG)
                prepareLogger()
        } catch (e: Exception) {
            Log.d(LOG_TAG, "Application Failed!")
            if (BuildConfig.DEBUG)
                Log.d(LOG_TAG, e.stackTraceToString())
        }
    }

    private fun prepareLogger() {
        Logger.addLogAdapter(
            AndroidLogAdapter(
                PrettyFormatStrategy.newBuilder().tag(LOG_TAG).build()
            )
        )
        Logger.d("Application is Started!")
    }
}