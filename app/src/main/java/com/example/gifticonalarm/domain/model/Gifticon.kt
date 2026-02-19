package com.example.gifticonalarm.domain.model

import java.util.Date

/**
 * 저장되는 쿠폰 타입.
 */
enum class GifticonType {
    EXCHANGE,
    AMOUNT
}

data class Gifticon(
    val id: Long = 0,
    val name: String,
    val brand: String,
    val expiryDate: Date,
    val barcode: String,
    val imageUri: String? = null,
    val memo: String? = null,
    val isUsed: Boolean = false,
    val type: GifticonType = GifticonType.EXCHANGE,
    val lastModifiedAt: Date = Date()
)
