package com.example.gifticonalarm.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gifticonalarm.ui.feature.add.CouponRegistrationRoute
import com.example.gifticonalarm.ui.feature.coupons.CouponsRoute
import com.example.gifticonalarm.ui.feature.home.HomeRoute
import com.example.gifticonalarm.ui.feature.notification.NotificationRoute
import com.example.gifticonalarm.ui.feature.settings.SettingsRoute
import com.example.gifticonalarm.ui.feature.settings.SettingsTimeRoute
import com.example.gifticonalarm.ui.feature.shared.barcodelarge.BarcodeLargeRoute
import com.example.gifticonalarm.ui.feature.shared.cashusage.CashUsageAddRoute
import com.example.gifticonalarm.ui.feature.shared.components.BottomToastBanner
import com.example.gifticonalarm.ui.feature.shared.effect.AutoDismissToast
import com.example.gifticonalarm.ui.feature.shared.text.CommonText
import com.example.gifticonalarm.ui.feature.shared.voucherdetailscreen.VoucherDetailRoute
import com.example.gifticonalarm.ui.onboarding.OnboardingRoute

private const val TOAST_MESSAGE_KEY = "toast_message"

/**
 * App navigation graph.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    var settingsExternalTimeText by rememberSaveable { mutableStateOf<String?>(null) }
    var settingsExternalTimeVersion by rememberSaveable { mutableIntStateOf(0) }
    var settingsExternalToastMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var settingsExternalToastVersion by rememberSaveable { mutableIntStateOf(0) }

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
            arguments = listOf(couponIdNavArgument(nullable = true))
        ) { backStackEntry ->
            val couponId = backStackEntry.arguments?.getString(COUPON_ID_ARG)
            CouponRegistrationRoute(
                couponId = couponId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onRegistrationCompleted = { isEditMode ->
                    navController.navigateToCouponsWithToast(resolveRegistrationToastMessage(isEditMode))
                }
            )
        }

        composable(Screen.SettingsTab.route) {
            SettingsRoute(
                onNavigateToNotificationTime = { navController.navigate(Screen.SettingsNotificationTime.route) },
                externalTimeText = settingsExternalTimeText,
                externalTimeVersion = settingsExternalTimeVersion,
                externalToastMessage = settingsExternalToastMessage,
                externalToastVersion = settingsExternalToastVersion
            )
        }

        composable(Screen.SettingsNotificationTime.route) {
            SettingsTimeRoute(
                onNavigateBack = { navController.popBackStack() },
                onSaveCompleted = { savedTimeText ->
                    applySavedNotificationTime(
                        savedTimeText = savedTimeText,
                        onApplied = { navController.popBackStack() },
                        onTimeTextChanged = { settingsExternalTimeText = it },
                        onToastMessageChanged = { settingsExternalToastMessage = it },
                        onTimeVersionIncrement = { settingsExternalTimeVersion += 1 },
                        onToastVersionIncrement = { settingsExternalToastVersion += 1 }
                    )
                }
            )
        }

        composable(Screen.Notification.route) {
            NotificationRoute(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.CashVoucherDetail.route,
            arguments = listOf(couponIdNavArgument())
        ) { backStackEntry ->
            val couponId = backStackEntry.arguments?.getString(COUPON_ID_ARG) ?: return@composable
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
                onNavigateToCashUsageAdd = { selectedCouponId ->
                    navController.navigate(Screen.CashUsageAdd.createRoute(selectedCouponId))
                },
                onDeleteCompleted = {
                    navController.handleVoucherDeletionCompleted()
                }
            )
        }

        composable(
            route = Screen.BarcodeLarge.route,
            arguments = listOf(couponIdNavArgument())
        ) { backStackEntry ->
            val couponId = backStackEntry.arguments?.getString(COUPON_ID_ARG) ?: return@composable
            BarcodeLargeRoute(
                couponId = couponId,
                onCloseClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.CashUsageAdd.route,
            arguments = listOf(couponIdNavArgument())
        ) { backStackEntry ->
            val couponId = backStackEntry.arguments?.getString(COUPON_ID_ARG) ?: return@composable
            CashUsageAddRoute(
                couponId = couponId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

private fun resolveRegistrationToastMessage(isEditMode: Boolean): String {
    return if (isEditMode) {
        CommonText.TOAST_CHANGES_SAVED
    } else {
        CommonText.TOAST_COUPON_REGISTERED
    }
}

private fun NavHostController.navigateToCouponsWithToast(toastMessage: String) {
    navigate(Screen.CouponsTab.route) {
        launchSingleTop = true
    }
    currentBackStackEntry?.savedStateHandle?.setToastMessage(toastMessage)
    runCatching { getBackStackEntry(Screen.HomeTab.route) }
        .getOrNull()
        ?.savedStateHandle
        ?.setToastMessage(toastMessage)
}

private fun NavHostController.handleVoucherDeletionCompleted() {
    previousBackStackEntry?.savedStateHandle?.setToastMessage(CommonText.TOAST_COUPON_DELETED)
    popBackStack()
    currentBackStackEntry?.savedStateHandle?.setToastMessage(CommonText.TOAST_COUPON_DELETED)
}

private fun SavedStateHandle.setToastMessage(message: String?) {
    set(TOAST_MESSAGE_KEY, message)
}

private fun applySavedNotificationTime(
    savedTimeText: String,
    onApplied: () -> Unit,
    onTimeTextChanged: (String) -> Unit,
    onToastMessageChanged: (String) -> Unit,
    onTimeVersionIncrement: () -> Unit,
    onToastVersionIncrement: () -> Unit
) {
    onTimeTextChanged(savedTimeText)
    onToastMessageChanged(CommonText.TOAST_SETTINGS_TIME_SAVED)
    onTimeVersionIncrement()
    onToastVersionIncrement()
    onApplied()
}

@Composable
private fun ToastAwareTabContent(
    backStackEntry: NavBackStackEntry,
    content: @Composable () -> Unit
) {
    val toastMessage by backStackEntry.savedStateHandle
        .getLiveData<String?>(TOAST_MESSAGE_KEY)
        .observeAsState()

    AutoDismissToast(message = toastMessage, onDismiss = {
        backStackEntry.savedStateHandle[TOAST_MESSAGE_KEY] = null
    })

    Box(modifier = Modifier.fillMaxSize()) {
        content()
        BottomToastBanner(message = toastMessage)
    }
}
