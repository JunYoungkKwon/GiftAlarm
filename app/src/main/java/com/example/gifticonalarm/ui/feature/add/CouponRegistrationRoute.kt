package com.example.gifticonalarm.ui.feature.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * 쿠폰 등록 라우트 진입점.
 */
@Composable
fun CouponRegistrationRoute(
    couponId: String?,
    onNavigateBack: () -> Unit,
    onRegistrationCompleted: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CouponRegistrationViewModel = hiltViewModel()
) {
    val editTarget by viewModel.getGifticonForEdit(couponId).observeAsState()

    CouponRegistrationScreen(
        modifier = modifier,
        couponId = couponId,
        editTarget = editTarget,
        onCloseClick = onNavigateBack,
        onRegisterClick = onRegistrationCompleted
    )
}
