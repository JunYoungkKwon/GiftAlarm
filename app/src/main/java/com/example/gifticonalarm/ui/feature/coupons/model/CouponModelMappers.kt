package com.example.gifticonalarm.ui.feature.coupons.model

import com.example.gifticonalarm.domain.model.GifticonAvailability
import com.example.gifticonalarm.domain.model.GifticonType

/**
 * 도메인 쿠폰 가용 상태를 쿠폰함 UI 상태로 변환한다.
 */
fun GifticonAvailability.toCouponStatus(): CouponStatus {
    return when (this) {
        GifticonAvailability.USED -> CouponStatus.USED
        GifticonAvailability.EXPIRED -> CouponStatus.EXPIRED
        GifticonAvailability.AVAILABLE -> CouponStatus.AVAILABLE
    }
}

/**
 * 도메인 쿠폰 타입을 쿠폰함 카테고리로 변환한다.
 */
fun GifticonType.toCouponCategoryType(): CouponCategoryType {
    return when (this) {
        GifticonType.EXCHANGE -> CouponCategoryType.EXCHANGE
        GifticonType.AMOUNT -> CouponCategoryType.AMOUNT
    }
}
