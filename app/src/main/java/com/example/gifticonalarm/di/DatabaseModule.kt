package com.example.gifticonalarm.di

import android.content.Context
import androidx.room.Room
import com.example.gifticonalarm.data.local.dao.GifticonDao
import com.example.gifticonalarm.data.local.database.GifticonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideGifticonDatabase(
        @ApplicationContext context: Context
    ): GifticonDatabase {
        return Room.databaseBuilder(
            context,
            GifticonDatabase::class.java,
            "gifticon_database"
        )
            .addMigrations(GifticonDatabase.MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideGifticonDao(database: GifticonDatabase): GifticonDao {
        return database.gifticonDao()
    }
}
