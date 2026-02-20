package com.example.gifticonalarm.ui.feature.shared.cashusage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gifticonalarm.ui.common.components.ToastBanner
import kotlinx.coroutines.delay

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

    LaunchedEffect(effect) {
        when (val current = effect) {
            is CashUsageAddEffect.ShowMessage -> {
                toastMessage = current.message
                viewModel.consumeEffect()
            }
            CashUsageAddEffect.Saved -> {
                onNavigateBack()
                viewModel.consumeEffect()
            }
            null -> Unit
        }
    }

    LaunchedEffect(toastMessage) {
        if (toastMessage == null) return@LaunchedEffect
        delay(1900L)
        toastMessage = null
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CashUsageAddScreen(
            uiState = uiState,
            onBackClick = onNavigateBack,
            onAmountChange = viewModel::updateAmount,
            onStoreChange = viewModel::updateStore,
            onUsedAtChange = viewModel::updateUsedAt,
            onSaveClick = viewModel::save
        )
        toastMessage?.let { message ->
            ToastBanner(
                message = message,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            )
        }
    }
}
