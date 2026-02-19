package com.example.gifticonalarm.domain.usecase

import javax.inject.Inject

/**
 * 쿠폰 이미지를 우선 사용하고, 없으면 기본 이미지를 반환한다.
 */
class ResolveGifticonImageUrlUseCase @Inject constructor() {

    operator fun invoke(id: Long, imageUri: String?): String {
        return imageUri.takeUnless { it.isNullOrBlank() } ?: defaultImage(id)
    }

    private fun defaultImage(id: Long): String {
        return "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=300&auto=format&fit=crop&q=80&seed=$id"
    }
}
