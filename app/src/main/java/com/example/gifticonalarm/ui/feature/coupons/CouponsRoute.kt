package com.example.gifticonalarm.ui.feature.coupons

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 쿠폰함 라우트 진입점.
 */
@Composable
fun CouponsRoute(
    onNavigateToAdd: () -> Unit,
    onNavigateToCouponDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    CouponBoxScreen(
        modifier = modifier,
        onAddClick = onNavigateToAdd,
        onCouponClick = { couponId ->
            onNavigateToCouponDetail(couponId.toString())
        }
    )
}
