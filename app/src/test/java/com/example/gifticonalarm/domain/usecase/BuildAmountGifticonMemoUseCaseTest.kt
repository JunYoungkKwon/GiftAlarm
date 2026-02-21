package com.example.gifticonalarm.domain.usecase

import java.util.Locale
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class BuildAmountGifticonMemoUseCaseTest {

    private val originalLocale = Locale.getDefault()
    private val parseUseCase = ParseGifticonMemoUseCase()
    private val useCase = BuildAmountGifticonMemoUseCase(parseUseCase)

    @Before
    fun setUp() {
        Locale.setDefault(Locale.US)
    }

    @After
    fun tearDown() {
        Locale.setDefault(originalLocale)
    }

    @Test
    fun `forRegistration returns null when all fields are empty`() {
        assertNull(
            useCase.forRegistration(
                amountDigits = "",
                note = "   ",
                previousMemo = null
            )
        )
    }

    @Test
    fun `forRegistration keeps previous usage history while updating amount and note`() {
        val previousMemo = """
            금액권: 50,000원
            사용내역: 2026.02.18 5,000원 사용
            사용내역: 2026.02.15 3,000원 사용
        """.trimIndent()

        val result = useCase.forRegistration(
            amountDigits = "42000",
            note = "남은 금액 확인",
            previousMemo = previousMemo
        )

        assertEquals(
            """
            금액권: 42000원
            메모: 남은 금액 확인
            사용내역: 2026.02.18 5,000원 사용
            사용내역: 2026.02.15 3,000원 사용
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `forUsageHistory prepends latest usage and preserves custom memo`() {
        val previousMemo = """
            금액권: 50,000원
            메모: 회사 복지포인트
            사용내역: 2026.02.18 5,000원 사용
        """.trimIndent()

        val result = useCase.forUsageHistory(
            previousMemo = previousMemo,
            remainAmount = 44500,
            latestUsageEntry = "2026.02.20 5,500원 사용"
        )

        assertEquals(
            """
            금액권: 44,500원
            메모: 회사 복지포인트
            사용내역: 2026.02.20 5,500원 사용
            사용내역: 2026.02.18 5,000원 사용
            """.trimIndent(),
            result
        )
    }
}
