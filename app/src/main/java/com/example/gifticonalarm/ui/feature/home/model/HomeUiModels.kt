package com.example.gifticonalarm.ui.feature.home.model

/**
 * 홈 대시보드용 UI 상태 모델.
 */
data class HomeUiState(
    val focus: HomeFocusItem?,
    val coupons: List<HomeCouponItem>
)

/**
 * Today Focus 영역 모델.
 */
data class HomeFocusItem(
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
