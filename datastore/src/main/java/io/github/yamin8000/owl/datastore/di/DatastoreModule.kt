/*
 *     freeDictionaryApp/freeDictionaryApp.datastore.main
 *     DatastoreModule.kt Copyrighted by Yamin Siahmargooei at 2024/8/19
 *     DatastoreModule.kt Last modified at 2024/8/19
 *     This file is part of freeDictionaryApp/freeDictionaryApp.datastore.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.datastore.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.datastore.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.datastore.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.yamin8000.owl.datastore.data.datasource.Datastore.historyDataStore
import io.github.yamin8000.owl.datastore.data.datasource.Datastore.settingsDataStore
import io.github.yamin8000.owl.datastore.data.repository.HistoryDatastoreRepository
import io.github.yamin8000.owl.datastore.data.repository.SettingsDatastoreRepository
import io.github.yamin8000.owl.datastore.domain.repository.HistoryRepository
import io.github.yamin8000.owl.datastore.domain.repository.SettingsRepository
import io.github.yamin8000.owl.datastore.domain.usecase.history.AddHistory
import io.github.yamin8000.owl.datastore.domain.usecase.history.GetAllHistory
import io.github.yamin8000.owl.datastore.domain.usecase.history.HistoryUseCases
import io.github.yamin8000.owl.datastore.domain.usecase.history.RemoveAllHistory
import io.github.yamin8000.owl.datastore.domain.usecase.history.RemoveHistory
import io.github.yamin8000.owl.datastore.domain.usecase.settings.GetStartingBlank
import io.github.yamin8000.owl.datastore.domain.usecase.settings.GetTTS
import io.github.yamin8000.owl.datastore.domain.usecase.settings.GetTheme
import io.github.yamin8000.owl.datastore.domain.usecase.settings.GetVibration
import io.github.yamin8000.owl.datastore.domain.usecase.settings.SetStartingBlank
import io.github.yamin8000.owl.datastore.domain.usecase.settings.SetTTS
import io.github.yamin8000.owl.datastore.domain.usecase.settings.SetTheme
import io.github.yamin8000.owl.datastore.domain.usecase.settings.SetVibration
import io.github.yamin8000.owl.datastore.domain.usecase.settings.SettingUseCases
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

    @Provides
    @Singleton
    fun provideSettingsRepository(app: Application): SettingsRepository {
        return SettingsDatastoreRepository(app.settingsDataStore)
    }

    @Provides
    @Singleton
    fun providesSettingsUseCases(repository: SettingsRepository): SettingUseCases {
        return SettingUseCases(
            getTTS = GetTTS(repository),
            setTTS = SetTTS(repository),
            getTheme = GetTheme(repository),
            setTheme = SetTheme(repository),
            getVibration = GetVibration(repository),
            setVibration = SetVibration(repository),
            getStartingBlank = GetStartingBlank(repository),
            setStartingBlank = SetStartingBlank(repository)
        )
    }

    @Provides
    @Singleton
    fun provideHistoryRepository(app: Application): HistoryRepository {
        return HistoryDatastoreRepository(app.historyDataStore)
    }

    @Provides
    @Singleton
    fun providesHistoryUseCases(repository: HistoryRepository): HistoryUseCases {
        return HistoryUseCases(
            addHistory = AddHistory(repository),
            removeHistory = RemoveHistory(repository),
            removeAllHistory = RemoveAllHistory(repository),
            getAllHistory = GetAllHistory(repository)
        )
    }
}