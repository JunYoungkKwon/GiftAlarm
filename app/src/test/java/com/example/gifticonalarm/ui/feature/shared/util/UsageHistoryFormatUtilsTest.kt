package com.example.gifticonalarm.ui.feature.shared.util

import org.junit.Assert.assertEquals
import org.junit.Test

class UsageHistoryFormatUtilsTest {

    @Test
    fun `buildUsageHistoryEntry formats full entry`() {
        val result = buildUsageHistoryEntry(
            store = "스타벅스 강남점",
            usedAtText = "2026-02-21",
            usedAmount = 4500
        )

        assertEquals("스타벅스 강남점 / 2026-02-21 / 4,500원 사용", result)
    }

    @Test
    fun `buildUsageHistoryEntry falls back store when blank`() {
        val result = buildUsageHistoryEntry(
            store = "  ",
            usedAtText = "2026-02-21",
            usedAmount = 1000
        )

        assertEquals("사용처 미입력 / 2026-02-21 / 1,000원 사용", result)
    }
}
