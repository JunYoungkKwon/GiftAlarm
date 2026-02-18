package com.example.gifticonalarm.ui.onboarding

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * 온보딩 라우트 진입점.
 */
@Composable
fun OnboardingRoute(
    onNavigateToHome: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    OnboardingScreen(
        onStartClick = {
            viewModel.completeOnboarding {
                onNavigateToHome()
            }
        }
    )
}
