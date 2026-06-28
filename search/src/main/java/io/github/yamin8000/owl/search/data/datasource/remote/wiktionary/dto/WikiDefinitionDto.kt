/*
 *     freeDictionaryApp/freeDictionaryApp.search.main
 *     WikiDefinitionDto.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     WikiDefinitionDto.kt Last modified at 2026/6/25
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

package io.github.yamin8000.owl.search.data.datasource.remote.wiktionary.dto

import com.squareup.moshi.JsonClass
import io.github.yamin8000.owl.search.domain.model.Definition
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.jsoup.Jsoup

@JsonClass(generateAdapter = true)
data class WikiDefinitionDto(
    val definition: String,
    val examples: List<String>? = null,
    val id: Long? = null,
    val meaningId: Long? = null
) {
    fun domain() = Definition(
        id = id,
        meaningId = meaningId,
        definition = Jsoup.parse(definition).text(),
        examples = examples?.map { Jsoup.parse(it).text() }
            ?.filter { it.isNotEmpty() }
            ?.toPersistentList() ?: persistentListOf()
    )
}
