package com.example.gifticonalarm.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gifticonalarm.data.local.dao.GifticonDao
import com.example.gifticonalarm.data.local.entity.GifticonEntity

@Database(
    entities = [GifticonEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GifticonDatabase : RoomDatabase() {
    abstract fun gifticonDao(): GifticonDao
}