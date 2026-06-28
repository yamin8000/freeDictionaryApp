/*
 *     freeDictionaryApp/freeDictionaryApp.search.main
 *     SearchWeb.kt Copyrighted by Yamin Siahmargooei at 2024/9/5
 *     SearchWeb.kt Last modified at 2024/9/5
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

package io.github.yamin8000.owl.search.di

import android.os.Build
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.yamin8000.owl.common.BuildConfig
import io.github.yamin8000.owl.common.util.log
import io.github.yamin8000.owl.search.data.datasource.remote.free.FreeDictionaryAPI
import io.github.yamin8000.owl.search.data.datasource.remote.wiktionary.WiktionaryAPI
import io.github.yamin8000.owl.search.data.repository.remote.FreeDictionaryRetrofitApiRepository
import io.github.yamin8000.owl.search.data.repository.remote.WiktionaryApiRetrofitRepository
import io.github.yamin8000.owl.search.domain.repository.remote.FreeDictionaryApiRepository
import io.github.yamin8000.owl.search.domain.repository.remote.WiktionaryApiRepository
import io.github.yamin8000.owl.search.domain.usecase.search.SearchFreeDictionary
import io.github.yamin8000.owl.search.domain.usecase.search.SearchWiktionary
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchWeb {

    @Qualifier
    annotation class FreeDictionary

    @Qualifier
    annotation class Wiktionary

    @FreeDictionary
    @Provides
    @Singleton
    fun providesFreeDictionaryRetrofit(): Retrofit {
        val clientBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor { chain ->
                val request = chain.request()
                log("Retrofit/OkHttp request: ${request.method}:${request.url}")
                log("Request body: ${request.body}")

                val response = chain.proceed(request)
                log("Response code for: ${request.method}:${request.url} is ${response.code}")

                return@addInterceptor response
            }
        }

        return Retrofit.Builder()
            .baseUrl("https://api.dictionaryapi.dev/api/v2/")
            .client(clientBuilder.build())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesFreeDictionaryApi(
        @FreeDictionary retrofit: Retrofit
    ): FreeDictionaryAPI {
        return retrofit.create<FreeDictionaryAPI>()
    }

    @Provides
    @Singleton
    fun providesFreeDictionaryApiRepository(api: FreeDictionaryAPI): FreeDictionaryApiRepository {
        return FreeDictionaryRetrofitApiRepository(api)
    }

    @Provides
    @Singleton
    fun providesFreeDictionaryApiUseCase(repository: FreeDictionaryApiRepository): SearchFreeDictionary {
        return SearchFreeDictionary(repository)
    }

    @Wiktionary
    @Provides
    @Singleton
    fun providesWiktionaryRetrofit(): Retrofit {
        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain.request()
                    .newBuilder()
                    .addHeader("User-Agent", createUserAgent())

                val request = requestBuilder.build()
                if (BuildConfig.DEBUG) {
                    log("Retrofit/OkHttp request: ${request.method}:${request.url}")
                    log("Request body: ${request.body}")
                }

                val response = chain.proceed(request)
                if (BuildConfig.DEBUG) {
                    log("Response code for: ${request.method}:${request.url} is ${response.code}")
                }

                return@addInterceptor response
            }



        return Retrofit.Builder()
            .baseUrl("https://en.wiktionary.org/api/rest_v1/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(clientBuilder.build())
            .build()
    }

    private fun createUserAgent() = buildString {
        append("FreeDictionaryApp ")
        append("${Build.BRAND} ${Build.MODEL} ${Build.DEVICE} ${Build.PRODUCT} ")
        append("Android: ${Build.VERSION.BASE_OS}, ${Build.VERSION.RELEASE}")
    }.trim()

    @Provides
    @Singleton
    fun providesWiktionaryApi(
        @Wiktionary retrofit: Retrofit
    ): WiktionaryAPI {
        return retrofit.create<WiktionaryAPI>()
    }

    @Provides
    @Singleton
    fun providesWiktionaryApiRepository(api: WiktionaryAPI): WiktionaryApiRepository {
        return WiktionaryApiRetrofitRepository(api)
    }

    @Provides
    @Singleton
    fun providesWiktionaryApiUseCase(repository: WiktionaryApiRepository): SearchWiktionary {
        return SearchWiktionary(repository)
    }
}