/*
 *     freeDictionaryApp/freeDictionaryApp.feature_about.main
 *     RepositoryDto.kt Copyrighted by Yamin Siahmargooei at 2025/11/16
 *     RepositoryDto.kt Last modified at 2025/11/16
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_about.main.
 *     Copyright (C) 2025  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_about.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_about.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_about.data.datasource.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import io.github.yamin8000.owl.feature_about.domain.Repository

@JsonClass(generateAdapter = true)
data class RepositoryDto(
    val name: String,
    val description: String,
    val forks: Int,
    @param:Json(name = "stargazers_count")
    val stargazers: Int
) {
    fun domain() = Repository(
        name = name,
        description = description,
        stars = stargazers,
        forks = forks
    )
}
