/*
 *     freeDictionaryApp/freeDictionaryApp.network.main
 *     Web.kt Copyrighted by Yamin Siahmargooei at 2024/5/9
 *     Web.kt Last modified at 2024/3/23
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

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/** App-wide Web Helper Singleton */
object Web {

    private const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/"

    private lateinit var retrofit: Retrofit

    fun getRetrofit(): Retrofit {
        if (!this::retrofit.isInitialized) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }
        return retrofit
    }

    /**
     * Get service of given API/interface
     *
     * @param T type/class/interface of API
     * @return service for that API
     */
    inline fun <reified T> Retrofit.getAPI(): T = this.create(T::class.java)
}