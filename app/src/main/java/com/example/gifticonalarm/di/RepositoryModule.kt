package com.example.gifticonalarm.di

import com.example.gifticonalarm.data.repository.GifticonRepositoryImpl
import com.example.gifticonalarm.data.repository.ImageStorageRepositoryImpl
import com.example.gifticonalarm.data.repository.NotificationSettingsRepositoryImpl
import com.example.gifticonalarm.data.repository.OnboardingRepositoryImpl
import com.example.gifticonalarm.data.notification.WorkManagerNotificationScheduler
import com.example.gifticonalarm.domain.repository.GifticonRepository
import com.example.gifticonalarm.domain.repository.ImageStorageRepository
import com.example.gifticonalarm.domain.repository.NotificationScheduler
import com.example.gifticonalarm.domain.repository.NotificationSettingsRepository
import com.example.gifticonalarm.domain.repository.OnboardingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGifticonRepository(
        gifticonRepositoryImpl: GifticonRepositoryImpl
    ): GifticonRepository

    @Binds
    @Singleton
    abstract fun bindImageStorageRepository(
        imageStorageRepositoryImpl: ImageStorageRepositoryImpl
    ): ImageStorageRepository

    /**
     * 온보딩 저장소 구현체를 바인딩한다.
     */
    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(
        onboardingRepositoryImpl: OnboardingRepositoryImpl
    ): OnboardingRepository

    @Binds
    @Singleton
    abstract fun bindNotificationSettingsRepository(
        notificationSettingsRepositoryImpl: NotificationSettingsRepositoryImpl
    ): NotificationSettingsRepository

    @Binds
    @Singleton
    abstract fun bindNotificationScheduler(
        workManagerNotificationScheduler: WorkManagerNotificationScheduler
    ): NotificationScheduler
}
