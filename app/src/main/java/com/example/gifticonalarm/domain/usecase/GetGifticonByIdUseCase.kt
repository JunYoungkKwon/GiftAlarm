package com.example.gifticonalarm.domain.usecase

import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.repository.GifticonRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * ID로 기프티콘 상세를 관찰하는 유스케이스.
 */
class GetGifticonByIdUseCase @Inject constructor(
    private val repository: GifticonRepository
) {
    operator fun invoke(id: Long): Flow<Gifticon?> {
        return repository.observeGifticonById(id)
    }
}
