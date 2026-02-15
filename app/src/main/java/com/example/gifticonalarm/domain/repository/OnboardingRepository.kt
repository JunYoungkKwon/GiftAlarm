package com.example.gifticonalarm.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * 온보딩 완료 상태를 제공하는 저장소 인터페이스.
 */
interface OnboardingRepository {

    /**
     * 온보딩 완료 여부를 관찰한다.
     */
    fun observeOnboardingCompleted(): Flow<Boolean>

    /**
     * 온보딩 완료 여부를 저장한다.
     */
    suspend fun setOnboardingCompleted(completed: Boolean)
}
