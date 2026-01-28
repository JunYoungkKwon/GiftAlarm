package com.example.gifticonalarm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gifticons")
data class GifticonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val brand: String,
    val expiryDate: Long,
    val barcode: String,
    val imageUri: String?,
    val memo: String?,
    val isUsed: Boolean
)