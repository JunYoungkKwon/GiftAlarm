package com.example.gifticonalarm.ui.feature.shared.barcodelarge

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.example.gifticonalarm.domain.usecase.FormatGifticonDateUseCase
import com.example.gifticonalarm.domain.usecase.GetGifticonByIdUseCase
import com.example.gifticonalarm.domain.usecase.ResolveGifticonImageUrlUseCase
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
        val id = couponId.toLongOrNull() ?: return androidx.lifecycle.MutableLiveData(BarcodeLargeUiState.NotFound)
        return getGifticonByIdUseCase(id).asLiveData().map { gifticon ->
            if (gifticon == null) {
                BarcodeLargeUiState.NotFound
            } else {
                BarcodeLargeUiState.Ready(
                    uiModel = BarcodeLargeUiModel(
                        couponId = couponId,
                        title = gifticon.name,
                        exchangePlaceText = gifticon.brand.ifBlank { "사용처 정보 없음" },
                        productImageUrl = resolveGifticonImageUrlUseCase(gifticon.id, gifticon.imageUri),
                        barcodeNumber = gifticon.barcode,
                        expireDateText = "${formatGifticonDateUseCase(gifticon.expiryDate, "yyyy.MM.dd")} 까지"
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
