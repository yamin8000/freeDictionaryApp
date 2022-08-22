/*
 *     Owl: an android app for Owlbot Dictionary API
 *     Web.kt Created by Yamin Siahmargooei at 2022/1/16
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

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("SameParameterValue")
object Web {

    private const val baseUrl = "https://owlbot.info/api/v4/"
    private const val ninjaApiBaseUrl = "https://api.api-ninjas.com/v1/"

    val retrofit: Retrofit by lazy(LazyThreadSafetyMode.NONE) { createRetrofit() }

    val ninjaApiRetrofit: Retrofit by lazy(LazyThreadSafetyMode.NONE) { createNinjaApiRetrofit() }

    private fun createNinjaApiRetrofit(): Retrofit {
        return createCustomUrlRetrofit(ninjaApiBaseUrl)
    }

    private fun createCustomUrlRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
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