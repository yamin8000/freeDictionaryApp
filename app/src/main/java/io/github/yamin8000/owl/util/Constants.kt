/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     Constants.kt Copyrighted by Yamin Siahmargooei at 2023/8/26
 *     Constants.kt Last modified at 2023/8/26
 *     This file is part of freeDictionaryApp/freeDictionaryApp.app.main.
 *     Copyright (C) 2023  Yamin Siahmargooei
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

package io.github.yamin8000.owl.util

import io.github.yamin8000.owl.data.db.AppDatabase
import java.util.Locale

object Constants {
    lateinit var db: AppDatabase
    const val LOG_TAG = "<==>"

    const val THEME = "theme"
    const val TTS_LANG = "tts_lang"
    const val IS_VIBRATING = "is_vibrating"
    const val IS_STARTING_BLANK = "is_starting_blank"

    const val DEFAULT_N_GRAM_SIZE = 3

    val NOT_WORD_CHARS_REGEX = Regex("\\W+")

    const val INTERNET_CHECK_DELAY = 3000L
    val DNS_SERVERS = listOf(
        "8.8.8.8",
        "8.8.4.4",
        "1.1.1.1",
        "1.0.0.1",
        "185.51.200.2",
        "178.22.122.100",
        "10.202.10.202",
        "10.202.10.102"
    )

    const val FREE = "free"

    val DEFAULT_LOCALE = Locale.US.toLanguageTag()
}