package com.example.gifticonalarm.ui.feature.coupons.model

enum class CouponStatus {
    AVAILABLE,
    EXPIRED,
    USED
}

enum class CouponFilterType {
    ALL,
    AVAILABLE,
    USED,
    EXPIRED
}

enum class CouponCategoryType(val label: String) {
    ALL("전체 카테고리"),
    EXCHANGE("교환권"),
    AMOUNT("금액권")
}

data class CouponUiModel(
    val id: Long,
    val brand: String,
    val name: String,
    val expiryText: String,
    val statusBadge: String,
    val imageUrl: String,
    val dday: Long?,
    val status: CouponStatus,
    val category: CouponCategoryType
)
