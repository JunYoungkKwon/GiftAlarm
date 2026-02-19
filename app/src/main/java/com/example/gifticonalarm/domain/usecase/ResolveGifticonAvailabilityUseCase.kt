package com.example.gifticonalarm.domain.usecase

import com.example.gifticonalarm.domain.model.GifticonAvailability
import java.util.Date
import javax.inject.Inject

/**
 * 쿠폰의 사용 여부와 만료일로 현재 상태를 계산한다.
 */
class ResolveGifticonAvailabilityUseCase @Inject constructor(
    private val calculateDdayUseCase: CalculateDdayUseCase
) {

    operator fun invoke(isUsed: Boolean, expiryDate: Date): GifticonAvailability {
        if (isUsed) return GifticonAvailability.USED
        return if (calculateDdayUseCase(expiryDate) < 0L) {
            GifticonAvailability.EXPIRED
        } else {
            GifticonAvailability.AVAILABLE
        }
    }
}
