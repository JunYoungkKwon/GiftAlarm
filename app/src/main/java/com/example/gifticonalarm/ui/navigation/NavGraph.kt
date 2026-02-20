package com.example.gifticonalarm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.compose.ui.unit.dp
import com.example.gifticonalarm.ui.common.components.ToastBanner
import com.example.gifticonalarm.ui.feature.add.CouponRegistrationRoute
import com.example.gifticonalarm.ui.feature.coupons.CouponsRoute
import com.example.gifticonalarm.ui.feature.home.HomeRoute
import com.example.gifticonalarm.ui.feature.notification.NotificationRoute
import com.example.gifticonalarm.ui.feature.settings.SettingsRoute
import com.example.gifticonalarm.ui.feature.shared.barcodelarge.BarcodeLargeRoute
import com.example.gifticonalarm.ui.feature.shared.voucherdetailscreen.VoucherDetailRoute
import com.example.gifticonalarm.ui.onboarding.OnboardingRoute
import kotlinx.coroutines.delay

private const val TOAST_MESSAGE_KEY = "toast_message"
private const val TOAST_REGISTERED = "쿠폰이 등록되었어요."
private const val TOAST_UPDATED = "변경사항이 저장되었어요."
private const val TOAST_DELETED = "쿠폰이 삭제되었어요."

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object HomeTab : Screen("home")
    data object CouponsTab : Screen("coupons")
    data object AddTab : Screen("coupon_registration") {
        const val routeWithArgs: String = "coupon_registration?couponId={couponId}"

        fun createRoute(couponId: String? = null): String {
            return if (couponId.isNullOrBlank()) {
                "coupon_registration"
            } else {
                "coupon_registration?couponId=$couponId"
            }
        }
    }
    data object SettingsTab : Screen("settings")
    data object Notification : Screen("notification")
    data object BarcodeLarge : Screen("barcode_large/{couponId}") {
        fun createRoute(couponId: String) = "barcode_large/$couponId"
    }
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

        composable(Screen.HomeTab.route) { backStackEntry ->
            ToastAwareTabContent(backStackEntry = backStackEntry) {
                HomeRoute(
                    onNavigateToAdd = {
                        navController.navigate(Screen.AddTab.createRoute())
                    },
                    onNavigateToNotification = {
                        navController.navigate(Screen.Notification.route)
                    },
                    onNavigateToCashVoucherDetail = { couponId ->
                        navController.navigate(Screen.CashVoucherDetail.createRoute(couponId))
                    }
                )
            }
        }

        composable(Screen.CouponsTab.route) { backStackEntry ->
            ToastAwareTabContent(backStackEntry = backStackEntry) {
                CouponsRoute(
                    onNavigateToAdd = {
                        navController.navigate(Screen.AddTab.createRoute())
                    },
                    onNavigateToCashVoucherDetail = { couponId ->
                        navController.navigate(Screen.CashVoucherDetail.createRoute(couponId))
                    }
                )
            }
        }

        composable(
            route = Screen.AddTab.routeWithArgs,
            arguments = listOf(
                navArgument("couponId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val couponId = backStackEntry.arguments?.getString("couponId")
            CouponRegistrationRoute(
                couponId = couponId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onRegistrationCompleted = { isEditMode ->
                    val toastMessage = if (isEditMode) {
                        TOAST_UPDATED
                    } else {
                        TOAST_REGISTERED
                    }
                    navController.navigate(Screen.CouponsTab.route) {
                        launchSingleTop = true
                    }
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set(TOAST_MESSAGE_KEY, toastMessage)
                    runCatching { navController.getBackStackEntry(Screen.HomeTab.route) }
                        .getOrNull()
                        ?.savedStateHandle
                        ?.set(TOAST_MESSAGE_KEY, toastMessage)
                }
            )
        }

        composable(Screen.SettingsTab.route) {
            SettingsRoute()
        }

        composable(Screen.Notification.route) {
            NotificationRoute(
                onBackClick = { navController.popBackStack() }
            )
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
                },
                onEditClick = { editCouponId ->
                    navController.navigate(Screen.AddTab.createRoute(editCouponId))
                },
                onNavigateToLargeBarcode = { selectedCouponId ->
                    navController.navigate(Screen.BarcodeLarge.createRoute(selectedCouponId))
                },
                onDeleteCompleted = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(TOAST_MESSAGE_KEY, TOAST_DELETED)
                    navController.popBackStack()
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set(TOAST_MESSAGE_KEY, TOAST_DELETED)
                }
            )
        }

        composable(
            route = Screen.BarcodeLarge.route,
            arguments = listOf(navArgument("couponId") { type = NavType.StringType })
        ) { backStackEntry ->
            val couponId = backStackEntry.arguments?.getString("couponId") ?: return@composable
            BarcodeLargeRoute(
                couponId = couponId,
                onCloseClick = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun ToastAwareTabContent(
    backStackEntry: NavBackStackEntry,
    content: @Composable () -> Unit
) {
    val toastMessage by backStackEntry.savedStateHandle
        .getLiveData<String?>(TOAST_MESSAGE_KEY)
        .observeAsState()

    LaunchedEffect(toastMessage) {
        if (toastMessage == null) return@LaunchedEffect
        delay(1900L)
        backStackEntry.savedStateHandle[TOAST_MESSAGE_KEY] = null
    }

    Box(modifier = Modifier.fillMaxSize()) {
        content()
        toastMessage?.let { message ->
            ToastBanner(
                message = message,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            )
        }
    }
}
