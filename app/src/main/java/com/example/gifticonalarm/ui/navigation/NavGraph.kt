package com.example.gifticonalarm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.gifticonalarm.ui.coupon.detail.CouponDetailScreen
import com.example.gifticonalarm.ui.feature.add.AddGifticonScreen
import com.example.gifticonalarm.ui.feature.detail.DetailScreen
import com.example.gifticonalarm.ui.feature.home.HomeScreen
import com.example.gifticonalarm.ui.feature.home.dashboard.DashboardScreen
import com.example.gifticonalarm.ui.feature.settings.SettingsScreen
import com.example.gifticonalarm.ui.onboarding.OnboardingScreen
import com.example.gifticonalarm.ui.onboarding.OnboardingViewModel

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object Home : Screen("home")
    data object Coupons : Screen("coupons")
    data object Add : Screen("add")
    data object Settings : Screen("settings")
    data object CouponDetail : Screen("coupon_detail/{couponId}") {
        fun createRoute(couponId: String) = "coupon_detail/$couponId"
    }
    data object Detail : Screen("detail/{gifticonId}") {
        fun createRoute(gifticonId: Long) = "detail/$gifticonId"
    }
}

/**
 * App navigation graph.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Onboarding.route) {
            val viewModel: OnboardingViewModel = hiltViewModel()
            OnboardingScreen(
                onSkipClick = {
                    viewModel.completeOnboarding {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
                onStartClick = {
                    viewModel.completeOnboarding {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            DashboardScreen(
                onCouponClick = { couponId ->
                    navController.navigate(Screen.CouponDetail.createRoute(couponId))
                }
            )
        }

        composable(Screen.Coupons.route) {
            HomeScreen(
                onNavigateToAdd = {
                    navController.navigate(Screen.Add.route)
                },
                onNavigateToDetail = { gifticonId ->
                    navController.navigate(Screen.Detail.createRoute(gifticonId))
                }
            )
        }

        composable(Screen.Add.route) {
            AddGifticonScreen(
                onNavigateBack = {
                    navController.navigate(Screen.Coupons.route) {
                        popUpTo(Screen.Coupons.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }

        composable(
            route = Screen.CouponDetail.route,
            arguments = listOf(navArgument("couponId") { type = NavType.StringType })
        ) { backStackEntry ->
            val couponId = backStackEntry.arguments?.getString("couponId") ?: return@composable
            CouponDetailScreen(
                couponId = couponId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("gifticonId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val gifticonId = backStackEntry.arguments?.getLong("gifticonId") ?: return@composable
            DetailScreen(
                gifticonId = gifticonId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
