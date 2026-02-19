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
import androidx.lifecycle.viewModelScope
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.model.GifticonType
import com.example.gifticonalarm.domain.usecase.DeleteGifticonUseCase
import com.example.gifticonalarm.domain.usecase.GetGifticonByIdUseCase
import com.example.gifticonalarm.ui.feature.shared.cashvoucherdetail.CashVoucherDetailScreen
import com.example.gifticonalarm.ui.feature.shared.cashvoucherdetail.CashVoucherDetailUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit
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
    val isDeleted by viewModel.isDeleted.observeAsState(false)
    val voucherType = viewModel.resolveVoucherType(gifticon)

    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            onDeleteCompleted()
            viewModel.consumeDeleted()
        }
    }

    when (voucherType) {
        VoucherType.AMOUNT -> {
            CashVoucherDetailScreen(
                couponId = couponId,
                onNavigateBack = onNavigateBack,
                modifier = modifier,
                uiModel = gifticon.toCashVoucherUiModel(couponId),
                onEditClick = { onEditClick(couponId) },
                onDeleteClick = { viewModel.deleteGifticon(gifticon) }
            )
        }

        VoucherType.EXCHANGE -> {
            ProductVoucherDetailScreen(
                couponId = couponId,
                onNavigateBack = onNavigateBack,
                modifier = modifier,
                uiModel = gifticon.toProductVoucherUiModel(couponId),
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

/**
 * 상세 라우트 전용 ViewModel.
 */
@HiltViewModel
class VoucherDetailViewModel @Inject constructor(
    private val getGifticonByIdUseCase: GetGifticonByIdUseCase,
    private val deleteGifticonUseCase: DeleteGifticonUseCase
) : ViewModel() {
    private val _isDeleted = MutableLiveData(false)
    val isDeleted: LiveData<Boolean> = _isDeleted

    fun getGifticon(couponId: String): LiveData<Gifticon?> {
        val id = couponId.toLongOrNull() ?: return MutableLiveData(null)
        return getGifticonByIdUseCase(id)
    }

    fun resolveVoucherType(gifticon: Gifticon?): VoucherType {
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

    fun consumeDeleted() {
        _isDeleted.value = false
    }
}

private fun Gifticon?.toCashVoucherUiModel(couponId: String): CashVoucherDetailUiModel {
    if (this == null) return CashVoucherDetailUiModel.placeholder(couponId)

    return CashVoucherDetailUiModel(
        couponId = id.toString(),
        brand = brand,
        title = name,
        status = statusLabel(isUsed, expiryDate),
        remainAmountText = extractAmountText(memo),
        expireDateText = "${formatDate(expiryDate)} 까지",
        expireBadgeText = ddayLabel(expiryDate, isUsed),
        barcodeNumber = barcode.ifBlank { "미등록" },
        exchangePlaceText = brand,
        memo = memo.orEmpty().ifBlank { "메모 없음" },
        brandLogoUrl = imageUri.orEmpty()
    )
}

private fun Gifticon?.toProductVoucherUiModel(couponId: String): ProductVoucherDetailUiModel {
    if (this == null) return ProductVoucherDetailUiModel.placeholder(couponId)

    val productStatus = when {
        isUsed -> ProductVoucherStatus.USED
        isExpired(expiryDate) -> ProductVoucherStatus.EXPIRED
        else -> ProductVoucherStatus.USABLE
    }

    return ProductVoucherDetailUiModel(
        couponId = id.toString(),
        brand = brand,
        productName = name,
        status = productStatus,
        expireDateText = "${formatDate(expiryDate)} 까지",
        expireBadgeText = ddayLabel(expiryDate, isUsed),
        barcodeNumber = barcode.ifBlank { "미등록" },
        exchangePlaceText = brand.ifBlank { DEFAULT_EXCHANGE_PLACE },
        memoText = memo.orEmpty().ifBlank { "메모 없음" },
        productImageUrl = imageUri.orEmpty()
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

private fun formatDate(date: java.util.Date): String {
    return SimpleDateFormat("yyyy. MM. dd", Locale.KOREAN).format(date)
}

private fun ddayLabel(expiryDate: java.util.Date, isUsed: Boolean): String {
    if (isUsed) return "사용 완료"
    val dday = calculateDday(expiryDate)
    return if (dday < 0) "만료" else "D-$dday"
}

private fun statusLabel(isUsed: Boolean, expiryDate: java.util.Date): String {
    return when {
        isUsed -> "사용 완료"
        isExpired(expiryDate) -> "만료"
        else -> "사용 가능"
    }
}

private fun isExpired(date: java.util.Date): Boolean = calculateDday(date) < 0

private fun calculateDday(targetDate: java.util.Date): Long {
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
    val target = Calendar.getInstance().apply {
        time = targetDate
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
    return TimeUnit.MILLISECONDS.toDays(target - today)
}
