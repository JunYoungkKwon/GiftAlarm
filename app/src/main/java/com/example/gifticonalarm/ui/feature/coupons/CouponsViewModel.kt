package com.example.gifticonalarm.ui.feature.coupons

import androidx.lifecycle.LiveData
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

    val coupons: LiveData<List<CouponUiModel>> = getAllGifticonsUseCase().map { gifticons ->
        gifticons.map { toCouponUiModel(it) }
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
