package com.example.gifticonalarm.domain.usecase

import com.example.gifticonalarm.domain.model.GifticonAvailability
import java.util.Date
import javax.inject.Inject

/**
 * 쿠폰 상태 라벨(예: D-7, 사용완료, 만료)을 생성한다.
 */
class BuildGifticonStatusLabelUseCase @Inject constructor(
    private val calculateDdayUseCase: CalculateDdayUseCase,
    private val resolveGifticonAvailabilityUseCase: ResolveGifticonAvailabilityUseCase
) {

    operator fun invoke(isUsed: Boolean, expiryDate: Date): String {
        return when (resolveGifticonAvailabilityUseCase(isUsed, expiryDate)) {
            GifticonAvailability.USED -> "사용완료"
            GifticonAvailability.EXPIRED -> "만료"
            GifticonAvailability.AVAILABLE -> "D-${calculateDdayUseCase(expiryDate)}"
        }
    }
}
