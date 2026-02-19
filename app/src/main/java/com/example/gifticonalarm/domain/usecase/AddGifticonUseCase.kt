package com.example.gifticonalarm.domain.usecase

import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.repository.GifticonRepository
import javax.inject.Inject

/**
 * 기프티콘을 저장하는 유스케이스.
 */
class AddGifticonUseCase @Inject constructor(
    private val repository: GifticonRepository
) {
    suspend operator fun invoke(gifticon: Gifticon): Long {
        return repository.insertGifticon(gifticon)
    }
}
