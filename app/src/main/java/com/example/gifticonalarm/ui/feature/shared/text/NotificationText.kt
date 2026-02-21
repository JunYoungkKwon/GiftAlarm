package com.example.gifticonalarm.ui.feature.shared.text

object NotificationText {
    const val SCREEN_TITLE = "알림"
    const val EMPTY = "표시할 알림이 없어요."
    const val MARK_ALL_READ = "모두 읽음"
    const val DELETE_ALL = "전체 삭제"
    const val TITLE_NEW_COUPON = "신규 등록"
    const val TITLE_EXPIRING_SOON = "만료 임박"
    const val JUST_NOW = "방금 전"
    const val YESTERDAY = "어제"

    fun newCouponMessage(couponName: String): String = "$couponName 쿠폰이 등록되었습니다."
    fun expiringSoonMessage(couponName: String, days: Int): String = "$couponName 쿠폰 유효기간이 ${days}일 남았습니다."
    fun minutesAgo(minutes: Long): String = "${minutes}분 전"
    fun hoursAgo(hours: Long): String = "${hours}시간 전"
    fun daysAgo(days: Long): String = "${days}일 전"
}
