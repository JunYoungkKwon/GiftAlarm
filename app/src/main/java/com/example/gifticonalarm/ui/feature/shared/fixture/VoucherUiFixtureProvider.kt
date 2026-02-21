package com.example.gifticonalarm.ui.feature.shared.fixture
import com.example.gifticonalarm.ui.feature.shared.text.TextFormatters

import com.example.gifticonalarm.ui.feature.shared.cashvoucherdetail.CashVoucherDetailUiModel
import com.example.gifticonalarm.ui.feature.shared.model.VoucherStatus
import com.example.gifticonalarm.ui.feature.shared.voucherdetailscreen.ProductVoucherDetailUiModel

/**
 * 상세 화면 기본 샘플 UI 모델 제공자.
 */
object VoucherUiFixtureProvider {

    fun cashVoucher(couponId: String): CashVoucherDetailUiModel {
        return CashVoucherDetailUiModel(
            couponId = couponId,
            brand = "스타벅스",
            title = "e카드 5만원 교환권",
            status = VoucherStatus.USABLE,
            remainAmountText = "32,500",
            usageHistoryText = "스타벅스 강남점 / 2026-02-21 / 4,500원 사용",
            expireDateText = TextFormatters.untilDateWithPostfix("2024. 12. 31"),
            expireBadgeText = TextFormatters.ddayLabel(45),
            barcodeNumber = "1234 5678 9012",
            exchangePlaceText = "스타벅스 전국 매장 (일부 특수 매장 제외)",
            memo = "친구 생일 선물로 받은 기프티콘. 유효기간 연장 1회 완료함.",
            brandLogoUrl = ""
        )
    }

    fun productVoucher(couponId: String): ProductVoucherDetailUiModel {
        return ProductVoucherDetailUiModel(
            couponId = couponId,
            brand = "스타벅스",
            productName = "아이스 아메리카노 T",
            status = VoucherStatus.USABLE,
            expireDateText = TextFormatters.untilDateWithPostfix("2024. 12. 31"),
            expireBadgeText = TextFormatters.ddayLabel(45),
            barcodeNumber = "1234 5678 9012",
            exchangePlaceText = "스타벅스 전국 매장 (일부 특수 매장 제외)",
            memoText = "생일 축하해! 시원한 아메리카노 한 잔 하고 힘내렴 :)",
            productImageUrl = ""
        )
    }
}
