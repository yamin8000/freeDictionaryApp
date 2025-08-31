/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     Definition.kt Copyrighted by Yamin Siahmargooei at 2024/8/18
 *     Definition.kt Last modified at 2024/8/18
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_home.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main is distributed in the hope that it will be useful,
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
data class Definition(
    val definition: String,
    val example: String?,
    val synonyms: List<String>,
    val antonyms: List<String>,
    val meaningId: Long? = null,
    val id: Long? = null
) {
    companion object {
        fun mock() = Definition(
            definition = LoremIpsum(Random.nextInt(1..3)).values.joinToString(" "),
            example = LoremIpsum(Random.nextInt(0..3)).values.joinToString(" "),
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
            repeat(n) {
                add(mock())
            }
        }
    }
}