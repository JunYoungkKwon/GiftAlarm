package com.example.gifticonalarm.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.onboardingDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "onboarding_preferences"
)

/**
 * DataStore 의존성을 제공하는 모듈.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    /**
     * 온보딩 상태 저장용 DataStore를 제공한다.
     */
    @Provides
    @Singleton
    fun provideOnboardingDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.onboardingDataStore
    }
}
