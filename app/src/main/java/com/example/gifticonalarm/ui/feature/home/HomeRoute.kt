package com.example.gifticonalarm.ui.feature.home

import androidx.compose.runtime.Composable

/**
 * 홈 라우트 진입점.
 */
@Composable
fun HomeRoute(
    onNavigateToAdd: () -> Unit = {},
    onNavigateToCouponDetail: (String) -> Unit = {}
) {
    HomeScreen(
        onPrimaryActionClick = onNavigateToAdd,
        onCouponClick = { couponId ->
            onNavigateToCouponDetail(couponId.toString())
        }
    )
}
