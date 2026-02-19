package com.example.gifticonalarm.domain.usecase

import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.repository.GifticonRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * 전체 기프티콘 목록을 관찰하는 유스케이스.
 */
class GetAllGifticonsUseCase @Inject constructor(
    private val repository: GifticonRepository
) {
    operator fun invoke(): Flow<List<Gifticon>> {
        return repository.observeAllGifticons()
    }
}
