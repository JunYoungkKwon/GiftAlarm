package com.example.gifticonalarm.domain.model

/**
 * 알림 유형별 ID 규칙을 정의한다.
 */
object NotificationIdPolicy {
    private const val EXPIRING_BASE: Long = 1_000_000L
    private const val NEW_COUPON_BASE: Long = 2_000_000L

    fun expiringSoonId(gifticonId: Long): Long = EXPIRING_BASE + gifticonId

    fun newCouponId(gifticonId: Long): Long = NEW_COUPON_BASE + gifticonId
}

