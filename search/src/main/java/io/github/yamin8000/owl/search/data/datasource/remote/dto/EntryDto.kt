/*
 *     freeDictionaryApp/freeDictionaryApp.search.main
 *     EntryDto.kt Copyrighted by Yamin Siahmargooei at 2025/9/17
 *     EntryDto.kt Last modified at 2025/9/8
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

package io.github.yamin8000.owl.search.data.datasource.remote.dto

import com.squareup.moshi.JsonClass
import io.github.yamin8000.owl.search.domain.model.Entry
import kotlinx.collections.immutable.toImmutableList

@JsonClass(generateAdapter = true)
data class EntryDto(
    val word: String,
    val phonetics: List<PhoneticDto>,
    val meanings: List<MeaningDto>,
    val license: LicenseDto? = null,
    val sourceUrls: List<String>? = null,
    val id: Long? = null
) {
    fun domain() = Entry(
        word = word,
        phonetics = phonetics.map { it.domain() }.toImmutableList(),
        meanings = meanings.map { it.domain() }.toImmutableList(),
        license = license?.domain(),
        sourceUrls = sourceUrls?.toImmutableList(),
        id = id
    )
}
