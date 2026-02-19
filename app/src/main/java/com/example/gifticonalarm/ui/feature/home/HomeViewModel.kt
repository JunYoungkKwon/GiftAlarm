package com.example.gifticonalarm.ui.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.usecase.BuildGifticonStatusLabelUseCase
import com.example.gifticonalarm.domain.usecase.CalculateDdayUseCase
import com.example.gifticonalarm.domain.usecase.FormatGifticonDateUseCase
import com.example.gifticonalarm.domain.usecase.GetAllGifticonsUseCase
import com.example.gifticonalarm.domain.usecase.ResolveGifticonImageUrlUseCase
import com.example.gifticonalarm.ui.feature.home.model.HomeBadgeType
import com.example.gifticonalarm.ui.feature.home.model.HomeCouponItem
import com.example.gifticonalarm.ui.feature.home.model.HomeFocusItem
import com.example.gifticonalarm.ui.feature.home.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getAllGifticonsUseCase: GetAllGifticonsUseCase,
    private val calculateDdayUseCase: CalculateDdayUseCase,
    private val buildGifticonStatusLabelUseCase: BuildGifticonStatusLabelUseCase,
    private val formatGifticonDateUseCase: FormatGifticonDateUseCase,
    private val resolveGifticonImageUrlUseCase: ResolveGifticonImageUrlUseCase
) : ViewModel() {

    val gifticons: LiveData<List<Gifticon>> = getAllGifticonsUseCase()
    val uiState: LiveData<HomeUiState> = gifticons.map { toHomeUiState(it) }

    private fun toHomeUiState(gifticons: List<Gifticon>): HomeUiState {
        if (gifticons.isEmpty()) {
            return HomeUiState(
                focus = null,
                coupons = emptyList()
            )
        }

        val focusTarget = gifticons.firstOrNull {
            !it.isUsed && calculateDdayUseCase(it.expiryDate) >= 0
        }

        val coupons = gifticons.take(6).map { gifticon ->
            val dday = calculateDdayUseCase(gifticon.expiryDate)
            val badgeType = resolveHomeBadgeType(
                isUsed = gifticon.isUsed,
                dday = dday
            )
            val statusLabel = buildGifticonStatusLabelUseCase(
                isUsed = gifticon.isUsed,
                expiryDate = gifticon.expiryDate
            )
            HomeCouponItem(
                id = gifticon.id,
                brand = gifticon.brand,
                name = gifticon.name,
                expireText = if (gifticon.isUsed) {
                    "${formatGifticonDateUseCase(gifticon.expiryDate)} 사용"
                } else {
                    "~ ${formatGifticonDateUseCase(gifticon.expiryDate)}"
                },
                imageUrl = resolveGifticonImageUrlUseCase(gifticon.id, gifticon.imageUri),
                badge = statusLabel,
                badgeType = badgeType,
                isUsed = gifticon.isUsed
            )
        }

        return HomeUiState(
            focus = focusTarget?.let {
                HomeFocusItem(
                    brand = it.brand,
                    title = it.name,
                    dday = focusDday(it),
                    expireText = "~ ${formatGifticonDateUseCase(it.expiryDate)} 까지",
                    imageUrl = resolveGifticonImageUrlUseCase(it.id, it.imageUri)
                )
            },
            coupons = coupons
        )
    }

    private fun focusDday(gifticon: Gifticon): String {
        val dday = calculateDdayUseCase(gifticon.expiryDate)
        return if (dday < 0) "만료" else "D-$dday"
    }
}

private fun resolveHomeBadgeType(isUsed: Boolean, dday: Long): HomeBadgeType {
    return when {
        isUsed -> HomeBadgeType.USED
        dday < 0 -> HomeBadgeType.EXPIRED
        dday in 1L..7L -> HomeBadgeType.URGENT
        dday in 8L..15L -> HomeBadgeType.NORMAL
        else -> HomeBadgeType.SAFE
    }
}
