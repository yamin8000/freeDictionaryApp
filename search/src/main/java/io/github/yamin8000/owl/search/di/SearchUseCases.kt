/*
 *     freeDictionaryApp/freeDictionaryApp.search.main
 *     SearchUseCases.kt Copyrighted by Yamin Siahmargooei at 2024/9/5
 *     SearchUseCases.kt Last modified at 2024/9/5
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
import io.github.yamin8000.owl.search.domain.repository.local.DefinitionRepository
import io.github.yamin8000.owl.search.domain.repository.local.EntryRepository
import io.github.yamin8000.owl.search.domain.repository.local.MeaningRepository
import io.github.yamin8000.owl.search.domain.repository.local.PhoneticRepository
import io.github.yamin8000.owl.search.domain.repository.local.TermRepository
import io.github.yamin8000.owl.search.domain.usecase.CacheWord
import io.github.yamin8000.owl.search.domain.usecase.CacheWordData
import io.github.yamin8000.owl.search.domain.usecase.GetCachedWord
import io.github.yamin8000.owl.search.domain.usecase.WordCacheUseCases
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchUseCases {

    @Provides
    @Singleton
    fun providesWordCacheUseCases(
        entryRepository: EntryRepository,
        phoneticRepository: PhoneticRepository,
        meaningRepository: MeaningRepository,
        definitionRepository: DefinitionRepository,
        termRepository: TermRepository
    ) = WordCacheUseCases(
        getCachedWord = GetCachedWord(entryRepository),
        cacheWord = CacheWord(
            entryRepository = entryRepository,
            meaningRepository = meaningRepository,
            definitionRepository = definitionRepository,
            phoneticRepository = phoneticRepository
        ),
        cacheWordData = CacheWordData(termRepository)
    )
}