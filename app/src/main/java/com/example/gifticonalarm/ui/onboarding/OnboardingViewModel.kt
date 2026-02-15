package com.example.gifticonalarm.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gifticonalarm.domain.usecase.SetOnboardingCompletedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

/**
 * 온보딩 화면 액션을 처리하는 ViewModel.
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val setOnboardingCompletedUseCase: SetOnboardingCompletedUseCase
) : ViewModel() {

    /**
     * 온보딩 완료 상태를 저장한다.
     */
    fun completeOnboarding(onCompleted: () -> Unit) {
        viewModelScope.launch {
            setOnboardingCompletedUseCase(true)
            onCompleted()
        }
    }
}
