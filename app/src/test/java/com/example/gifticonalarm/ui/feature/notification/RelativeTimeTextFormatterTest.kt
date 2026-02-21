package com.example.gifticonalarm.ui.feature.notification

import java.util.concurrent.TimeUnit
import org.junit.Assert.assertEquals
import org.junit.Test

class RelativeTimeTextFormatterTest {

    @Test
    fun `returns just now for zero or negative`() {
        assertEquals("방금 전", formatRelativeTimeText(0))
        assertEquals("방금 전", formatRelativeTimeText(-10))
    }

    @Test
    fun `returns minutes for under one hour`() {
        assertEquals("5분 전", formatRelativeTimeText(TimeUnit.MINUTES.toMillis(5)))
    }

    @Test
    fun `returns hours for under one day`() {
        assertEquals("3시간 전", formatRelativeTimeText(TimeUnit.HOURS.toMillis(3)))
    }

    @Test
    fun `returns yesterday when one day passed`() {
        assertEquals("어제", formatRelativeTimeText(TimeUnit.DAYS.toMillis(1)))
    }

    @Test
    fun `returns days for two days or more`() {
        assertEquals("4일 전", formatRelativeTimeText(TimeUnit.DAYS.toMillis(4)))
    }
}
