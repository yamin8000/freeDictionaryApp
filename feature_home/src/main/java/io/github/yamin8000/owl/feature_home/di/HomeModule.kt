/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     HomeModule.kt Copyrighted by Yamin Siahmargooei at 2024/8/18
 *     HomeModule.kt Last modified at 2024/8/18
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

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.yamin8000.owl.feature_home.data.datasource.local.AppDatabase
import io.github.yamin8000.owl.feature_home.data.datasource.local.dao.DAOs
import io.github.yamin8000.owl.feature_home.data.datasource.local.entity.TermEntity
import io.github.yamin8000.owl.feature_home.data.datasource.remote.FreeDictionaryAPI
import io.github.yamin8000.owl.feature_home.data.repository.TermSuggesterRepositoryImpl
import io.github.yamin8000.owl.feature_home.data.repository.local.DefinitionRoomRepository
import io.github.yamin8000.owl.feature_home.data.repository.local.EntryRoomRepository
import io.github.yamin8000.owl.feature_home.data.repository.local.MeaningRoomRepository
import io.github.yamin8000.owl.feature_home.data.repository.local.PhoneticRoomRepository
import io.github.yamin8000.owl.feature_home.data.repository.local.TermRoomRepository
import io.github.yamin8000.owl.feature_home.data.repository.remote.FreeDictionaryRetrofitApiRepository
import io.github.yamin8000.owl.feature_home.domain.repository.TermSuggesterRepository
import io.github.yamin8000.owl.feature_home.domain.repository.local.DefinitionRepository
import io.github.yamin8000.owl.feature_home.domain.repository.local.EntryRepository
import io.github.yamin8000.owl.feature_home.domain.repository.local.MeaningRepository
import io.github.yamin8000.owl.feature_home.domain.repository.local.PhoneticRepository
import io.github.yamin8000.owl.feature_home.domain.repository.local.util.BaseRepository
import io.github.yamin8000.owl.feature_home.domain.repository.remote.FreeDictionaryApiRepository
import io.github.yamin8000.owl.feature_home.domain.usecase.FreeDictionaryUseCase
import io.github.yamin8000.owl.feature_home.domain.usecase.GetCachedWord
import io.github.yamin8000.owl.feature_home.domain.usecase.WordCacheUseCases
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    @Singleton
    fun providesDb(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "db")
            .build()
    }

    @Provides
    @Singleton
    fun providesEntryDao(app: AppDatabase): DAOs.EntryDao {
        return app.entryDao()
    }

    @Provides
    @Singleton
    fun providesEntryRepository(dao: DAOs.EntryDao): EntryRepository {
        return EntryRoomRepository(dao)
    }

    @Provides
    @Singleton
    fun providesTermDao(app: AppDatabase): DAOs.TermDao {
        return app.termDao()
    }

    @Provides
    @Singleton
    fun providesTermRepository(dao: DAOs.TermDao): BaseRepository<TermEntity> {
        return TermRoomRepository(dao)
    }

    @Provides
    @Singleton
    fun providesDefinitionDao(app: AppDatabase): DAOs.DefinitionDao {
        return app.definitionDao()
    }

    @Provides
    @Singleton
    fun providesDefinitionRepository(dao: DAOs.DefinitionDao): DefinitionRepository {
        return DefinitionRoomRepository(dao)
    }

    @Provides
    @Singleton
    fun providesMeaningDao(app: AppDatabase): DAOs.MeaningDao {
        return app.meaningDao()
    }

    @Provides
    @Singleton
    fun providesMeaningRepository(dao: DAOs.MeaningDao): MeaningRepository {
        return MeaningRoomRepository(dao)
    }

    @Provides
    @Singleton
    fun providesPhoneticDao(app: AppDatabase): DAOs.PhoneticDao {
        return app.phoneticDao()
    }

    @Provides
    @Singleton
    fun providesPhoneticRepository(dao: DAOs.PhoneticDao): PhoneticRepository {
        return PhoneticRoomRepository(dao)
    }

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
    fun providesFreeDictionaryApiUseCase(repository: FreeDictionaryApiRepository): FreeDictionaryUseCase {
        return FreeDictionaryUseCase(repository)
    }

    @Provides
    @Singleton
    fun providesTermSuggesterRepository(
        db: AppDatabase,
        app: Application
    ): TermSuggesterRepository = TermSuggesterRepositoryImpl(db.termDao(), app)

    @Provides
    @Singleton
    fun providesWordCacheUseCases(
        entryRepository: EntryRepository,
        phoneticRepository: PhoneticRepository,
        meaningRepository: MeaningRepository,
        definitionRepository: DefinitionRepository
    ) = WordCacheUseCases(
        getCachedWord = GetCachedWord(
            entryRepository,
            phoneticRepository,
            meaningRepository,
            definitionRepository
        )
    )
}