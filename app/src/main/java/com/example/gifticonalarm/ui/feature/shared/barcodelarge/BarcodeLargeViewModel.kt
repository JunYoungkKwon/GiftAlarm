package com.example.gifticonalarm.ui.feature.shared.barcodelarge
import com.example.gifticonalarm.ui.feature.shared.text.TextFormatters
import com.example.gifticonalarm.ui.feature.shared.text.CommonText

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.example.gifticonalarm.domain.model.DateFormatPolicy
import com.example.gifticonalarm.domain.usecase.FormatGifticonDateUseCase
import com.example.gifticonalarm.domain.usecase.GetGifticonByIdUseCase
import com.example.gifticonalarm.domain.usecase.ResolveGifticonImageUrlUseCase
import com.example.gifticonalarm.ui.feature.shared.livedata.liveDataOf
import com.example.gifticonalarm.ui.feature.shared.util.parseCouponIdOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 바코드 크게 보기 화면 상태를 관리한다.
 */
@HiltViewModel
class BarcodeLargeViewModel @Inject constructor(
    private val getGifticonByIdUseCase: GetGifticonByIdUseCase,
    private val formatGifticonDateUseCase: FormatGifticonDateUseCase,
    private val resolveGifticonImageUrlUseCase: ResolveGifticonImageUrlUseCase
) : ViewModel() {

    fun getUiState(couponId: String): LiveData<BarcodeLargeUiState> {
        val id = parseCouponIdOrNull(couponId) ?: return liveDataOf(BarcodeLargeUiState.NotFound)
        return getGifticonByIdUseCase(id).asLiveData().map { gifticon ->
            if (gifticon == null) {
                BarcodeLargeUiState.NotFound
            } else {
                BarcodeLargeUiState.Ready(
                    uiModel = BarcodeLargeUiModel(
                        couponId = couponId,
                        title = gifticon.name,
                        exchangePlaceText = gifticon.brand.ifBlank { CommonText.DEFAULT_EXCHANGE_PLACE },
                        productImageUrl = resolveGifticonImageUrlUseCase(gifticon.id, gifticon.imageUri),
                        barcodeNumber = gifticon.barcode,
                        expireDateText = TextFormatters.untilDateWithPostfix(
                            formatGifticonDateUseCase(gifticon.expiryDate, DateFormatPolicy.YMD_DOT)
                        )
                    )
                )
            }
        }
    }
}

/**
 * 바코드 크게 보기 UI 상태.
 */
sealed interface BarcodeLargeUiState {
    data class Ready(val uiModel: BarcodeLargeUiModel) : BarcodeLargeUiState
    data object NotFound : BarcodeLargeUiState
}

/**
 * 바코드 크게 보기 화면 모델.
 */
data class BarcodeLargeUiModel(
    val couponId: String,
    val title: String,
    val exchangePlaceText: String,
    val productImageUrl: String,
    val barcodeNumber: String,
    val expireDateText: String
)
