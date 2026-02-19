package com.example.gifticonalarm.ui.feature.coupons

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.model.GifticonAvailability
import com.example.gifticonalarm.domain.usecase.BuildGifticonStatusLabelUseCase
import com.example.gifticonalarm.domain.usecase.CalculateDdayUseCase
import com.example.gifticonalarm.domain.usecase.FormatGifticonDateUseCase
import com.example.gifticonalarm.domain.usecase.GetAllGifticonsUseCase
import com.example.gifticonalarm.domain.usecase.ResolveGifticonAvailabilityUseCase
import com.example.gifticonalarm.domain.usecase.ResolveGifticonImageUrlUseCase
import com.example.gifticonalarm.ui.feature.coupons.model.CouponFilterType
import com.example.gifticonalarm.ui.feature.coupons.model.CouponStatus
import com.example.gifticonalarm.ui.feature.coupons.model.CouponUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 쿠폰함 화면에서 표시할 쿠폰 목록을 제공하는 ViewModel.
 */
@HiltViewModel
class CouponsViewModel @Inject constructor(
    getAllGifticonsUseCase: GetAllGifticonsUseCase,
    private val calculateDdayUseCase: CalculateDdayUseCase,
    private val resolveGifticonAvailabilityUseCase: ResolveGifticonAvailabilityUseCase,
    private val buildGifticonStatusLabelUseCase: BuildGifticonStatusLabelUseCase,
    private val formatGifticonDateUseCase: FormatGifticonDateUseCase,
    private val resolveGifticonImageUrlUseCase: ResolveGifticonImageUrlUseCase
) : ViewModel() {

    private val allCoupons: LiveData<List<CouponUiModel>> = getAllGifticonsUseCase().map { gifticons ->
        gifticons.map { toCouponUiModel(it) }
    }

    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery

    private val _selectedFilter = MutableLiveData(CouponFilterType.ALL)
    val selectedFilter: LiveData<CouponFilterType> = _selectedFilter

    val coupons: LiveData<List<CouponUiModel>> = MediatorLiveData<List<CouponUiModel>>().apply {
        val update = {
            val sourceCoupons = allCoupons.value.orEmpty()
            val query = _searchQuery.value.orEmpty().trim()
            val filter = _selectedFilter.value ?: CouponFilterType.ALL

            val byFilter = when (filter) {
                CouponFilterType.ALL -> sourceCoupons
                CouponFilterType.AVAILABLE -> sourceCoupons.filter { it.status == CouponStatus.AVAILABLE }
                CouponFilterType.USED -> sourceCoupons.filter { it.status == CouponStatus.USED }
                CouponFilterType.EXPIRED -> sourceCoupons.filter { it.status == CouponStatus.EXPIRED }
            }

            value = if (query.isBlank()) {
                byFilter
            } else {
                byFilter.filter {
                    it.brand.contains(query, ignoreCase = true) ||
                        it.name.contains(query, ignoreCase = true)
                }
            }
        }

        addSource(allCoupons) { update() }
        addSource(_searchQuery) { update() }
        addSource(_selectedFilter) { update() }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFilterSelected(filter: CouponFilterType) {
        _selectedFilter.value = filter
    }

    private fun toCouponUiModel(gifticon: Gifticon): CouponUiModel {
        val dday = calculateDdayUseCase(gifticon.expiryDate)
        val availability = resolveGifticonAvailabilityUseCase(
            isUsed = gifticon.isUsed,
            expiryDate = gifticon.expiryDate
        )
        val status = when (availability) {
            GifticonAvailability.USED -> CouponStatus.USED
            GifticonAvailability.EXPIRED -> CouponStatus.EXPIRED
            GifticonAvailability.AVAILABLE -> CouponStatus.AVAILABLE
        }

        return CouponUiModel(
            id = gifticon.id,
            brand = gifticon.brand,
            name = gifticon.name,
            expiryText = when (status) {
                CouponStatus.USED -> "${formatGifticonDateUseCase(gifticon.expiryDate)} 사용"
                else -> "~${formatGifticonDateUseCase(gifticon.expiryDate)} 까지"
            },
            statusBadge = buildGifticonStatusLabelUseCase(
                isUsed = gifticon.isUsed,
                expiryDate = gifticon.expiryDate
            ),
            imageUrl = resolveGifticonImageUrlUseCase(gifticon.id, gifticon.imageUri),
            dday = dday,
            status = status
        )
    }
}
