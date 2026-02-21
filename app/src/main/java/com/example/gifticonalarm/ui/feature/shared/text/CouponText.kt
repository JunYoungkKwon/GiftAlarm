package com.example.gifticonalarm.ui.feature.shared.text

/**
 * 쿠폰함 화면에서 사용하는 문구 모음.
 */
object CouponText {
    const val TITLE_COUPON_LIST = "쿠폰 목록"
    const val DESCRIPTION_ADD_COUPON = "쿠폰 추가"
    const val PLACEHOLDER_SEARCH = "브랜드 또는 상품명 검색"

    const val FILTER_ALL = "전체"
    const val FILTER_AVAILABLE = "사용 가능"
    const val FILTER_USED = "사용 완료"
    const val FILTER_EXPIRED = "만료"

    const val DESCRIPTION_USED_COUPON = "사용완료"

    fun totalCountLabel(totalCount: Int): String = "총 ${totalCount}개"
}
