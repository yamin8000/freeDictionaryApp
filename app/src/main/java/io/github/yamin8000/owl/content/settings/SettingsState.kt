/*
 *     Owl: an android app for Owlbot Dictionary API
 *     SettingsState.kt Created by Yamin Siahmargooei at 2022/9/20
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

package io.github.yamin8000.owl.content.settings

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import io.github.yamin8000.owl.content.settings
import io.github.yamin8000.owl.util.Constants
import io.github.yamin8000.owl.util.DataStoreHelper
import kotlinx.coroutines.launch

class SettingsState(
    context: Context,
    val coroutineScope: LifecycleCoroutineScope,
    val themeSetting: MutableState<ThemeSetting>
) {
    private val dataStore = DataStoreHelper(context.settings)

    init {
        coroutineScope.launch {
            themeSetting.value = ThemeSetting.valueOf(
                dataStore.getString(Constants.theme) ?: ThemeSetting.System.name
            )
        }
    }

    suspend fun updateThemeSetting(
        newTheme: ThemeSetting
    ) {
        themeSetting.value = newTheme
        coroutineScope.launch {
            dataStore.setString(Constants.theme, newTheme.name)
        }
    }
}

@Composable
fun rememberSettingsState(
    context: Context = LocalContext.current,
    coroutineScope: LifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope,
    themeSetting: MutableState<ThemeSetting> = rememberSaveable { mutableStateOf(ThemeSetting.System) }
) = remember(context, themeSetting, coroutineScope) {
    SettingsState(context, coroutineScope, themeSetting)
}