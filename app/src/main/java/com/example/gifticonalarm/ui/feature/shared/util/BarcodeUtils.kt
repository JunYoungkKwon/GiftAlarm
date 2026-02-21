package com.example.gifticonalarm.ui.feature.shared.util
import com.example.gifticonalarm.ui.feature.shared.text.CommonText

import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer

/**
 * 바코드 번호를 화면 표기용 4자리 그룹으로 정리한다.
 */
fun formatBarcodeNumberForDisplay(rawBarcodeNumber: String): String {
    val normalized = rawBarcodeNumber.trim()
    if (normalizeRegisteredBarcodeOrNull(normalized) == null) return normalized
    return normalized.replace(" ", "").chunked(4).joinToString(" ")
}

/**
 * 등록된 바코드인지 검증 후 정규화된 문자열을 반환한다.
 */
fun normalizeRegisteredBarcodeOrNull(rawBarcodeNumber: String): String? {
    val normalized = rawBarcodeNumber.trim()
    if (normalized.isBlank() || normalized == CommonText.UNREGISTERED) return null
    return normalized
}

/**
 * Code128 바코드 비트 패턴을 생성한다.
 */
fun encodeCode128ModulesOrNull(barcodeNumber: String, width: Int = 800): BooleanArray? {
    val normalized = normalizeRegisteredBarcodeOrNull(barcodeNumber) ?: return null
    if (!normalized.all { it.code in 32..126 }) return null

    return runCatching {
        val bitMatrix = Code128Writer().encode(normalized, BarcodeFormat.CODE_128, width, 1)
        val row = bitMatrix.height / 2
        BooleanArray(bitMatrix.width) { x -> bitMatrix.get(x, row) }
    }.getOrNull()
}
