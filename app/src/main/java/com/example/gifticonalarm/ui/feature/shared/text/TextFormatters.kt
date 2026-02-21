package com.example.gifticonalarm.ui.feature.shared.text

object TextFormatters {
    fun ddayLabel(days: Int): String = "D-$days"
    fun parseDdayOrNull(text: String): Long? = text.trim().removePrefix("D-").toLongOrNull()
    fun usedOn(dateText: String): String = "$dateText 사용"
    fun untilDate(dateText: String): String = "~ $dateText"
    fun untilDateWithPostfix(dateText: String): String = "~ $dateText 까지"
    fun expireDateLabel(dateText: String): String = "${VoucherText.LABEL_EXPIRE_DATE}: $dateText"
    fun exchangePlaceLabel(exchangePlace: String): String = "${VoucherText.LABEL_EXCHANGE_PLACE}: $exchangePlace"
}
