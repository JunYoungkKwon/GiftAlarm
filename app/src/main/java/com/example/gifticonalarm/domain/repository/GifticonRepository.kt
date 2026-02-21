package com.example.gifticonalarm.domain.repository

import com.example.gifticonalarm.domain.model.Gifticon
import kotlinx.coroutines.flow.Flow

/**
 * 기프티콘 도메인 저장소 인터페이스.
 * Domain 레이어가 Android 프레임워크에 의존하지 않도록 Flow를 사용한다.
 */
interface GifticonRepository {
    /**
     * 전체 기프티콘 목록 변화를 관찰한다.
     */
    fun observeAllGifticons(): Flow<List<Gifticon>>

    /**
     * 현재 저장된 전체 기프티콘을 즉시 조회한다.
     */
    suspend fun getAllGifticons(): List<Gifticon>

    /**
     * ID에 해당하는 기프티콘 변화를 관찰한다.
     */
    fun observeGifticonById(id: Long): Flow<Gifticon?>

    /**
     * 기프티콘을 추가하고 생성된 ID를 반환한다.
     */
    suspend fun insertGifticon(gifticon: Gifticon): Long

    /**
     * 기프티콘을 수정한다.
     */
    suspend fun updateGifticon(gifticon: Gifticon)

    /**
     * 기프티콘을 삭제한다.
     */
    suspend fun deleteGifticon(gifticon: Gifticon)

    /**
     * 만료 임박 기준일 이내의 기프티콘 목록 변화를 관찰한다.
     */
    fun observeExpiringGifticons(daysThreshold: Int): Flow<List<Gifticon>>
}
