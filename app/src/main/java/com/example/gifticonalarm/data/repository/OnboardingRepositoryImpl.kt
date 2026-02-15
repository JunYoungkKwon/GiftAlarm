package com.example.gifticonalarm.data.repository

import com.example.gifticonalarm.data.local.preferences.OnboardingPreferencesDataSource
import com.example.gifticonalarm.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 온보딩 저장소 구현체.
 */
class OnboardingRepositoryImpl @Inject constructor(
    private val onboardingPreferencesDataSource: OnboardingPreferencesDataSource
) : OnboardingRepository {

    override fun observeOnboardingCompleted(): Flow<Boolean> {
        return onboardingPreferencesDataSource.observeOnboardingCompleted()
    }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        onboardingPreferencesDataSource.setOnboardingCompleted(completed)
    }
}
