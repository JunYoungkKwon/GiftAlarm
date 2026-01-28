package com.example.gifticonalarm.ui.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.usecase.DeleteGifticonUseCase
import com.example.gifticonalarm.domain.usecase.GetAllGifticonsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllGifticonsUseCase: GetAllGifticonsUseCase,
    private val deleteGifticonUseCase: DeleteGifticonUseCase
) : ViewModel() {

    val gifticons: LiveData<List<Gifticon>> = getAllGifticonsUseCase()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun deleteGifticon(gifticon: Gifticon) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                deleteGifticonUseCase(gifticon)
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