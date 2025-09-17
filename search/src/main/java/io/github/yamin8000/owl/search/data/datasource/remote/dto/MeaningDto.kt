/*
 *     freeDictionaryApp/freeDictionaryApp.search.main
 *     MeaningDto.kt Copyrighted by Yamin Siahmargooei at 2025/9/17
 *     MeaningDto.kt Last modified at 2025/9/8
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
import io.github.yamin8000.owl.search.domain.model.Meaning

@JsonClass(generateAdapter = true)
data class MeaningDto(
    val partOfSpeech: String,
    val definitions: List<DefinitionDto>,
    val synonyms: List<String>,
    val antonyms: List<String>,
    val id: Long? = null,
    val entryId: Long? = null
) {
    fun domain() = Meaning(
        id = id,
        entryId = entryId,
        partOfSpeech = partOfSpeech,
        definitions = definitions.map { it.domain() },
        synonyms = synonyms,
        antonyms = antonyms
    )
}