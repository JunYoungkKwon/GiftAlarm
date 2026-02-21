package com.example.gifticonalarm.ui.feature.shared.model

import com.example.gifticonalarm.ui.feature.shared.text.VoucherText

/**
 * 상세 화면에서 공통으로 사용하는 쿠폰 상태.
 */
enum class VoucherStatus(val label: String) {
    USABLE(VoucherText.STATUS_USABLE),
    USED(VoucherText.STATUS_USED),
    EXPIRED(VoucherText.STATUS_EXPIRED)
}
