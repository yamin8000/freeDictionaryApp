/*
 *     Owl: an android app for Owlbot Dictionary API
 *     APIs.kt Created by Yamin Siahmargooei at 2022/1/16
 *     This file is part of Owl.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Owl is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Owl is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Owl.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.network

import io.github.yamin8000.owl.model.RandomWord
import io.github.yamin8000.owl.model.Word
import io.github.yamin8000.owl.network.ApiKey.API_NINJA_KEY
import io.github.yamin8000.owl.network.ApiKey.OWLBOT_TOKEN
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

object APIs {

    interface OwlBotWordAPI {

        @Headers(OWLBOT_TOKEN)
        @GET("dictionary/{word}")
        suspend fun searchWord(@Path("word") word: String): Word?
    }

    interface NinjaAPI {

        @Headers(API_NINJA_KEY)
        @GET("randomword")
        suspend fun getRandomWord(): RandomWord
    }
}