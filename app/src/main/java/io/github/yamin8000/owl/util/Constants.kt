/*
 *     Owl: an android app for Owlbot Dictionary API
 *     Constants.kt Created by Yamin Siahmargooei at 2022/1/16
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

package io.github.yamin8000.owl.util

object Constants {
    const val WORDS_TEXT_FILE = "words.txt"
    const val LOG_TAG = "<==>"
    const val IMAGE_URL = "image_url"
    const val THEME = "theme"
    const val TTS_LANG = "tts_lang"
    const val DEFAULT_N_GRAM_SIZE = 3

    val NOT_WORD_CHARS_REGEX = Regex("\\W+")

    const val INTERNET_CHECK_DELAY = 3000L
    val DNS_SERVERS = listOf("8.8.8.8", "8.8.4.4", "1.1.1.1", "4.2.2.4")
}