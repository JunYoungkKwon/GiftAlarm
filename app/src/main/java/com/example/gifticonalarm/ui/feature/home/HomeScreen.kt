package com.example.gifticonalarm.ui.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.gifticonalarm.ui.couponbox.CouponBoxScreen

/**
 * 쿠폰함 라우트에서 사용하는 화면 래퍼.
 */
@Composable
fun HomeScreen(
    onNavigateToAdd: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    CouponBoxScreen(
        modifier = modifier,
        onAddClick = onNavigateToAdd,
        onCouponClick = onNavigateToDetail
    )
}
