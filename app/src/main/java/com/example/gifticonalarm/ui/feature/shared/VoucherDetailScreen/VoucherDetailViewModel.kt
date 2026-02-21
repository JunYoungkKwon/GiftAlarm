package com.example.gifticonalarm.ui.feature.shared.voucherdetailscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.model.GifticonType
import com.example.gifticonalarm.domain.usecase.DeleteGifticonUseCase
import com.example.gifticonalarm.domain.usecase.GetGifticonByIdUseCase
import com.example.gifticonalarm.domain.usecase.UpdateGifticonUseCase
import com.example.gifticonalarm.ui.feature.shared.cashvoucherdetail.CashVoucherDetailUiModel
import com.example.gifticonalarm.ui.feature.shared.livedata.consumeEffect
import com.example.gifticonalarm.ui.feature.shared.livedata.emitEffect
import com.example.gifticonalarm.ui.feature.shared.livedata.nullLiveData
import com.example.gifticonalarm.ui.feature.shared.text.CommonText
import com.example.gifticonalarm.ui.feature.shared.util.normalizeRegisteredBarcodeOrNull
import com.example.gifticonalarm.ui.feature.shared.util.parseCouponIdOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 상세 화면 상태.
 */
sealed interface VoucherDetailUiState {
    data class Cash(val uiModel: CashVoucherDetailUiModel) : VoucherDetailUiState
    data class Product(val uiModel: ProductVoucherDetailUiModel) : VoucherDetailUiState
}

/**
 * 상세 화면 단발성 이벤트.
 */
sealed interface VoucherDetailEffect {
    data class CopyBarcode(
        val barcodeNumber: String,
        val message: String
    ) : VoucherDetailEffect
    data class ShowMessage(val message: String) : VoucherDetailEffect
    data class OpenLargeBarcode(val couponId: String) : VoucherDetailEffect
}

/**
 * 상세 라우트 전용 ViewModel.
 */
@HiltViewModel
class VoucherDetailViewModel @Inject constructor(
    private val getGifticonByIdUseCase: GetGifticonByIdUseCase,
    private val deleteGifticonUseCase: DeleteGifticonUseCase,
    private val updateGifticonUseCase: UpdateGifticonUseCase,
    private val voucherDetailUiMapper: VoucherDetailUiMapper
) : ViewModel() {
    private val _isDeleted = MutableLiveData(false)
    val isDeleted: LiveData<Boolean> = _isDeleted
    private val _effect = MutableLiveData<VoucherDetailEffect?>()
    val effect: LiveData<VoucherDetailEffect?> = _effect

    fun getGifticon(couponId: String): LiveData<Gifticon?> {
        val id = parseCouponIdOrNull(couponId) ?: return nullLiveData()
        return getGifticonByIdUseCase(id).asLiveData()
    }

    fun getDetailUiState(couponId: String): LiveData<VoucherDetailUiState> {
        return getGifticon(couponId).map { gifticon ->
            when (gifticon?.type) {
                GifticonType.AMOUNT -> VoucherDetailUiState.Cash(
                    uiModel = voucherDetailUiMapper.toCashVoucherUiModel(gifticon, couponId)
                )
                GifticonType.EXCHANGE, null -> VoucherDetailUiState.Product(
                    uiModel = voucherDetailUiMapper.toProductVoucherUiModel(gifticon, couponId)
                )
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

    fun requestBarcodeCopy(barcodeNumber: String) {
        val normalizedBarcode = normalizeRegisteredBarcodeOrNull(barcodeNumber) ?: return
        _effect.emitEffect(VoucherDetailEffect.CopyBarcode(
            barcodeNumber = normalizedBarcode,
            message = CommonText.MESSAGE_BARCODE_COPIED
        ))
    }

    fun requestOpenLargeBarcode(couponId: String, barcodeNumber: String) {
        if (normalizeRegisteredBarcodeOrNull(barcodeNumber) == null) {
            _effect.emitEffect(VoucherDetailEffect.ShowMessage(CommonText.MESSAGE_BARCODE_NOT_REGISTERED))
            return
        }
        _effect.emitEffect(VoucherDetailEffect.OpenLargeBarcode(couponId))
    }

    fun consumeEffect() {
        _effect.consumeEffect()
    }

}
