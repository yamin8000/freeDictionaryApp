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

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.squareup.moshi.JsonClass
import kotlin.random.Random
import kotlin.random.nextInt

@JsonClass(generateAdapter = true)
data class Meaning(
    val partOfSpeech: String,
    val definitions: List<Definition>,
    val synonyms: List<String>,
    val antonyms: List<String>,
    val entryId: Long? = null,
    val id: Long? = null
) {
    companion object {
        fun mock() = Meaning(
            partOfSpeech = LoremIpsum(1).values.joinToString(" "),
            definitions = Definition.mockList(),
            synonyms = buildList {
                repeat(Random.nextInt(0..3)) {
                    add(LoremIpsum(Random.nextInt(0..3)).values.joinToString(" "))
                }
            },
            antonyms = buildList {
                repeat(Random.nextInt(0..3)) {
                    add(LoremIpsum(Random.nextInt(0..3)).values.joinToString(" "))
                }
            }
        )

        fun mockList(n: Int = Random.nextInt(0..3)) = buildList {
            repeat(n) { add(mock()) }
        }
    }
}