package com.example.gifticonalarm.ui.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gifticonalarm.ui.feature.home.model.HomeSortType
import com.example.gifticonalarm.ui.feature.home.model.HomeUiState

/**
 * 홈 라우트 진입점.
 */
@Composable
fun HomeRoute(
    onNavigateToAdd: () -> Unit = {},
    onNavigateToCashVoucherDetail: (String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.observeAsState(
        HomeUiState(
            focus = null,
            coupons = emptyList(),
            selectedSort = HomeSortType.LATEST
        )
    )

    HomeScreen(
        state = state,
        onSortSelected = viewModel::onSortSelected,
        onPrimaryActionClick = onNavigateToAdd,
        onFocusClick = { focusId ->
            onNavigateToCashVoucherDetail(focusId.toString())
        },
        onCouponClick = { couponId ->
            onNavigateToCashVoucherDetail(couponId.toString())
        }
    )
}
