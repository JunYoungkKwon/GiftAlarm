package com.example.gifticonalarm.domain.repository

import com.example.gifticonalarm.domain.model.Gifticon
import kotlinx.coroutines.flow.Flow

/**
 * 기프티콘 도메인 저장소 인터페이스.
 * Domain 레이어가 Android 프레임워크에 의존하지 않도록 Flow를 사용한다.
 */
interface GifticonRepository {
    fun observeAllGifticons(): Flow<List<Gifticon>>
    suspend fun getAllGifticons(): List<Gifticon>
    fun observeGifticonById(id: Long): Flow<Gifticon?>
    suspend fun insertGifticon(gifticon: Gifticon): Long
    suspend fun updateGifticon(gifticon: Gifticon)
    suspend fun deleteGifticon(gifticon: Gifticon)
    fun observeExpiringGifticons(daysThreshold: Int): Flow<List<Gifticon>>
}
