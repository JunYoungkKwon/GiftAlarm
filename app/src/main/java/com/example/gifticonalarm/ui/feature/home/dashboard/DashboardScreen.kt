package com.example.gifticonalarm.ui.feature.home.dashboard

import androidx.compose.runtime.Composable

/**
 * Home dashboard entry that delegates to Stitch-based home UI.
 */
@Composable
fun DashboardScreen(
    onCouponClick: (String) -> Unit = {}
) {
    HomeDashboardScreen(
        onCouponClick = { couponId ->
            onCouponClick(couponId.toString())
        }
    )
}
