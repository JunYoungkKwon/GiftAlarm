package com.example.gifticonalarm.ui.feature.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gifticonalarm.ui.feature.add.bottomsheet.ExpirationDate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 쿠폰 등록 화면의 유효기간 바텀시트 상태를 관리하는 ViewModel.
 */
@HiltViewModel
class CouponRegistrationDateViewModel @Inject constructor() : ViewModel() {

    private val _isExpiryBottomSheetVisible = MutableLiveData(false)
    val isExpiryBottomSheetVisible: LiveData<Boolean> = _isExpiryBottomSheetVisible

    private val _selectedExpiryDate = MutableLiveData<ExpirationDate?>(null)
    val selectedExpiryDate: LiveData<ExpirationDate?> = _selectedExpiryDate

    private val _draftExpiryDate = MutableLiveData(ExpirationDate.today())
    val draftExpiryDate: LiveData<ExpirationDate> = _draftExpiryDate

    /** 유효기간 선택 바텀시트를 연다. */
    fun openExpiryBottomSheet() {
        _draftExpiryDate.value = _selectedExpiryDate.value ?: ExpirationDate.today()
        _isExpiryBottomSheetVisible.value = true
    }

    /** 유효기간 선택 바텀시트를 닫는다. */
    fun dismissExpiryBottomSheet() {
        _isExpiryBottomSheetVisible.value = false
    }

    /** 바텀시트에서 날짜 임시 선택값을 갱신한다. */
    fun updateDraftExpiryDate(date: ExpirationDate) {
        _draftExpiryDate.value = date
    }

    /** 임시 선택값을 실제 유효기간으로 반영한다. */
    fun confirmExpiryDate() {
        _selectedExpiryDate.value = _draftExpiryDate.value
        _isExpiryBottomSheetVisible.value = false
    }

    /** 수정 모드에서 유효기간 초기값을 설정한다. */
    fun setSelectedExpiryDate(date: ExpirationDate) {
        _selectedExpiryDate.value = date
        _draftExpiryDate.value = date
    }
}
