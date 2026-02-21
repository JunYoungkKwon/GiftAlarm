package com.example.gifticonalarm.domain.usecase

import com.example.gifticonalarm.domain.model.GifticonType
import javax.inject.Inject

/**
 * 기프티콘 메모 문자열에서 금액/메모/사용내역 정보를 파싱한다.
 */
class ParseGifticonMemoUseCase @Inject constructor() {

    /**
     * 금액권 메모에서 금액 숫자 문자열을 추출한다.
     */
    fun extractAmountDigits(memo: String?): String {
        if (memo.isNullOrBlank()) return ""
        return AMOUNT_REGEX.find(memo)
            ?.groupValues
            ?.getOrNull(1)
            ?.replace(",", "")
            .orEmpty()
    }

    /**
     * 금액권 메모에서 금액을 Int로 추출한다.
     */
    fun extractAmountInt(memo: String?): Int {
        return extractAmountDigits(memo).toIntOrNull() ?: 0
    }

    /**
     * 메모를 화면 노출용 텍스트로 정규화한다.
     */
    fun extractDisplayMemo(memo: String?, type: GifticonType): String {
        val rawMemo = memo.orEmpty().trim()
        if (rawMemo.isBlank()) return ""
        if (type == GifticonType.EXCHANGE && !isAmountMemo(rawMemo)) {
            return rawMemo
        }

        val lines = normalizedLines(rawMemo)
        val explicitNote = lines.firstOrNull { it.startsWith(NOTE_PREFIX) }
            ?.removePrefix(NOTE_PREFIX)
            ?.trim()
        if (!explicitNote.isNullOrBlank()) {
            if (type == GifticonType.AMOUNT) {
                if (explicitNote.startsWith(LEGACY_USAGE_PREFIX)) return ""
                return explicitNote.substringBefore(" | $LEGACY_USAGE_PREFIX").trim()
            }
            return explicitNote
        }

        if (lines.firstOrNull()?.startsWith(AMOUNT_MEMO_PREFIX) == true) {
            return lines.drop(1)
                .filterNot { it.startsWith(USAGE_HISTORY_PREFIX) }
                .filterNot { it.startsWith(LEGACY_USAGE_PREFIX) }
                .joinToString("\n")
                .trim()
        }
        return rawMemo
    }

    /**
     * 메모에서 사용내역 목록을 추출한다.
     */
    fun extractUsageHistory(memo: String?): List<String> {
        val lines = normalizedLines(memo.orEmpty())
        val usageLines = lines
            .filter { it.startsWith(USAGE_HISTORY_PREFIX) }
            .map { it.removePrefix(USAGE_HISTORY_PREFIX).trim() }
            .filter { it.isNotBlank() }
        if (usageLines.isNotEmpty()) return usageLines

        val legacyNote = lines.firstOrNull { it.startsWith(NOTE_PREFIX) }
            ?.removePrefix(NOTE_PREFIX)
            ?.trim()
            .orEmpty()
        if (legacyNote.isBlank()) return emptyList()

        return when {
            legacyNote.contains("| $LEGACY_USAGE_PREFIX") -> {
                listOf(legacyNote.substringAfter("| $LEGACY_USAGE_PREFIX").trim())
            }
            legacyNote.startsWith(LEGACY_USAGE_PREFIX) -> {
                listOf(legacyNote.removePrefix(LEGACY_USAGE_PREFIX).trim())
            }
            else -> emptyList()
        }.filter { it.isNotBlank() }
    }

    /**
     * 메모가 금액권 포맷인지 확인한다.
     */
    fun isAmountMemo(memo: String?): Boolean {
        return memo.orEmpty().trim().startsWith(AMOUNT_MEMO_PREFIX)
    }

    private fun normalizedLines(text: String): List<String> {
        return text.lines()
            .map { it.trim() }
            .filter { it.isNotBlank() }
    }

    private companion object {
        const val AMOUNT_MEMO_PREFIX = "금액권:"
        const val NOTE_PREFIX = "메모:"
        const val USAGE_HISTORY_PREFIX = "사용내역:"
        const val LEGACY_USAGE_PREFIX = "최근 사용:"
        val AMOUNT_REGEX = Regex("""금액권:\s*([0-9,]+)\s*원""")
    }
}

