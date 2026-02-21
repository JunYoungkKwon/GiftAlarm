package com.example.gifticonalarm.ui.feature.home

import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.ui.feature.home.model.HomeBadgeType
import com.example.gifticonalarm.ui.feature.home.model.HomeSortType
import com.example.gifticonalarm.ui.feature.shared.text.HomeText
import com.example.gifticonalarm.ui.feature.shared.text.TextFormatters

fun List<Gifticon>.sortedBySortType(sortType: HomeSortType): List<Gifticon> {
    return when (sortType) {
        HomeSortType.LATEST -> sortedByDescending { it.lastModifiedAt.time }
        HomeSortType.EXPIRY_SOON -> sortedBy { it.expiryDate.time }
        HomeSortType.EXPIRY_LATE -> sortedByDescending { it.expiryDate.time }
    }
}

fun resolveHomeBadgeType(isUsed: Boolean, dday: Long): HomeBadgeType {
    return when {
        isUsed -> HomeBadgeType.USED
        dday < 0 -> HomeBadgeType.EXPIRED
        dday in 0L..7L -> HomeBadgeType.URGENT
        dday in 8L..15L -> HomeBadgeType.NORMAL
        else -> HomeBadgeType.SAFE
    }
}

fun focusDdayLabel(dday: Long): String {
    return if (dday < 0) HomeText.EXPIRED else TextFormatters.ddayLabel(dday.toInt())
}
