package com.example.gifticonalarm.ui.feature.home

import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.ui.feature.home.model.HomeBadgeType
import com.example.gifticonalarm.ui.feature.home.model.HomeSortType
import java.util.Date
import org.junit.Assert.assertEquals
import org.junit.Test

class HomeUiMapperUtilsTest {

    @Test
    fun `sortedBySortType sorts by last modified desc when latest`() {
        val older = gifticon(id = 1L, expiry = 2_000L, modified = 1_000L)
        val newer = gifticon(id = 2L, expiry = 1_000L, modified = 3_000L)

        val result = listOf(older, newer).sortedBySortType(HomeSortType.LATEST)

        assertEquals(listOf(2L, 1L), result.map { it.id })
    }

    @Test
    fun `sortedBySortType sorts by expiry asc when expiry soon`() {
        val late = gifticon(id = 1L, expiry = 5_000L, modified = 1_000L)
        val soon = gifticon(id = 2L, expiry = 1_000L, modified = 2_000L)

        val result = listOf(late, soon).sortedBySortType(HomeSortType.EXPIRY_SOON)

        assertEquals(listOf(2L, 1L), result.map { it.id })
    }

    @Test
    fun `resolveHomeBadgeType returns used first`() {
        assertEquals(HomeBadgeType.USED, resolveHomeBadgeType(isUsed = true, dday = 3))
    }

    @Test
    fun `resolveHomeBadgeType returns expected by dday ranges`() {
        assertEquals(HomeBadgeType.EXPIRED, resolveHomeBadgeType(isUsed = false, dday = -1))
        assertEquals(HomeBadgeType.URGENT, resolveHomeBadgeType(isUsed = false, dday = 7))
        assertEquals(HomeBadgeType.NORMAL, resolveHomeBadgeType(isUsed = false, dday = 12))
        assertEquals(HomeBadgeType.SAFE, resolveHomeBadgeType(isUsed = false, dday = 20))
    }

    @Test
    fun `focusDdayLabel returns expired text when negative`() {
        assertEquals("만료", focusDdayLabel(-1))
        assertEquals("D-3", focusDdayLabel(3))
    }

    private fun gifticon(id: Long, expiry: Long, modified: Long): Gifticon {
        return Gifticon(
            id = id,
            name = "name$id",
            brand = "brand$id",
            expiryDate = Date(expiry),
            barcode = "code$id",
            lastModifiedAt = Date(modified)
        )
    }
}
