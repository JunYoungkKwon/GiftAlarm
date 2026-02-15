package com.example.gifticonalarm.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.gifticonalarm.domain.usecase.GetOnboardingCompletedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 앱 시작 시 온보딩 완료 상태를 제공하는 ViewModel.
 */
@HiltViewModel
class AppStartViewModel @Inject constructor(
    getOnboardingCompletedUseCase: GetOnboardingCompletedUseCase
) : ViewModel() {

    /**
     * 온보딩 완료 여부.
     */
    val isOnboardingCompleted: LiveData<Boolean> = getOnboardingCompletedUseCase().asLiveData()
}
