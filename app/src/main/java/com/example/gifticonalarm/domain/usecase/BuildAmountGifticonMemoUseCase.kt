package com.example.gifticonalarm.domain.usecase

import com.example.gifticonalarm.domain.model.GifticonType
import java.util.Locale
import javax.inject.Inject

/**
 * 금액권 메모 문자열을 생성한다.
 */
class BuildAmountGifticonMemoUseCase @Inject constructor(
    private val parseGifticonMemoUseCase: ParseGifticonMemoUseCase
) {

    /**
     * 쿠폰 저장 시 금액권 메모를 생성한다.
     */
    fun forRegistration(amountDigits: String, note: String, previousMemo: String?): String? {
        val amountPart = amountDigits.ifBlank { null }?.let { "$AMOUNT_MEMO_PREFIX ${it}원" }
        val notePart = note.trim().ifBlank { null }?.let { "$NOTE_PREFIX $it" }
        val usageLines = parseGifticonMemoUseCase.extractUsageHistory(previousMemo)
            .map { "$USAGE_HISTORY_PREFIX $it" }

        if (amountPart == null && notePart == null && usageLines.isEmpty()) return null

        return buildString {
            amountPart?.let { append(it) }
            notePart?.let {
                if (isNotEmpty()) append('\n')
                append(it)
            }
            usageLines.forEach { usageLine ->
                if (isNotEmpty()) append('\n')
                append(usageLine)
            }
        }
    }

    /**
     * 금액권 사용내역 추가 시 메모를 생성한다.
     */
    fun forUsageHistory(previousMemo: String?, remainAmount: Int, latestUsageEntry: String): String {
        val customMemo = parseGifticonMemoUseCase.extractDisplayMemo(previousMemo, GifticonType.AMOUNT)
        val previousUsageHistory = parseGifticonMemoUseCase.extractUsageHistory(previousMemo)
        val mergedUsageHistory = listOf(latestUsageEntry) + previousUsageHistory

        return buildString {
            append("$AMOUNT_MEMO_PREFIX ${formatAmount(remainAmount)}원")
            if (customMemo.isNotBlank()) {
                append('\n')
                append("$NOTE_PREFIX $customMemo")
            }
            mergedUsageHistory.forEach { history ->
                if (history.isBlank()) return@forEach
                append('\n')
                append("$USAGE_HISTORY_PREFIX $history")
            }
        }
    }

    private fun formatAmount(amount: Int): String {
        return String.format(Locale.getDefault(), "%,d", amount)
    }

    private companion object {
        const val AMOUNT_MEMO_PREFIX = "금액권:"
        const val NOTE_PREFIX = "메모:"
        const val USAGE_HISTORY_PREFIX = "사용내역:"
    }
}
