package com.example.gifticonalarm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.gifticonalarm.ui.feature.add.CouponRegistrationRoute
import com.example.gifticonalarm.ui.feature.coupons.CouponsRoute
import com.example.gifticonalarm.ui.feature.home.HomeRoute
import com.example.gifticonalarm.ui.feature.settings.SettingsRoute
import com.example.gifticonalarm.ui.feature.shared.voucherdetailscreen.VoucherDetailRoute
import com.example.gifticonalarm.ui.onboarding.OnboardingRoute

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object HomeTab : Screen("home")
    data object CouponsTab : Screen("coupons")
    data object AddTab : Screen("coupon_registration")
    data object SettingsTab : Screen("settings")
    data object CashVoucherDetail : Screen("cash_voucher_detail/{couponId}") {
        fun createRoute(couponId: String) = "cash_voucher_detail/$couponId"
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
            OnboardingRoute(
                onNavigateToHome = {
                    navController.navigate(Screen.HomeTab.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.HomeTab.route) {
            HomeRoute(
                onNavigateToAdd = {
                    navController.navigate(Screen.AddTab.route)
                },
                onNavigateToCashVoucherDetail = { couponId ->
                    navController.navigate(Screen.CashVoucherDetail.createRoute(couponId))
                }
            )
        }

        composable(Screen.CouponsTab.route) {
            CouponsRoute(
                onNavigateToAdd = {
                    navController.navigate(Screen.AddTab.route)
                },
                onNavigateToCashVoucherDetail = { couponId ->
                    navController.navigate(Screen.CashVoucherDetail.createRoute(couponId))
                }
            )
        }

        composable(Screen.AddTab.route) {
            CouponRegistrationRoute(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onRegistrationCompleted = {
                    navController.navigate(Screen.CouponsTab.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.SettingsTab.route) {
            SettingsRoute()
        }

        composable(
            route = Screen.CashVoucherDetail.route,
            arguments = listOf(navArgument("couponId") { type = NavType.StringType })
        ) { backStackEntry ->
            val couponId = backStackEntry.arguments?.getString("couponId") ?: return@composable
            VoucherDetailRoute(
                couponId = couponId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
