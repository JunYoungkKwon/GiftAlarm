package com.example.gifticonalarm.domain.usecase

import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.repository.GifticonRepository
import javax.inject.Inject

/**
 * 기프티콘을 삭제하는 유스케이스.
 */
class DeleteGifticonUseCase @Inject constructor(
    private val repository: GifticonRepository
) {
    suspend operator fun invoke(gifticon: Gifticon) {
        repository.deleteGifticon(gifticon)
    }
}
