/*
 *     freeDictionaryApp/freeDictionaryApp.network.main
 *     APIs.kt Copyrighted by Yamin Siahmargooei at 2024/5/9
 *     APIs.kt Last modified at 2024/3/23
 *     This file is part of freeDictionaryApp/freeDictionaryApp.network.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.network.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.network.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.network

import io.github.yamin8000.owl.data.model.Entry
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * App APIs
 */
object APIs {

    /**
     * [Free Dictionary API](https://dictionaryapi.dev)
     */
    interface FreeDictionaryAPI {

        /**
         * Search for [word] definitions
         */
        @GET("entries/en/{word}")
        suspend fun search(@Path("word") word: String): List<Entry>
    }
}