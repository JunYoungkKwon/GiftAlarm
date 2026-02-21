package com.example.gifticonalarm.domain.usecase

import com.example.gifticonalarm.domain.model.GifticonType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ParseGifticonMemoUseCaseTest {

    private val useCase = ParseGifticonMemoUseCase()

    @Test
    fun `extractAmountDigits returns empty when memo is null or blank`() {
        assertEquals("", useCase.extractAmountDigits(null))
        assertEquals("", useCase.extractAmountDigits("   "))
    }

    @Test
    fun `extractAmountDigits strips comma and returns digits only`() {
        val memo = "금액권: 12,300원\n메모: 테스트"

        assertEquals("12300", useCase.extractAmountDigits(memo))
        assertEquals(12300, useCase.extractAmountInt(memo))
    }

    @Test
    fun `extractDisplayMemo returns raw memo for exchange coupon`() {
        val rawMemo = "직접 입력한 메모"

        assertEquals(rawMemo, useCase.extractDisplayMemo(rawMemo, GifticonType.EXCHANGE))
    }

    @Test
    fun `extractDisplayMemo for amount coupon removes usage tail`() {
        val memo = """
            금액권: 50,000원
            메모: 생일 선물 | 최근 사용: 2026.02.10 3,000원 사용
        """.trimIndent()

        assertEquals("생일 선물", useCase.extractDisplayMemo(memo, GifticonType.AMOUNT))
    }

    @Test
    fun `extractUsageHistory prefers new usage history lines`() {
        val memo = """
            금액권: 50,000원
            메모: 메모 텍스트
            사용내역: 2026.02.20 5,000원 사용
            사용내역: 2026.02.19 2,500원 사용
        """.trimIndent()

        assertEquals(
            listOf("2026.02.20 5,000원 사용", "2026.02.19 2,500원 사용"),
            useCase.extractUsageHistory(memo)
        )
    }

    @Test
    fun `extractUsageHistory supports legacy note format`() {
        val memo = "금액권: 30,000원\n메모: 최근 사용: 2026.02.01 1,000원 사용"

        assertEquals(
            listOf("2026.02.01 1,000원 사용"),
            useCase.extractUsageHistory(memo)
        )
    }

    @Test
    fun `isAmountMemo identifies amount prefix`() {
        assertTrue(useCase.isAmountMemo("금액권: 10,000원"))
        assertTrue(!useCase.isAmountMemo("일반 메모"))
    }
}
