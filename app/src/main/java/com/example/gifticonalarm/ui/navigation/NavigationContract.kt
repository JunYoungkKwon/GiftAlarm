package com.example.gifticonalarm.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.NamedNavArgument
import androidx.navigation.navArgument

internal const val COUPON_ID_ARG = "couponId"

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object HomeTab : Screen("home")
    data object CouponsTab : Screen("coupons")
    data object AddTab : Screen("coupon_registration") {
        private const val BASE_ROUTE = "coupon_registration"
        const val routeWithArgs: String = "$BASE_ROUTE?$COUPON_ID_ARG={$COUPON_ID_ARG}"

        fun createRoute(couponId: String? = null): String {
            return if (couponId.isNullOrBlank()) {
                BASE_ROUTE
            } else {
                "$BASE_ROUTE?$COUPON_ID_ARG=$couponId"
            }
        }
    }

    data object SettingsTab : Screen("settings")
    data object SettingsNotificationTime : Screen("settings_notification_time")
    data object Notification : Screen("notification")
    data object BarcodeLarge : Screen("barcode_large/{$COUPON_ID_ARG}") {
        private const val BASE_ROUTE = "barcode_large"
        fun createRoute(couponId: String) = "$BASE_ROUTE/$couponId"
    }

    data object CashUsageAdd : Screen("cash_usage_add/{$COUPON_ID_ARG}") {
        private const val BASE_ROUTE = "cash_usage_add"
        fun createRoute(couponId: String) = "$BASE_ROUTE/$couponId"
    }

    data object CashVoucherDetail : Screen("cash_voucher_detail/{$COUPON_ID_ARG}") {
        private const val BASE_ROUTE = "cash_voucher_detail"
        fun createRoute(couponId: String) = "$BASE_ROUTE/$couponId"
    }
}

internal fun couponIdNavArgument(nullable: Boolean = false): NamedNavArgument {
    return navArgument(COUPON_ID_ARG) {
        type = NavType.StringType
        this.nullable = nullable
        if (nullable) defaultValue = null
    }
}
