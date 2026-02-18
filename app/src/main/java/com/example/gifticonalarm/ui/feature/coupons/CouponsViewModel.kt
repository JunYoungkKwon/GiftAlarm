package com.example.gifticonalarm.ui.feature.coupons

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.usecase.GetAllGifticonsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 쿠폰함 화면에서 표시할 쿠폰 목록을 제공하는 ViewModel.
 */
@HiltViewModel
class CouponsViewModel @Inject constructor(
    getAllGifticonsUseCase: GetAllGifticonsUseCase
) : ViewModel() {

    val gifticons: LiveData<List<Gifticon>> = getAllGifticonsUseCase()
}
