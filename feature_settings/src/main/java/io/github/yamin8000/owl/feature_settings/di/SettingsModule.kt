package io.github.yamin8000.owl.feature_settings.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.yamin8000.owl.common.util.TTS
import io.github.yamin8000.owl.datastore.domain.usecase.settings.SettingUseCases
import kotlinx.coroutines.runBlocking
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

    @Provides
    @Singleton
    fun providesTtsLanguages(
        app: Application,
        settingUseCases: SettingUseCases
    ): List<Locale> = runBlocking {
        TTS(
            app,
            settingUseCases.getTTS()
        ).englishLanguages()
    }
}