package com.example.gifticonalarm.ui.feature.shared.barcodelarge
import com.example.gifticonalarm.ui.feature.shared.text.VoucherText

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * 바코드 크게 보기 라우트 진입점.
 */
@Composable
fun BarcodeLargeRoute(
    couponId: String,
    onCloseClick: () -> Unit,
    viewModel: BarcodeLargeViewModel = hiltViewModel()
) {
    val uiState by viewModel.getUiState(couponId).observeAsState(BarcodeLargeUiState.NotFound)

    when (val state = uiState) {
        is BarcodeLargeUiState.Ready -> {
            BarcodeLargeScreen(
                uiModel = state.uiModel,
                onCloseClick = onCloseClick
            )
        }

        BarcodeLargeUiState.NotFound -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = VoucherText.BARCODE_NOT_FOUND,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
