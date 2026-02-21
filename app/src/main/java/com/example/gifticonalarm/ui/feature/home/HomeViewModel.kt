package com.example.gifticonalarm.ui.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.usecase.BuildGifticonStatusLabelUseCase
import com.example.gifticonalarm.domain.usecase.CalculateDdayUseCase
import com.example.gifticonalarm.domain.usecase.FormatGifticonDateUseCase
import com.example.gifticonalarm.domain.usecase.GetAllGifticonsUseCase
import com.example.gifticonalarm.domain.usecase.ObserveHasUnreadNotificationsUseCase
import com.example.gifticonalarm.domain.usecase.ResolveGifticonImageUrlUseCase
import com.example.gifticonalarm.ui.feature.home.model.HomeBadgeType
import com.example.gifticonalarm.ui.feature.home.model.HomeCouponItem
import com.example.gifticonalarm.ui.feature.home.model.HomeFocusItem
import com.example.gifticonalarm.ui.feature.home.model.HomeSortType
import com.example.gifticonalarm.ui.feature.home.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getAllGifticonsUseCase: GetAllGifticonsUseCase,
    observeHasUnreadNotificationsUseCase: ObserveHasUnreadNotificationsUseCase,
    private val calculateDdayUseCase: CalculateDdayUseCase,
    private val buildGifticonStatusLabelUseCase: BuildGifticonStatusLabelUseCase,
    private val formatGifticonDateUseCase: FormatGifticonDateUseCase,
    private val resolveGifticonImageUrlUseCase: ResolveGifticonImageUrlUseCase
) : ViewModel() {

    val gifticons: LiveData<List<Gifticon>> = getAllGifticonsUseCase().asLiveData()
    val hasUnreadNotifications: LiveData<Boolean> = observeHasUnreadNotificationsUseCase().asLiveData()
    private val _selectedSort = MutableLiveData(HomeSortType.LATEST)

    val uiState: LiveData<HomeUiState> = MediatorLiveData<HomeUiState>().apply {
        val update = {
            val coupons = gifticons.value.orEmpty()
            val sort = _selectedSort.value ?: HomeSortType.LATEST
            val hasUnread = hasUnreadNotifications.value ?: false
            value = toHomeUiState(coupons, sort, hasUnread)
        }
        addSource(gifticons) { update() }
        addSource(_selectedSort) { update() }
        addSource(hasUnreadNotifications) { update() }
    }

    fun onSortSelected(sortType: HomeSortType) {
        _selectedSort.value = sortType
    }

    private fun toHomeUiState(
        gifticons: List<Gifticon>,
        selectedSort: HomeSortType,
        hasUnreadNotifications: Boolean
    ): HomeUiState {
        if (gifticons.isEmpty()) {
            return HomeUiState(
                focus = null,
                coupons = emptyList(),
                selectedSort = selectedSort,
                hasUnreadNotifications = hasUnreadNotifications
            )
        }

        val focusTarget = gifticons.firstOrNull {
            !it.isUsed && calculateDdayUseCase(it.expiryDate) >= 0
        }

        val sortedGifticons = gifticons.sortedBySortType(selectedSort)
        val coupons = sortedGifticons.take(6).map { gifticon ->
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
                    id = it.id,
                    brand = it.brand,
                    title = it.name,
                    dday = focusDday(it),
                    expireText = "~ ${formatGifticonDateUseCase(it.expiryDate)} 까지",
                    imageUrl = resolveGifticonImageUrlUseCase(it.id, it.imageUri)
                )
            },
            coupons = coupons,
            selectedSort = selectedSort,
            hasUnreadNotifications = hasUnreadNotifications
        )
    }

    private fun focusDday(gifticon: Gifticon): String {
        val dday = calculateDdayUseCase(gifticon.expiryDate)
        return if (dday < 0) "만료" else "D-$dday"
    }
}

private fun List<Gifticon>.sortedBySortType(sortType: HomeSortType): List<Gifticon> {
    return when (sortType) {
        HomeSortType.LATEST -> sortedByDescending { it.lastModifiedAt.time }
        HomeSortType.EXPIRY_SOON -> sortedBy { it.expiryDate.time }
        HomeSortType.EXPIRY_LATE -> sortedByDescending { it.expiryDate.time }
    }
}

private fun resolveHomeBadgeType(isUsed: Boolean, dday: Long): HomeBadgeType {
    return when {
        isUsed -> HomeBadgeType.USED
        dday < 0 -> HomeBadgeType.EXPIRED
        dday in 0L..7L -> HomeBadgeType.URGENT
        dday in 8L..15L -> HomeBadgeType.NORMAL
        else -> HomeBadgeType.SAFE
    }
}
