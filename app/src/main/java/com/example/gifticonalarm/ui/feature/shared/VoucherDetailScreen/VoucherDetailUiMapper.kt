package com.example.gifticonalarm.ui.feature.shared.voucherdetailscreen
import com.example.gifticonalarm.ui.feature.shared.text.TextFormatters
import com.example.gifticonalarm.ui.feature.shared.text.VoucherText
import com.example.gifticonalarm.ui.feature.shared.text.CommonText

import com.example.gifticonalarm.domain.model.DateFormatPolicy
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.model.GifticonType
import com.example.gifticonalarm.domain.usecase.BuildGifticonStatusLabelUseCase
import com.example.gifticonalarm.domain.usecase.FormatGifticonDateUseCase
import com.example.gifticonalarm.domain.usecase.ParseGifticonMemoUseCase
import com.example.gifticonalarm.domain.usecase.ResolveGifticonAvailabilityUseCase
import com.example.gifticonalarm.domain.usecase.ResolveGifticonImageUrlUseCase
import com.example.gifticonalarm.ui.feature.shared.cashvoucherdetail.CashVoucherDetailUiModel
import com.example.gifticonalarm.ui.feature.shared.model.toVoucherStatus
import javax.inject.Inject

/**
 * 상세 화면 UI 모델 조립 전담 매퍼.
 */
class VoucherDetailUiMapper @Inject constructor(
    private val resolveGifticonAvailabilityUseCase: ResolveGifticonAvailabilityUseCase,
    private val resolveGifticonImageUrlUseCase: ResolveGifticonImageUrlUseCase,
    private val buildGifticonStatusLabelUseCase: BuildGifticonStatusLabelUseCase,
    private val formatGifticonDateUseCase: FormatGifticonDateUseCase,
    private val parseGifticonMemoUseCase: ParseGifticonMemoUseCase
) {

    fun toCashVoucherUiModel(gifticon: Gifticon?, couponId: String): CashVoucherDetailUiModel {
        if (gifticon == null) return CashVoucherDetailUiModel.placeholder(couponId)

        return CashVoucherDetailUiModel(
            couponId = gifticon.id.toString(),
            brand = gifticon.brand,
            title = gifticon.name,
            status = resolveGifticonAvailabilityUseCase(gifticon.isUsed, gifticon.expiryDate).toVoucherStatus(),
            remainAmountText = parseGifticonMemoUseCase.extractAmountDigits(gifticon.memo)
                .ifBlank { CommonText.PLACEHOLDER_AMOUNT_ZERO },
            usageHistoryText = parseGifticonMemoUseCase.extractUsageHistory(gifticon.memo)
                .joinToString(separator = "\n") { "• $it" },
            expireDateText = TextFormatters.untilDateWithPostfix(
                formatGifticonDateUseCase(gifticon.expiryDate, DateFormatPolicy.YMD_DOT_SPACED)
            ),
            expireBadgeText = detailBadgeLabel(gifticon.expiryDate, gifticon.isUsed),
            barcodeNumber = gifticon.barcode.ifBlank { CommonText.UNREGISTERED },
            exchangePlaceText = gifticon.brand,
            memo = parseGifticonMemoUseCase.extractDisplayMemo(gifticon.memo, GifticonType.AMOUNT)
                .ifBlank { CommonText.PLACEHOLDER_MEMO_EMPTY },
            brandLogoUrl = resolveGifticonImageUrlUseCase(gifticon.id, gifticon.imageUri)
        )
    }

    fun toProductVoucherUiModel(gifticon: Gifticon?, couponId: String): ProductVoucherDetailUiModel {
        if (gifticon == null) return ProductVoucherDetailUiModel.placeholder(couponId)

        return ProductVoucherDetailUiModel(
            couponId = gifticon.id.toString(),
            brand = gifticon.brand,
            productName = gifticon.name,
            status = resolveGifticonAvailabilityUseCase(gifticon.isUsed, gifticon.expiryDate).toVoucherStatus(),
            expireDateText = TextFormatters.untilDateWithPostfix(
                formatGifticonDateUseCase(gifticon.expiryDate, DateFormatPolicy.YMD_DOT_SPACED)
            ),
            expireBadgeText = detailBadgeLabel(gifticon.expiryDate, gifticon.isUsed),
            barcodeNumber = gifticon.barcode.ifBlank { CommonText.UNREGISTERED },
            exchangePlaceText = gifticon.brand.ifBlank { CommonText.DEFAULT_EXCHANGE_PLACE },
            memoText = parseGifticonMemoUseCase.extractDisplayMemo(gifticon.memo, GifticonType.EXCHANGE)
                .ifBlank { CommonText.PLACEHOLDER_MEMO_EMPTY },
            productImageUrl = resolveGifticonImageUrlUseCase(gifticon.id, gifticon.imageUri)
        )
    }

    private fun detailBadgeLabel(expiryDate: java.util.Date, isUsed: Boolean): String {
        return if (isUsed) {
            VoucherText.STATUS_USED
        } else {
            buildGifticonStatusLabelUseCase(isUsed = false, expiryDate = expiryDate)
        }
    }
}
