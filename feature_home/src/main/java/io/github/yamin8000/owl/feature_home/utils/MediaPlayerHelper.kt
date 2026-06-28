/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     MediaPlayerHelper.kt Copyrighted by Yamin Siahmargooei at 2026/6/27
 *     MediaPlayerHelper.kt Last modified at 2026/6/27
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_home.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
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

package io.github.yamin8000.owl.feature_home.utils

import android.media.MediaPlayer
import io.github.yamin8000.owl.common.util.log

class MediaPlayerHelper {
    private var player: MediaPlayer? = MediaPlayer()

    fun playFromUrl(
        audioUrl: String
    ) {
        try {
            if (player == null) {
                player = MediaPlayer()
            }
            player?.setDataSource(audioUrl)
            player?.prepareAsync()
            player?.setOnPreparedListener { player?.start() }
            player?.setOnCompletionListener {
                player?.reset()
                player?.release()
                player = null
            }
        } catch (e: Exception) {
            log(e.stackTraceToString())
            player?.reset()
            player?.release()
            player = null
        }
    }
}