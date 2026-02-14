package com.example.gifticonalarm.domain.usecase

import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.repository.GifticonRepository
import javax.inject.Inject

class AddGifticonUseCase @Inject constructor(
    private val repository: GifticonRepository
) {
    suspend operator fun invoke(gifticon: Gifticon): Long {
        return repository.insertGifticon(gifticon)
    }
}