package com.example.gifticonalarm.ui.feature.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.model.GifticonType
import com.example.gifticonalarm.domain.usecase.AddGifticonUseCase
import com.example.gifticonalarm.domain.usecase.GetGifticonByIdUseCase
import com.example.gifticonalarm.domain.usecase.SaveGifticonImageUseCase
import com.example.gifticonalarm.domain.usecase.UpdateGifticonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * 쿠폰 등록 저장 로직을 담당하는 ViewModel.
 */
@HiltViewModel
class CouponRegistrationViewModel @Inject constructor(
    private val addGifticonUseCase: AddGifticonUseCase,
    private val updateGifticonUseCase: UpdateGifticonUseCase,
    private val getGifticonByIdUseCase: GetGifticonByIdUseCase,
    private val saveGifticonImageUseCase: SaveGifticonImageUseCase
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _isRegistered = MutableLiveData(false)
    val isRegistered: LiveData<Boolean> = _isRegistered

    /**
     * 등록/수정 폼 데이터를 실제 기프티콘으로 저장한다.
     */
    fun saveGifticon(
        couponId: String?,
        existingGifticon: Gifticon?,
        name: String,
        brand: String,
        barcode: String,
        expiryDate: Date,
        imageUri: String?,
        memo: String?,
        type: GifticonType
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val persistedImageUri = imageUri
                    ?.takeIf { it.isNotBlank() }
                    ?.let { selectedImageUri ->
                        if (selectedImageUri == existingGifticon?.imageUri) {
                            selectedImageUri
                        } else {
                            saveGifticonImageUseCase(selectedImageUri)
                        }
                    }
                val gifticon = Gifticon(
                    id = couponId?.toLongOrNull() ?: 0L,
                    name = name,
                    brand = brand,
                    expiryDate = expiryDate,
                    barcode = barcode,
                    imageUri = persistedImageUri,
                    memo = memo,
                    isUsed = existingGifticon?.isUsed ?: false,
                    type = type
                )
                if (couponId == null) {
                    addGifticonUseCase(gifticon)
                } else {
                    updateGifticonUseCase(gifticon)
                }
                _isRegistered.value = true
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message ?: "쿠폰 저장에 실패했어요."
                _isLoading.value = false
            }
        }
    }

    /**
     * 수정 대상 쿠폰을 조회한다.
     */
    fun getGifticonForEdit(couponId: String?): LiveData<Gifticon?> {
        val id = couponId?.toLongOrNull() ?: return MutableLiveData(null)
        return getGifticonByIdUseCase(id)
    }

    fun consumeRegistered() {
        _isRegistered.value = false
    }

    fun consumeError() {
        _error.value = null
    }
}
