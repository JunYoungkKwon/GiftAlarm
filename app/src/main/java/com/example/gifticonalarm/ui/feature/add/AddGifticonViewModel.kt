package com.example.gifticonalarm.ui.feature.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.usecase.AddGifticonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddGifticonViewModel @Inject constructor(
    private val addGifticonUseCase: AddGifticonUseCase
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _success = MutableLiveData(false)
    val success: LiveData<Boolean> = _success

    fun addGifticon(
        name: String,
        brand: String,
        barcode: String,
        expiryDate: Date,
        memo: String?
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val gifticon = Gifticon(
                    name = name,
                    brand = brand,
                    barcode = barcode,
                    expiryDate = expiryDate,
                    memo = memo
                )

                addGifticonUseCase(gifticon)
                _success.value = true
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun onErrorShown() {
        _error.value = null
    }
}