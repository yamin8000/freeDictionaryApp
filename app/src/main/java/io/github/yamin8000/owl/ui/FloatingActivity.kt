/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     FloatingActivity.kt Copyrighted by Yamin Siahmargooei at 2024/9/5
 *     FloatingActivity.kt Last modified at 2024/9/5
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

package io.github.yamin8000.owl.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

internal class FloatingActivity : BaseActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = handleOutsideInputIntent()

        showContent {
            val configuration = LocalConfiguration.current
            val screenHeight = configuration.screenHeightDp.dp
            BasicAlertDialog(
                onDismissRequest = { finish() },
                properties = DialogProperties(
                    dismissOnClickOutside = false,
                    usePlatformDefaultWidth = false
                ),
                content = {
                    Surface(
                        modifier = Modifier
                            .size(screenHeight / 2)
                            .padding(horizontal = 10.dp),
                        content = {
                            Text("is this what you want? $data")
                        }
                    )
                }
            )
        }
    }

    private fun handleOutsideInputIntent(): String? {
        return if (intent.type == "text/plain") {
            when (intent.action) {
                Intent.ACTION_TRANSLATE, Intent.ACTION_DEFINE, Intent.ACTION_SEND -> {
                    intent.getStringExtra(Intent.EXTRA_TEXT)
                }

                Intent.ACTION_PROCESS_TEXT -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT)
                    else null
                }

                else -> {
                    null
                }
            }
        } else {
            null
        }
    }
}