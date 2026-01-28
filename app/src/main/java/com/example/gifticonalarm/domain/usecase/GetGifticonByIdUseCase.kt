package com.example.gifticonalarm.domain.usecase

import androidx.lifecycle.LiveData
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.repository.GifticonRepository
import javax.inject.Inject

class GetGifticonByIdUseCase @Inject constructor(
    private val repository: GifticonRepository
) {
    operator fun invoke(id: Long): LiveData<Gifticon?> {
        return repository.getGifticonById(id)
    }
}