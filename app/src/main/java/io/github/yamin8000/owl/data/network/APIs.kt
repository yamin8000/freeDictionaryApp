/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     APIs.kt Copyrighted by Yamin Siahmargooei at 2023/8/26
 *     APIs.kt Last modified at 2023/8/26
 *     This file is part of freeDictionaryApp/freeDictionaryApp.app.main.
 *     Copyright (C) 2023  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.data.network

import io.github.yamin8000.owl.data.model.Entry
import retrofit2.http.GET
import retrofit2.http.Path

object APIs {

    interface FreeDictionaryAPI {

        @GET("entries/en/{word}")
        suspend fun search(@Path("word") word: String): List<Entry>
    }
}