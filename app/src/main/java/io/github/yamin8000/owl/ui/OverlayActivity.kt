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
import androidx.core.os.bundleOf
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import io.github.yamin8000.owl.feature_overlay.di.OverlayViewModelFactory
import io.github.yamin8000.owl.feature_overlay.ui.OverlayScreen
import io.github.yamin8000.owl.feature_overlay.ui.OverlayWindowViewModel

internal class OverlayActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = handleOutsideInputIntent()

        showContent {
            OverlayScreen(
                onDismissRequest = { finish() },
                navigateToApp = {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtras(bundleOf("Search" to it))
                    startActivity(intent)
                    finish()
                },
                vm = hiltViewModel<OverlayWindowViewModel, OverlayViewModelFactory>(
                    creationCallback = { factory ->
                        factory.create(intentSearch = data)
                    }
                )
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