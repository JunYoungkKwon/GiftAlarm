package com.example.gifticonalarm.domain.usecase

import javax.inject.Inject

/**
 * 쿠폰 이미지를 우선 사용하고, 없으면 기본 이미지를 반환한다.
 */
class ResolveGifticonImageUrlUseCase @Inject constructor() {

    operator fun invoke(id: Long, imageUri: String?): String {
        return imageUri.takeUnless { it.isNullOrBlank() } ?: defaultImage(id)
    }

    private fun defaultImage(@Suppress("UNUSED_PARAMETER") id: Long): String {
        return "android.resource://com.example.gifticonalarm/drawable/default_coupon_image"
    }
}
