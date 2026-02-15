package com.example.gifticonalarm.domain.usecase

import com.example.gifticonalarm.domain.repository.OnboardingRepository
import javax.inject.Inject

/**
 * 온보딩 완료 상태를 저장하는 유스케이스.
 */
class SetOnboardingCompletedUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {

    /**
     * 온보딩 완료 여부를 저장한다.
     */
    suspend operator fun invoke(completed: Boolean) {
        onboardingRepository.setOnboardingCompleted(completed)
    }
}
