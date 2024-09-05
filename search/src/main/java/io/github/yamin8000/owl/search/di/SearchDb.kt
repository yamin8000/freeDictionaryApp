/*
 *     freeDictionaryApp/freeDictionaryApp.search.main
 *     HomeDb.kt Copyrighted by Yamin Siahmargooei at 2024/9/5
 *     HomeDb.kt Last modified at 2024/9/5
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

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.yamin8000.owl.search.data.datasource.local.AppDatabase
import io.github.yamin8000.owl.search.data.datasource.local.MIGRATION_7_8
import io.github.yamin8000.owl.search.data.datasource.local.dao.DAOs
import io.github.yamin8000.owl.search.data.repository.local.DefinitionRoomRepository
import io.github.yamin8000.owl.search.data.repository.local.EntryRoomRepository
import io.github.yamin8000.owl.search.data.repository.local.MeaningRoomRepository
import io.github.yamin8000.owl.search.data.repository.local.PhoneticRoomRepository
import io.github.yamin8000.owl.search.data.repository.local.TermRoomRepository
import io.github.yamin8000.owl.search.domain.repository.local.DefinitionRepository
import io.github.yamin8000.owl.search.domain.repository.local.EntryRepository
import io.github.yamin8000.owl.search.domain.repository.local.MeaningRepository
import io.github.yamin8000.owl.search.domain.repository.local.PhoneticRepository
import io.github.yamin8000.owl.search.domain.repository.local.TermRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchDb {

    @Provides
    @Singleton
    fun providesDb(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "db")
            .addMigrations(MIGRATION_7_8)
            .build()
    }

    @Provides
    @Singleton
    fun providesEntryDao(app: AppDatabase): DAOs.EntryDao {
        return app.entryDao()
    }

    @Provides
    @Singleton
    fun providesEntryRepository(
        entryDao: DAOs.EntryDao,
        phoneticRepository: PhoneticRepository,
        meaningRepository: MeaningRepository
    ): EntryRepository = EntryRoomRepository(
        dao = entryDao,
        phoneticRepository = phoneticRepository,
        meaningRepository = meaningRepository
    )

    @Provides
    @Singleton
    fun providesTermDao(app: AppDatabase): DAOs.TermDao {
        return app.termDao()
    }

    @Provides
    @Singleton
    fun providesTermRepository(dao: DAOs.TermDao): TermRepository {
        return TermRoomRepository(dao)
    }

    @Provides
    @Singleton
    fun providesDefinitionDao(app: AppDatabase): DAOs.DefinitionDao {
        return app.definitionDao()
    }

    @Provides
    @Singleton
    fun providesSynonymDao(app: AppDatabase): DAOs.SynonymDao {
        return app.synonymDao()
    }

    @Provides
    @Singleton
    fun providesAntonymDao(app: AppDatabase): DAOs.AntonymDao {
        return app.antonymDao()
    }

    @Provides
    @Singleton
    fun providesDefinitionRepository(
        definitionDao: DAOs.DefinitionDao,
        synonymDao: DAOs.SynonymDao,
        antonymDao: DAOs.AntonymDao
    ): DefinitionRepository = DefinitionRoomRepository(
        definitionDao = definitionDao,
        antonymDao = antonymDao,
        synonymDao = synonymDao
    )

    @Provides
    @Singleton
    fun providesMeaningDao(app: AppDatabase): DAOs.MeaningDao {
        return app.meaningDao()
    }

    @Provides
    @Singleton
    fun providesMeaningRepository(
        meaningDao: DAOs.MeaningDao,
        definitionRepository: DefinitionRepository
    ): MeaningRepository = MeaningRoomRepository(
        meaningDao = meaningDao,
        definitionRepository = definitionRepository
    )

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
}