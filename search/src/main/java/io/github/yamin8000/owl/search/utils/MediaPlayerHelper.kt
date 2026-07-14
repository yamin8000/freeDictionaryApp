/*
 *     freeDictionaryApp/freeDictionaryApp.search.main
 *     MediaPlayerHelper.kt Copyrighted by Yamin Siahmargooei at 2026/7/14
 *     MediaPlayerHelper.kt Last modified at 2026/7/14
 *     This file is part of freeDictionaryApp/freeDictionaryApp.search.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.search.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.search.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.search.utils

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