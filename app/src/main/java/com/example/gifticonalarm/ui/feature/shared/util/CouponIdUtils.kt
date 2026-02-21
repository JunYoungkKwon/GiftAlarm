package com.example.gifticonalarm.ui.feature.shared.util

/**
 * 쿠폰 ID 문자열을 Long으로 파싱한다.
 */
fun parseCouponIdOrNull(couponId: String?): Long? {
    return couponId?.toLongOrNull()
}

