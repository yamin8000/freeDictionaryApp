/*
 *     freeDictionaryApp/freeDictionaryApp.app.main
 *     Web.kt Copyrighted by Yamin Siahmargooei at 2023/8/26
 *     Web.kt Last modified at 2023/8/26
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

package io.github.yamin8000.owl.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Web {

    private const val baseUrl = "https://api.dictionaryapi.dev/api/v2/"

    val retrofit: Retrofit by lazy(LazyThreadSafetyMode.NONE) { createRetrofit() }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    /**
     * get service of given api/interface
     *
     * @param T type/class/interface of api
     * @return service for that api
     */
    inline fun <reified T> Retrofit.getAPI(): T = this.create(T::class.java)
}