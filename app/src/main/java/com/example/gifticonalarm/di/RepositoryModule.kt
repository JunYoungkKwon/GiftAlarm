package com.example.gifticonalarm.di

import com.example.gifticonalarm.data.repository.GifticonRepositoryImpl
import com.example.gifticonalarm.data.repository.OnboardingRepositoryImpl
import com.example.gifticonalarm.domain.repository.GifticonRepository
import com.example.gifticonalarm.domain.repository.OnboardingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGifticonRepository(
        gifticonRepositoryImpl: GifticonRepositoryImpl
    ): GifticonRepository

    /**
     * 온보딩 저장소 구현체를 바인딩한다.
     */
    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(
        onboardingRepositoryImpl: OnboardingRepositoryImpl
    ): OnboardingRepository
}
