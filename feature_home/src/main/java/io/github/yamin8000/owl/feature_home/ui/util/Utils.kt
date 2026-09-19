/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     Utils.kt Copyrighted by Yamin Siahmargooei at 2025/1/16
 *     Utils.kt Last modified at 2025/1/16
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_home.main.
 *     Copyright (C) 2025  Yamin Siahmargooei
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

package io.github.yamin8000.owl.feature_home.ui.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import io.github.yamin8000.owl.strings.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

object Utils {

    @Composable
    internal fun <T> ObserverEvent(
        flow: Flow<T>,
        onEvent: suspend (T) -> Unit
    ) {
        val lifeCycleOwner = LocalLifecycleOwner.current
        LaunchedEffect(flow, lifeCycleOwner.lifecycle) {
            lifeCycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                withContext(Dispatchers.Main.immediate) {
                    flow.collect(onEvent)
                }
            }
        }
    }

    internal fun getErrorText(
        context: Context,
        error: HomeError?
    ) = when (error) {
        HomeError.SearchFailed -> context.getString(R.string.general_net_error)
        HomeError.TermIsEmpty -> context.getString(R.string.no_search_term_entered)
        HomeError.NoInternet -> context.getString(R.string.general_net_error)
        HomeError.ApiAuthorizationError -> context.getString(R.string.api_authorization_error)
        HomeError.ApiThrottled -> context.getString(R.string.api_throttled)
        HomeError.Cancelled -> context.getString(R.string.cancelled)
        HomeError.NotFound -> context.getString(R.string.definition_not_found)
        HomeError.Unknown -> context.getString(R.string.untracked_error)
        HomeError.AddedToFavourite -> context.getString(R.string.added_to_favourites)
        HomeError.TtsError -> context.getString(R.string.tts_error)
        null -> ""
    }
}