package com.example.gifticonalarm.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gifticonalarm.data.local.dao.GifticonDao
import com.example.gifticonalarm.data.local.entity.GifticonEntity

@Database(
    entities = [GifticonEntity::class],
    version = 3,
    exportSchema = false
)
abstract class GifticonDatabase : RoomDatabase() {
    abstract fun gifticonDao(): GifticonDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE gifticons ADD COLUMN type TEXT NOT NULL DEFAULT 'EXCHANGE'"
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE gifticons ADD COLUMN lastModifiedAt INTEGER NOT NULL DEFAULT 0"
                )
                db.execSQL(
                    "UPDATE gifticons SET lastModifiedAt = CAST(strftime('%s','now') AS INTEGER) * 1000 WHERE lastModifiedAt = 0"
                )
            }
        }
    }
}
