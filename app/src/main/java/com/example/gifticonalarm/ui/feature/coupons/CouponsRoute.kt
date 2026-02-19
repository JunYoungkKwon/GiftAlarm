package com.example.gifticonalarm.ui.feature.coupons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gifticonalarm.ui.feature.coupons.model.CouponFilterType

/**
 * 쿠폰함 라우트 진입점.
 */
@Composable
fun CouponsRoute(
    onNavigateToAdd: () -> Unit,
    onNavigateToCashVoucherDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CouponsViewModel = hiltViewModel()
) {
    val coupons by viewModel.coupons.observeAsState(emptyList())
    val searchQuery by viewModel.searchQuery.observeAsState("")
    val selectedFilter by viewModel.selectedFilter.observeAsState(CouponFilterType.ALL)

    CouponBoxScreen(
        coupons = coupons,
        searchQuery = searchQuery,
        selectedFilter = selectedFilter,
        onSearchQueryChange = viewModel::onSearchQueryChanged,
        onFilterSelected = viewModel::onFilterSelected,
        modifier = modifier,
        onAddClick = onNavigateToAdd,
        onCouponClick = { couponId ->
            onNavigateToCashVoucherDetail(couponId.toString())
        }
    )
}
