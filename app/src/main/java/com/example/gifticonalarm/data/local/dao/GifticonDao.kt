package com.example.gifticonalarm.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gifticonalarm.data.local.entity.GifticonEntity

@Dao
interface GifticonDao {
    @Query("SELECT * FROM gifticons ORDER BY expiryDate ASC")
    fun getAllGifticons(): LiveData<List<GifticonEntity>>

    @Query("SELECT * FROM gifticons ORDER BY expiryDate ASC")
    suspend fun getAllGifticonsSnapshot(): List<GifticonEntity>

    @Query("SELECT * FROM gifticons WHERE id = :id")
    fun getGifticonById(id: Long): LiveData<GifticonEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGifticon(gifticon: GifticonEntity): Long

    @Update
    suspend fun updateGifticon(gifticon: GifticonEntity)

    @Delete
    suspend fun deleteGifticon(gifticon: GifticonEntity)

    @Query("SELECT * FROM gifticons WHERE expiryDate <= :thresholdDate AND isUsed = 0")
    fun getExpiringGifticons(thresholdDate: Long): LiveData<List<GifticonEntity>>
}
