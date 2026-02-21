package com.example.gifticonalarm.ui.feature.shared.voucherdetailscreen

import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.model.GifticonType
import com.example.gifticonalarm.domain.usecase.BuildGifticonStatusLabelUseCase
import com.example.gifticonalarm.domain.usecase.CalculateDdayUseCase
import com.example.gifticonalarm.domain.usecase.FormatGifticonDateUseCase
import com.example.gifticonalarm.domain.usecase.ParseGifticonMemoUseCase
import com.example.gifticonalarm.domain.usecase.ResolveGifticonAvailabilityUseCase
import com.example.gifticonalarm.domain.usecase.ResolveGifticonImageUrlUseCase
import com.example.gifticonalarm.ui.feature.shared.model.VoucherStatus
import java.util.Calendar
import java.util.Date
import org.junit.Assert.assertEquals
import org.junit.Test

class VoucherDetailUiMapperTest {

    private val calculateDdayUseCase = CalculateDdayUseCase()
    private val resolveAvailabilityUseCase = ResolveGifticonAvailabilityUseCase(calculateDdayUseCase)
    private val mapper = VoucherDetailUiMapper(
        resolveGifticonAvailabilityUseCase = resolveAvailabilityUseCase,
        resolveGifticonImageUrlUseCase = ResolveGifticonImageUrlUseCase(),
        buildGifticonStatusLabelUseCase = BuildGifticonStatusLabelUseCase(
            calculateDdayUseCase,
            resolveAvailabilityUseCase
        ),
        formatGifticonDateUseCase = FormatGifticonDateUseCase(),
        parseGifticonMemoUseCase = ParseGifticonMemoUseCase()
    )

    @Test
    fun `toCashVoucherUiModel maps defaults when barcode and memo are blank`() {
        val gifticon = Gifticon(
            id = 1L,
            name = "e카드",
            brand = "스타벅스",
            expiryDate = futureDate(days = 10),
            barcode = "",
            memo = null,
            type = GifticonType.AMOUNT,
            isUsed = false
        )

        val uiModel = mapper.toCashVoucherUiModel(gifticon, couponId = "1")

        assertEquals(VoucherStatus.USABLE, uiModel.status)
        assertEquals("0", uiModel.remainAmountText)
        assertEquals("미등록", uiModel.barcodeNumber)
        assertEquals("메모 없음", uiModel.memo)
    }

    @Test
    fun `toProductVoucherUiModel maps used status and preserves barcode`() {
        val gifticon = Gifticon(
            id = 2L,
            name = "아메리카노",
            brand = "스타벅스",
            expiryDate = futureDate(days = 5),
            barcode = "123456789012",
            memo = "직접 입력 메모",
            type = GifticonType.EXCHANGE,
            isUsed = true
        )

        val uiModel = mapper.toProductVoucherUiModel(gifticon, couponId = "2")

        assertEquals(VoucherStatus.USED, uiModel.status)
        assertEquals("123456789012", uiModel.barcodeNumber)
        assertEquals("직접 입력 메모", uiModel.memoText)
    }

    private fun futureDate(days: Int): Date {
        return Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, days) }.time
    }
}
