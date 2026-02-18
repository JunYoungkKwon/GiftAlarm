package com.example.gifticonalarm.ui.feature.coupons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * 쿠폰함 라우트 진입점.
 */
@Composable
fun CouponsRoute(
    onNavigateToAdd: () -> Unit,
    onNavigateToCouponDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CouponsViewModel = hiltViewModel()
) {
    val gifticons by viewModel.gifticons.observeAsState(emptyList())

    CouponBoxScreen(
        coupons = gifticons,
        modifier = modifier,
        onAddClick = onNavigateToAdd,
        onCouponClick = { couponId ->
            onNavigateToCouponDetail(couponId.toString())
        }
    )
}
