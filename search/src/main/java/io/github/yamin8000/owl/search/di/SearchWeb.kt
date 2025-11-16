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

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.yamin8000.owl.search.data.datasource.remote.FreeDictionaryAPI
import io.github.yamin8000.owl.search.data.repository.remote.FreeDictionaryRetrofitApiRepository
import io.github.yamin8000.owl.search.domain.repository.remote.FreeDictionaryApiRepository
import io.github.yamin8000.owl.search.domain.usecase.SearchFreeDictionary
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchWeb {

    @Qualifier
    annotation class SearchModule

    @SearchModule
    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        val baseUrl = "https://api.dictionaryapi.dev/api/v2/"
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesFreeDictionaryApi(
        @SearchModule
        retrofit: Retrofit
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
}