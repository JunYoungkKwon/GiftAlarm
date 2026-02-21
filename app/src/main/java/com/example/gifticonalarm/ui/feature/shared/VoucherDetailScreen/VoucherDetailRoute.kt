package com.example.gifticonalarm.ui.feature.shared.voucherdetailscreen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gifticonalarm.ui.feature.shared.components.BottomToastBanner
import com.example.gifticonalarm.ui.feature.shared.cashvoucherdetail.CashVoucherDetailScreen
import com.example.gifticonalarm.ui.feature.shared.cashvoucherdetail.CashVoucherDetailUiModel
import com.example.gifticonalarm.ui.feature.shared.effect.AutoDismissToast
import com.example.gifticonalarm.ui.feature.shared.effect.HandleRouteEffect

/**
 * 쿠폰 상세 진입 라우트.
 * 저장된 쿠폰 타입(교환권/금액권)에 따라 상세 화면을 분기한다.
 */
@Composable
fun VoucherDetailRoute(
    couponId: String,
    onNavigateBack: () -> Unit,
    onEditClick: (String) -> Unit,
    onNavigateToLargeBarcode: (String) -> Unit,
    onNavigateToCashUsageAdd: (String) -> Unit,
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

    HandleRouteEffect(
        effect = effect,
        onConsumed = viewModel::consumeEffect
    ) { currentEffect ->
        when (currentEffect) {
            is VoucherDetailEffect.CopyBarcode -> {
                clipboardManager.setPrimaryClip(
                    ClipData.newPlainText("barcode_number", currentEffect.barcodeNumber)
                )
                toastMessage = currentEffect.message
            }
            is VoucherDetailEffect.ShowMessage -> {
                toastMessage = currentEffect.message
            }
            is VoucherDetailEffect.OpenLargeBarcode -> {
                onNavigateToLargeBarcode(currentEffect.couponId)
            }
        }
    }

    AutoDismissToast(message = toastMessage, onDismiss = { toastMessage = null })

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
                    onAddUsageClick = { onNavigateToCashUsageAdd(couponId) },
                    onShowBarcodeClick = {
                        viewModel.requestOpenLargeBarcode(
                            couponId = couponId,
                            barcodeNumber = gifticon?.barcode.orEmpty()
                        )
                    },
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

        BottomToastBanner(message = toastMessage)
    }
}
