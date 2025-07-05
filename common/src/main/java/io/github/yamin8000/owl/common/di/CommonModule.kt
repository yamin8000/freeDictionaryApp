/*
 *     freeDictionaryApp/freeDictionaryApp.common.main
 *     CommonModule.kt Copyrighted by Yamin Siahmargooei at 2024/8/25
 *     CommonModule.kt Last modified at 2024/8/25
 *     This file is part of freeDictionaryApp/freeDictionaryApp.common.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.common.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.common.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.common.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.yamin8000.owl.common.util.TTS
import io.github.yamin8000.owl.datastore.domain.usecase.settings.SettingUseCases
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    @Singleton
    fun providesTts(
        app: Application,
        settingUseCases: SettingUseCases
    ): TTS {
        return runBlocking {
            settingUseCases.getTTS()
            return@runBlocking TTS(
                app,
                settingUseCases.getTTS()
            )
        }
    }
}