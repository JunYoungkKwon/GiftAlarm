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

data class CouponUiModel(
    val id: Long,
    val brand: String,
    val name: String,
    val expiryText: String,
    val statusBadge: String,
    val imageUrl: String,
    val dday: Long?,
    val status: CouponStatus
)
