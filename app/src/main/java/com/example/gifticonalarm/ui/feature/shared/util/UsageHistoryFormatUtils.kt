package com.example.gifticonalarm.ui.feature.shared.util
import com.example.gifticonalarm.ui.feature.shared.text.CommonText


/**
 * 사용내역 1건을 저장 포맷("사용처 / 날짜 / 금액원 사용")으로 만든다.
 */
fun buildUsageHistoryEntry(
    store: String,
    usedAtText: String,
    usedAmount: Int
): String {
    return buildString {
        if (store.isNotBlank()) append(store) else append(CommonText.STORE_NOT_PROVIDED)
        if (usedAtText.isNotBlank()) append("$USAGE_HISTORY_SEPARATOR$usedAtText")
        append("$USAGE_HISTORY_SEPARATOR${formatAmountWithComma(usedAmount)}${CommonText.UNIT_WON} 사용")
    }.trim()
}
