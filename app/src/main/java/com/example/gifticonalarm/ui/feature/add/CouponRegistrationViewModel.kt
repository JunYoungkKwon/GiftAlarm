package com.example.gifticonalarm.ui.feature.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.model.GifticonType
import com.example.gifticonalarm.domain.usecase.AddGifticonUseCase
import com.example.gifticonalarm.domain.usecase.SaveGifticonImageUseCase
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
    private val saveGifticonImageUseCase: SaveGifticonImageUseCase
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _isRegistered = MutableLiveData(false)
    val isRegistered: LiveData<Boolean> = _isRegistered

    /**
     * 등록 폼 데이터를 실제 기프티콘으로 저장한다.
     */
    fun registerGifticon(
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
                    ?.let { saveGifticonImageUseCase(it) }
                val gifticon = Gifticon(
                    name = name,
                    brand = brand,
                    expiryDate = expiryDate,
                    barcode = barcode,
                    imageUri = persistedImageUri,
                    memo = memo,
                    isUsed = false,
                    type = type
                )
                addGifticonUseCase(gifticon)
                _isRegistered.value = true
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message ?: "쿠폰 저장에 실패했어요."
                _isLoading.value = false
            }
        }
    }

    fun consumeRegistered() {
        _isRegistered.value = false
    }

    fun consumeError() {
        _error.value = null
    }
}
