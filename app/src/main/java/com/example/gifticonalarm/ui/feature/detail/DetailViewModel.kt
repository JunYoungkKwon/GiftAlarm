package com.example.gifticonalarm.ui.feature.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.usecase.GetGifticonByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getGifticonByIdUseCase: GetGifticonByIdUseCase
) : ViewModel() {

    fun getGifticon(id: Long): LiveData<Gifticon?> {
        return getGifticonByIdUseCase(id)
    }
}