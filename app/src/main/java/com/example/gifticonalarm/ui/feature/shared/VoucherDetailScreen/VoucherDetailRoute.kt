package com.example.gifticonalarm.ui.feature.shared.voucherdetailscreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.model.GifticonAvailability
import com.example.gifticonalarm.domain.model.GifticonType
import com.example.gifticonalarm.domain.usecase.BuildGifticonStatusLabelUseCase
import com.example.gifticonalarm.domain.usecase.DeleteGifticonUseCase
import com.example.gifticonalarm.domain.usecase.FormatGifticonDateUseCase
import com.example.gifticonalarm.domain.usecase.GetGifticonByIdUseCase
import com.example.gifticonalarm.domain.usecase.ResolveGifticonAvailabilityUseCase
import com.example.gifticonalarm.domain.usecase.ResolveGifticonImageUrlUseCase
import com.example.gifticonalarm.domain.usecase.UpdateGifticonUseCase
import com.example.gifticonalarm.ui.feature.shared.cashvoucherdetail.CashVoucherDetailScreen
import com.example.gifticonalarm.ui.feature.shared.cashvoucherdetail.CashVoucherDetailUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val AMOUNT_MEMO_PREFIX = "금액권:"
private const val DEFAULT_EXCHANGE_PLACE = "사용처 정보 없음"

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

    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            onDeleteCompleted()
            viewModel.consumeDeleted()
        }
    }

    when (val state = detailUiState) {
        is VoucherDetailUiState.Cash -> {
            CashVoucherDetailScreen(
                couponId = couponId,
                onNavigateBack = onNavigateBack,
                modifier = modifier,
                uiModel = state.uiModel,
                onEditClick = { onEditClick(couponId) },
                onDeleteClick = { viewModel.deleteGifticon(gifticon) }
            )
        }

        is VoucherDetailUiState.Product -> {
            ProductVoucherDetailScreen(
                couponId = couponId,
                onNavigateBack = onNavigateBack,
                modifier = modifier,
                uiModel = state.uiModel,
                onShowBarcodeClick = { viewModel.toggleUsed(gifticon) },
                onEditClick = { onEditClick(couponId) },
                onDeleteClick = { viewModel.deleteGifticon(gifticon) }
            )
        }
    }
}

/**
 * 상세 분기 타입.
 */
enum class VoucherType {
    EXCHANGE,
    AMOUNT
}

sealed interface VoucherDetailUiState {
    data class Cash(val uiModel: CashVoucherDetailUiModel) : VoucherDetailUiState
    data class Product(val uiModel: ProductVoucherDetailUiModel) : VoucherDetailUiState
}

/**
 * 상세 라우트 전용 ViewModel.
 */
@HiltViewModel
class VoucherDetailViewModel @Inject constructor(
    private val getGifticonByIdUseCase: GetGifticonByIdUseCase,
    private val deleteGifticonUseCase: DeleteGifticonUseCase,
    private val updateGifticonUseCase: UpdateGifticonUseCase,
    private val resolveGifticonAvailabilityUseCase: ResolveGifticonAvailabilityUseCase,
    private val resolveGifticonImageUrlUseCase: ResolveGifticonImageUrlUseCase,
    private val buildGifticonStatusLabelUseCase: BuildGifticonStatusLabelUseCase,
    private val formatGifticonDateUseCase: FormatGifticonDateUseCase
) : ViewModel() {
    private val _isDeleted = MutableLiveData(false)
    val isDeleted: LiveData<Boolean> = _isDeleted

    fun getGifticon(couponId: String): LiveData<Gifticon?> {
        val id = couponId.toLongOrNull() ?: return MutableLiveData(null)
        return getGifticonByIdUseCase(id).asLiveData()
    }

    fun getDetailUiState(couponId: String): LiveData<VoucherDetailUiState> {
        return getGifticon(couponId).map { gifticon ->
            when (resolveVoucherType(gifticon)) {
                VoucherType.AMOUNT -> VoucherDetailUiState.Cash(
                    uiModel = gifticon.toCashVoucherUiModel(couponId)
                )
                VoucherType.EXCHANGE -> VoucherDetailUiState.Product(
                    uiModel = gifticon.toProductVoucherUiModel(couponId)
                )
            }
        }
    }

    private fun resolveVoucherType(gifticon: Gifticon?): VoucherType {
        return when (gifticon?.type) {
            GifticonType.AMOUNT -> VoucherType.AMOUNT
            GifticonType.EXCHANGE -> VoucherType.EXCHANGE
            null -> {
                if (gifticon?.memo.orEmpty().startsWith(AMOUNT_MEMO_PREFIX)) {
                    VoucherType.AMOUNT
                } else {
                    VoucherType.EXCHANGE
                }
            }
        }
    }

    fun deleteGifticon(gifticon: Gifticon?) {
        if (gifticon == null) return
        viewModelScope.launch {
            deleteGifticonUseCase(gifticon)
            _isDeleted.value = true
        }
    }

    fun toggleUsed(gifticon: Gifticon?) {
        if (gifticon == null) return
        viewModelScope.launch {
            updateGifticonUseCase(gifticon.copy(isUsed = !gifticon.isUsed))
        }
    }

    fun consumeDeleted() {
        _isDeleted.value = false
    }

    private fun Gifticon?.toCashVoucherUiModel(couponId: String): CashVoucherDetailUiModel {
        if (this == null) return CashVoucherDetailUiModel.placeholder(couponId)

        return CashVoucherDetailUiModel(
            couponId = id.toString(),
            brand = brand,
            title = name,
            status = statusLabel(isUsed, expiryDate),
            remainAmountText = extractAmountText(memo),
            expireDateText = "${formatGifticonDateUseCase(expiryDate, "yyyy. MM. dd")} 까지",
            expireBadgeText = detailBadgeLabel(expiryDate, isUsed),
            barcodeNumber = barcode.ifBlank { "미등록" },
            exchangePlaceText = brand,
            memo = memo.orEmpty().ifBlank { "메모 없음" },
            brandLogoUrl = resolveGifticonImageUrlUseCase(id, imageUri)
        )
    }

    private fun Gifticon?.toProductVoucherUiModel(couponId: String): ProductVoucherDetailUiModel {
        if (this == null) return ProductVoucherDetailUiModel.placeholder(couponId)

        val productStatus = when (resolveGifticonAvailabilityUseCase(isUsed, expiryDate)) {
            GifticonAvailability.USED -> ProductVoucherStatus.USED
            GifticonAvailability.EXPIRED -> ProductVoucherStatus.EXPIRED
            GifticonAvailability.AVAILABLE -> ProductVoucherStatus.USABLE
        }

        return ProductVoucherDetailUiModel(
            couponId = id.toString(),
            brand = brand,
            productName = name,
            status = productStatus,
            expireDateText = "${formatGifticonDateUseCase(expiryDate, "yyyy. MM. dd")} 까지",
            expireBadgeText = detailBadgeLabel(expiryDate, isUsed),
            barcodeNumber = barcode.ifBlank { "미등록" },
            exchangePlaceText = brand.ifBlank { DEFAULT_EXCHANGE_PLACE },
            memoText = memo.orEmpty().ifBlank { "메모 없음" },
            productImageUrl = resolveGifticonImageUrlUseCase(id, imageUri)
        )
    }

    private fun extractAmountText(memo: String?): String {
        val amountValue = memo
            ?.removePrefix(AMOUNT_MEMO_PREFIX)
            ?.removeSuffix("원")
            ?.trim()
            ?.ifBlank { null }
        return amountValue ?: "0"
    }

    private fun detailBadgeLabel(expiryDate: java.util.Date, isUsed: Boolean): String {
        return if (isUsed) {
            "사용 완료"
        } else {
            buildGifticonStatusLabelUseCase(isUsed = false, expiryDate = expiryDate)
        }
    }

    private fun statusLabel(isUsed: Boolean, expiryDate: java.util.Date): String {
        return when (resolveGifticonAvailabilityUseCase(isUsed, expiryDate)) {
            GifticonAvailability.USED -> "사용 완료"
            GifticonAvailability.EXPIRED -> "만료"
            GifticonAvailability.AVAILABLE -> "사용 가능"
        }
    }
}
