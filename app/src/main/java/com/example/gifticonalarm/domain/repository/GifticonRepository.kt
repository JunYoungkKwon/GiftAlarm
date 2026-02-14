package com.example.gifticonalarm.domain.repository

import androidx.lifecycle.LiveData
import com.example.gifticonalarm.domain.model.Gifticon

interface GifticonRepository {
    fun getAllGifticons(): LiveData<List<Gifticon>>
    fun getGifticonById(id: Long): LiveData<Gifticon?>
    suspend fun insertGifticon(gifticon: Gifticon): Long
    suspend fun updateGifticon(gifticon: Gifticon)
    suspend fun deleteGifticon(gifticon: Gifticon)
    fun getExpiringGifticons(daysThreshold: Int): LiveData<List<Gifticon>>
}