/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     HomeUseCases.kt Copyrighted by Yamin Siahmargooei at 2024/8/25
 *     HomeUseCases.kt Last modified at 2024/8/25
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
import io.github.yamin8000.owl.feature_home.domain.usecase.GetRandomWord
import io.github.yamin8000.owl.search.domain.repository.local.TermRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeUseCases {

    @Provides
    @Singleton
    fun providesRandomWordUseCase(
        repository: TermRepository
    ): GetRandomWord = GetRandomWord(repository)
}