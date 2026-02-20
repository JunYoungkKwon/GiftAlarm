package com.example.gifticonalarm.ui.feature.shared.voucherdetailscreen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gifticonalarm.ui.common.components.ToastBanner
import com.example.gifticonalarm.ui.feature.shared.cashvoucherdetail.CashVoucherDetailScreen
import com.example.gifticonalarm.ui.feature.shared.cashvoucherdetail.CashVoucherDetailUiModel
import kotlinx.coroutines.delay

/**
 * 쿠폰 상세 진입 라우트.
 * 저장된 쿠폰 타입(교환권/금액권)에 따라 상세 화면을 분기한다.
 */
@Composable
fun VoucherDetailRoute(
    couponId: String,
    onNavigateBack: () -> Unit,
    onEditClick: (String) -> Unit,
    onDeleteCompleted: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VoucherDetailViewModel = hiltViewModel()
) {
    val gifticon by viewModel.getGifticon(couponId).observeAsState()
    val detailUiState by viewModel.getDetailUiState(couponId).observeAsState(
        VoucherDetailUiState.Cash(
            uiModel = CashVoucherDetailUiModel.placeholder(couponId)
        )
    )
    val isDeleted by viewModel.isDeleted.observeAsState(false)
    val effect by viewModel.effect.observeAsState()
    val context = LocalContext.current
    val clipboardManager = remember(context) {
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }
    var toastMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            onDeleteCompleted()
            viewModel.consumeDeleted()
        }
    }

    LaunchedEffect(effect) {
        when (val currentEffect = effect) {
            is VoucherDetailEffect.CopyBarcode -> {
                clipboardManager.setPrimaryClip(
                    ClipData.newPlainText("barcode_number", currentEffect.barcodeNumber)
                )
                toastMessage = currentEffect.message
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

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when (val state = detailUiState) {
            is VoucherDetailUiState.Cash -> {
                CashVoucherDetailScreen(
                    couponId = couponId,
                    onNavigateBack = onNavigateBack,
                    modifier = Modifier.fillMaxSize(),
                    uiModel = state.uiModel,
                    onCopyBarcodeClick = viewModel::requestBarcodeCopy,
                    onEditClick = { onEditClick(couponId) },
                    onDeleteClick = { viewModel.deleteGifticon(gifticon) }
                )
            }

            is VoucherDetailUiState.Product -> {
                ProductVoucherDetailScreen(
                    couponId = couponId,
                    onNavigateBack = onNavigateBack,
                    modifier = Modifier.fillMaxSize(),
                    uiModel = state.uiModel,
                    onShowBarcodeClick = { viewModel.toggleUsed(gifticon) },
                    onCopyBarcodeClick = viewModel::requestBarcodeCopy,
                    onEditClick = { onEditClick(couponId) },
                    onDeleteClick = { viewModel.deleteGifticon(gifticon) }
                )
            }
        }

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
