package com.example.gifticonalarm.ui.feature.home.model

/**
 * 홈 대시보드용 UI 상태 모델.
 */
data class HomeUiState(
    val focus: HomeFocusItem?,
    val coupons: List<HomeCouponItem>,
    val selectedSort: HomeSortType = HomeSortType.LATEST,
    val hasUnreadNotifications: Boolean = false
)

/**
 * Today Focus 영역 모델.
 */
data class HomeFocusItem(
    val id: Long,
    val brand: String,
    val title: String,
    val dday: String,
    val expireText: String,
    val imageUrl: String
)

/**
 * 홈 쿠폰 카드 뱃지 타입.
 */
enum class HomeBadgeType {
    USED,
    EXPIRED,
    URGENT,
    NORMAL,
    SAFE
}

enum class HomeSortType(val label: String) {
    LATEST("최신순"),
    EXPIRY_SOON("유효기간 짧은 순"),
    EXPIRY_LATE("유효기간 긴 순")
}

/**
 * 쿠폰 카드 UI 모델.
 */
data class HomeCouponItem(
    val id: Long,
    val brand: String,
    val name: String,
    val expireText: String,
    val imageUrl: String,
    val badge: String? = null,
    val badgeType: HomeBadgeType = HomeBadgeType.NORMAL,
    val isUsed: Boolean = false
)
