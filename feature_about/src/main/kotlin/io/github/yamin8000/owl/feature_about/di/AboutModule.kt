/*
 *     freeDictionaryApp/freeDictionaryApp.feature_about.main
 *     AboutModule.kt Copyrighted by Yamin Siahmargooei at 2025/11/16
 *     AboutModule.kt Last modified at 2025/11/16
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

package io.github.yamin8000.owl.feature_about.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.yamin8000.owl.feature_about.data.datasource.remote.GithubAPIs
import io.github.yamin8000.owl.feature_about.data.repository.GithubWebRepoRepository
import io.github.yamin8000.owl.feature_about.domain.repository.GithubRepoRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AboutModule {

    @Qualifier
    annotation class AboutModule

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request()
                newRequest.newBuilder()
                    .addHeader("Accept", "application/vnd.github+json")
                    .addHeader("X-GitHub-Api-Version", "2022-11-28")
                    .build()
                return@addInterceptor chain.proceed(newRequest)
            }.build()
    }

    @AboutModule
    @Provides
    @Singleton
    fun providesRetrofit(
        client: OkHttpClient
    ): Retrofit {
        val baseUrl = "https://api.github.com"
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesGithubAPIs(
        @AboutModule
        retrofit: Retrofit
    ): GithubAPIs {
        return retrofit.create<GithubAPIs>()
    }

    @Provides
    @Singleton
    fun providesGithubRepoRepository(api: GithubAPIs): GithubRepoRepository {
        return GithubWebRepoRepository(api)
    }
}