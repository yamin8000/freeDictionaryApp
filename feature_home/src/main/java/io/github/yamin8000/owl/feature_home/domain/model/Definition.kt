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

package io.github.yamin8000.owl.feature_home.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Definition(
    val definition: String,
    val example: String?,
    val synonyms: List<String>,
    val antonyms: List<String>,
    val id: Long? = null
)