/*
 *     freeDictionaryApp/freeDictionaryApp.search.main
 *     Phonetic.kt Copyrighted by Yamin Siahmargooei at 2025/9/17
 *     Phonetic.kt Last modified at 2025/9/17
 *     This file is part of freeDictionaryApp/freeDictionaryApp.search.main.
 *     Copyright (C) 2025  Yamin Siahmargooei
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

package io.github.yamin8000.owl.search.domain.model

import androidx.compose.runtime.Stable
import kotlin.random.Random
import kotlin.random.nextInt

/**
 * @param audio url to phonetic audio
 */
@Stable
data class Phonetic(
    val text: String? = null,
    val audio: String? = null,
    val sourceUrl: String? = null,
    val license: License? = null,
    val entryId: Long? = null,
    val id: Long? = null
) {
    companion object {
        fun mock() = Phonetic(
            text = listOf("/ˈskɛ.dʒu.əl/", "/ʃɛ.djuːl/").random(),
            audio = "https://api.dictionaryapi.dev/media/pronunciations/en/schedule-au.mp3",
            sourceUrl = "https://creativecommons.org/licenses/by-sa/4.0",
            license = License.mock(),
        )

        fun mockList() = buildList {
            repeat(Random.nextInt(1..4)) {
                add(mock())
            }
        }
    }
}