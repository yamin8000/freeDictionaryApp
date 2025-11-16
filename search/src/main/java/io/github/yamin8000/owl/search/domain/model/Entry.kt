/*
 *     freeDictionaryApp/freeDictionaryApp.search.main
 *     Entry.kt Copyrighted by Yamin Siahmargooei at 2025/9/17
 *     Entry.kt Last modified at 2025/9/17
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

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class Entry(
    val word: String,
    val phonetics: ImmutableList<Phonetic>,
    val meanings: ImmutableList<Meaning>,
    val license: License? = null,
    val sourceUrls: ImmutableList<String>? = null,
    val id: Long? = null
) {
    companion object {
        fun mock() = Entry(
            word = "word",
            phonetics = persistentListOf(),
            meanings = Meaning.mockList().toImmutableList(),
            license = null,
            sourceUrls = persistentListOf()
        )
    }
}