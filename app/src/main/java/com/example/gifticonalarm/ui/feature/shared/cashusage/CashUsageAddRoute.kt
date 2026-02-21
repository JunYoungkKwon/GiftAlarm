package com.example.gifticonalarm.ui.feature.shared.cashusage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gifticonalarm.ui.feature.shared.components.BottomToastBanner
import com.example.gifticonalarm.ui.feature.shared.effect.AutoDismissToast
import com.example.gifticonalarm.ui.feature.shared.effect.HandleRouteEffect

/**
 * 금액권 사용 내역 추가 라우트.
 */
@Composable
fun CashUsageAddRoute(
    couponId: String,
    onNavigateBack: () -> Unit,
    viewModel: CashUsageAddViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.observeAsState(CashUsageAddUiState())
    val effect by viewModel.effect.observeAsState()
    var toastMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(couponId) {
        viewModel.load(couponId)
    }

    HandleRouteEffect(
        effect = effect,
        onConsumed = viewModel::consumeEffect
    ) { current ->
        when (current) {
            is CashUsageAddEffect.ShowMessage -> {
                toastMessage = current.message
            }
            CashUsageAddEffect.Saved -> {
                onNavigateBack()
            }
        }
    }

    AutoDismissToast(message = toastMessage, onDismiss = { toastMessage = null })

    Box(modifier = Modifier.fillMaxSize()) {
        CashUsageAddScreen(
            uiState = uiState,
            onBackClick = onNavigateBack,
            onAmountChange = viewModel::updateAmount,
            onStoreChange = viewModel::updateStore,
            onUsedAtChange = viewModel::updateUsedAt,
            onSaveClick = viewModel::save
        )
        BottomToastBanner(message = toastMessage)
    }
}
