package com.example.gifticonalarm.ui.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gifticonalarm.ui.onboarding.AppStartViewModel
import kotlinx.coroutines.delay

private const val SPLASH_DURATION_MS = 1200L

/**
 * 앱 시작 분기를 담당하는 스플래시 라우트.
 */
@Composable
fun SplashRoute(
    onNavigateToHome: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    viewModel: AppStartViewModel = hiltViewModel()
) {
    val isOnboardingCompleted by viewModel.isOnboardingCompleted.observeAsState()

    LaunchedEffect(isOnboardingCompleted) {
        val completed = isOnboardingCompleted ?: return@LaunchedEffect
        delay(SPLASH_DURATION_MS)
        if (completed) {
            onNavigateToHome()
        } else {
            onNavigateToOnboarding()
        }
    }

    SplashScreen()
}
