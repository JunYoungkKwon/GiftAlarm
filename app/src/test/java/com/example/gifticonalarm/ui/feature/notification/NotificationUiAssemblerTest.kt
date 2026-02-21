package com.example.gifticonalarm.ui.feature.notification

import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.model.NotificationInboxState
import com.example.gifticonalarm.domain.model.NotificationSettings
import com.example.gifticonalarm.domain.usecase.CalculateDdayUseCase
import java.util.Calendar
import java.util.Date
import org.junit.Assert.assertEquals
import org.junit.Test

class NotificationUiAssemblerTest {

    private val assembler = NotificationUiAssembler(CalculateDdayUseCase())

    @Test
    fun `build returns expiring and new coupon notifications`() {
        val nowMillis = 1_700_000_000_000L
        val gifticon = Gifticon(
            id = 10L,
            name = "아메리카노",
            brand = "스타벅스",
            expiryDate = futureDate(days = 3),
            barcode = "1234",
            isUsed = false,
            lastModifiedAt = Date(nowMillis - 1_000L)
        )

        val result = assembler.build(
            gifticons = listOf(gifticon),
            settings = NotificationSettings(pushEnabled = true, selectedDays = setOf(3)),
            inboxState = NotificationInboxState(),
            nowMillis = nowMillis
        )

        assertEquals(2, result.size)
        assertEquals(NotificationType.EXPIRING_SOON, result[0].type)
        assertEquals("D-3", result[0].timeText)
        assertEquals(NotificationType.NEW_COUPON, result[1].type)
    }

    @Test
    fun `build excludes deleted notifications`() {
        val nowMillis = 1_700_000_000_000L
        val gifticon = Gifticon(
            id = 22L,
            name = "쿠폰",
            brand = "브랜드",
            expiryDate = futureDate(days = 7),
            barcode = "1234",
            isUsed = false,
            lastModifiedAt = Date(nowMillis - 1_000L)
        )

        val notifications = assembler.build(
            gifticons = listOf(gifticon),
            settings = NotificationSettings(pushEnabled = true, selectedDays = setOf(7)),
            inboxState = NotificationInboxState(
                deletedNotificationIds = setOf(1_000_000L + 22L, 2_000_000L + 22L)
            ),
            nowMillis = nowMillis
        )

        assertEquals(0, notifications.size)
    }

    private fun futureDate(days: Int): Date {
        return Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, days) }.time
    }
}
