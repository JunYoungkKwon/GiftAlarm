package com.example.gifticonalarm.di

import com.example.gifticonalarm.data.repository.GifticonRepositoryImpl
import com.example.gifticonalarm.domain.repository.GifticonRepository
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
}