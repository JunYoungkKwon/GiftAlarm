package com.example.gifticonalarm.ui.feature.shared.validation

/**
 * 문자열 필수 입력 검증.
 */
fun validateRequired(value: String, message: String): String? {
    return if (value.isBlank()) message else null
}

/**
 * 양수 금액 검증.
 */
fun validatePositiveAmount(amount: Int, message: String): String? {
    return if (amount <= 0) message else null
}

/**
 * 최대값 초과 검증.
 */
fun validateAtMost(value: Int, max: Int, message: String): String? {
    return if (value > max) message else null
}

/**
 * 여러 검증 결과 중 첫 번째 에러를 반환한다.
 */
fun firstValidationError(vararg errors: String?): String? {
    return errors.firstOrNull { it != null }
}

