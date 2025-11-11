/*
 *     freeDictionaryApp/freeDictionaryApp.search.main
 *     Meaning.kt Copyrighted by Yamin Siahmargooei at 2025/9/17
 *     Meaning.kt Last modified at 2025/9/17
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
import kotlinx.collections.immutable.toImmutableList
import kotlin.random.Random
import kotlin.random.nextInt

data class Meaning(
    val partOfSpeech: String,
    val definitions: ImmutableList<Definition>,
    val synonyms: ImmutableList<String>,
    val antonyms: ImmutableList<String>,
    val entryId: Long? = null,
    val id: Long? = null
) {
    companion object {
        fun mock() = Meaning(
            partOfSpeech = "partOfSpeech",
            definitions = Definition.mockList().toImmutableList(),
            synonyms = buildList {
                repeat(Random.nextInt(0..3)) {
                    add("Synonym")
                }
            }.toImmutableList(),
            antonyms = buildList {
                repeat(Random.nextInt(0..3)) {
                    add("Antonym")
                }
            }.toImmutableList()
        )

        fun mockList(n: Int = Random.nextInt(1..5)) = buildList {
            repeat(n) { add(mock()) }
        }
    }
}