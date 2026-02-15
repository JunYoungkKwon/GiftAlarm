package com.example.gifticonalarm.domain.usecase

import com.example.gifticonalarm.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 온보딩 완료 상태를 조회하는 유스케이스.
 */
class GetOnboardingCompletedUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {

    /**
     * 온보딩 완료 여부 스트림을 반환한다.
     */
    operator fun invoke(): Flow<Boolean> = onboardingRepository.observeOnboardingCompleted()
}
