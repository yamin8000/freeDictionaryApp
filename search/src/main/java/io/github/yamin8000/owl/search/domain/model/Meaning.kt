/*
 *     freeDictionaryApp/freeDictionaryApp.search.main
 *     Meaning.kt Copyrighted by Yamin Siahmargooei at 2024/9/5
 *     Meaning.kt Last modified at 2024/9/5
 *     This file is part of freeDictionaryApp/freeDictionaryApp.search.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
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

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Meaning(
    val partOfSpeech: String,
    val definitions: List<Definition>,
    val synonyms: List<String>,
    val antonyms: List<String>,
    val entryId: Long? = null,
    val id: Long? = null
)