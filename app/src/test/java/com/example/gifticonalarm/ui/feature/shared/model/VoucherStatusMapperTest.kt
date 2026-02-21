package com.example.gifticonalarm.ui.feature.shared.model

import com.example.gifticonalarm.domain.model.GifticonAvailability
import org.junit.Assert.assertEquals
import org.junit.Test

class VoucherStatusMapperTest {

    @Test
    fun `maps availability to voucher status`() {
        assertEquals(VoucherStatus.USABLE, GifticonAvailability.AVAILABLE.toVoucherStatus())
        assertEquals(VoucherStatus.USED, GifticonAvailability.USED.toVoucherStatus())
        assertEquals(VoucherStatus.EXPIRED, GifticonAvailability.EXPIRED.toVoucherStatus())
    }
}
