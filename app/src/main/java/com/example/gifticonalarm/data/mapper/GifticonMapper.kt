package com.example.gifticonalarm.data.mapper

import com.example.gifticonalarm.data.local.entity.GifticonEntity
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.model.GifticonType
import java.util.Date

fun GifticonEntity.toDomain(): Gifticon {
    return Gifticon(
        id = id,
        name = name,
        brand = brand,
        expiryDate = Date(expiryDate),
        barcode = barcode,
        imageUri = imageUri,
        memo = memo,
        isUsed = isUsed,
        type = runCatching { GifticonType.valueOf(type) }.getOrDefault(GifticonType.EXCHANGE),
        lastModifiedAt = Date(lastModifiedAt)
    )
}

fun Gifticon.toEntity(): GifticonEntity {
    return GifticonEntity(
        id = id,
        name = name,
        brand = brand,
        expiryDate = expiryDate.time,
        barcode = barcode,
        imageUri = imageUri,
        memo = memo,
        isUsed = isUsed,
        type = type.name,
        lastModifiedAt = lastModifiedAt.time
    )
}
