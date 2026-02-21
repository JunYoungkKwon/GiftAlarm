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
import com.example.gifticonalarm.ui.feature.home.model.HomeCouponItem
import com.example.gifticonalarm.ui.feature.home.model.HomeFocusItem
import com.example.gifticonalarm.ui.feature.home.model.HomeSortType
import com.example.gifticonalarm.ui.feature.home.model.HomeUiState
import com.example.gifticonalarm.ui.feature.shared.text.TextFormatters
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
        val update = { value = buildUiState() }
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

        val coupons = gifticons
            .sortedBySortType(selectedSort)
            .take(6)
            .map(::toHomeCouponItem)

        return HomeUiState(
            focus = focusTarget?.let(::toHomeFocusItem),
            coupons = coupons,
            selectedSort = selectedSort,
            hasUnreadNotifications = hasUnreadNotifications
        )
    }

    private fun toHomeCouponItem(gifticon: Gifticon): HomeCouponItem {
        val dday = calculateDdayUseCase(gifticon.expiryDate)
        val formattedDate = formatGifticonDateUseCase(gifticon.expiryDate)
        return HomeCouponItem(
            id = gifticon.id,
            brand = gifticon.brand,
            name = gifticon.name,
            expireText = if (gifticon.isUsed) {
                TextFormatters.usedOn(formattedDate)
            } else {
                TextFormatters.untilDate(formattedDate)
            },
            imageUrl = resolveGifticonImageUrlUseCase(gifticon.id, gifticon.imageUri),
            badge = buildGifticonStatusLabelUseCase(
                isUsed = gifticon.isUsed,
                expiryDate = gifticon.expiryDate
            ),
            badgeType = resolveHomeBadgeType(
                isUsed = gifticon.isUsed,
                dday = dday
            ),
            isUsed = gifticon.isUsed
        )
    }

    private fun toHomeFocusItem(gifticon: Gifticon): HomeFocusItem {
        return HomeFocusItem(
            id = gifticon.id,
            brand = gifticon.brand,
            title = gifticon.name,
            dday = focusDdayLabel(calculateDdayUseCase(gifticon.expiryDate)),
            expireText = TextFormatters.untilDateWithPostfix(
                formatGifticonDateUseCase(gifticon.expiryDate)
            ),
            imageUrl = resolveGifticonImageUrlUseCase(gifticon.id, gifticon.imageUri)
        )
    }

    private fun buildUiState(): HomeUiState {
        return toHomeUiState(
            gifticons = gifticons.value.orEmpty(),
            selectedSort = _selectedSort.value ?: HomeSortType.LATEST,
            hasUnreadNotifications = hasUnreadNotifications.value ?: false
        )
    }
}
