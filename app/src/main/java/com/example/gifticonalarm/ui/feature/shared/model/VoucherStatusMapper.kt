package com.example.gifticonalarm.ui.feature.shared.model

import com.example.gifticonalarm.domain.model.GifticonAvailability

fun GifticonAvailability.toVoucherStatus(): VoucherStatus {
    return when (this) {
        GifticonAvailability.USED -> VoucherStatus.USED
        GifticonAvailability.EXPIRED -> VoucherStatus.EXPIRED
        GifticonAvailability.AVAILABLE -> VoucherStatus.USABLE
    }
}
