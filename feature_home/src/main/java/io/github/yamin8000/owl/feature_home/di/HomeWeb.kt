/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     HomeWebModule.kt Copyrighted by Yamin Siahmargooei at 2024/8/25
 *     HomeWebModule.kt Last modified at 2024/8/25
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

package io.github.yamin8000.owl.feature_home.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.yamin8000.owl.feature_home.data.datasource.remote.FreeDictionaryAPI
import io.github.yamin8000.owl.feature_home.data.repository.remote.FreeDictionaryRetrofitApiRepository
import io.github.yamin8000.owl.feature_home.domain.repository.remote.FreeDictionaryApiRepository
import io.github.yamin8000.owl.feature_home.domain.usecase.SearchFreeDictionary
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeWeb {

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
    fun providesFreeDictionaryApi(retrofit: Retrofit): FreeDictionaryAPI {
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