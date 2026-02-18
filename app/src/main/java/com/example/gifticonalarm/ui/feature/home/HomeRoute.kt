package com.example.gifticonalarm.ui.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gifticonalarm.domain.model.Gifticon
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * 홈 라우트 진입점.
 */
@Composable
fun HomeRoute(
    onNavigateToAdd: () -> Unit = {},
    onNavigateToCouponDetail: (String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val gifticons by viewModel.gifticons.observeAsState(emptyList())
    val state = gifticons.toHomeUiState()

    HomeScreen(
        state = state,
        onPrimaryActionClick = onNavigateToAdd,
        onCouponClick = { couponId ->
            onNavigateToCouponDetail(couponId.toString())
        }
    )
}

private fun List<Gifticon>.toHomeUiState(): HomeUiState {
    if (isEmpty()) {
        return HomeUiState(
            focus = null,
            coupons = emptyList()
        )
    }

    val focusTarget = firstOrNull { !it.isUsed } ?: first()
    val coupons = take(6).map { gifticon ->
        val dday = calculateDday(gifticon.expiryDate)
        HomeCouponItem(
            id = gifticon.id,
            brand = gifticon.brand,
            name = gifticon.name,
            expireText = if (gifticon.isUsed) {
                "${formatDate(gifticon.expiryDate)} 사용"
            } else {
                "~ ${formatDate(gifticon.expiryDate)}"
            },
            imageUrl = gifticon.imageUri ?: defaultImage(gifticon.id),
            badge = when {
                gifticon.isUsed -> "사용완료"
                dday < 0 -> "만료"
                else -> "D-$dday"
            },
            badgeAtStart = gifticon.isUsed || dday < 0,
            isUsed = gifticon.isUsed
        )
    }

    return HomeUiState(
        focus = HomeFocusItem(
            brand = focusTarget.brand,
            title = focusTarget.name,
            dday = focusDday(focusTarget),
            expireText = "~ ${formatDate(focusTarget.expiryDate)} 까지",
            imageUrl = focusTarget.imageUri ?: defaultImage(focusTarget.id)
        ),
        coupons = coupons
    )
}

private fun focusDday(gifticon: Gifticon): String {
    if (gifticon.isUsed) return "사용완료"
    val dday = calculateDday(gifticon.expiryDate)
    return if (dday < 0) "만료" else "D-$dday"
}

private fun calculateDday(targetDate: java.util.Date): Long {
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
    val target = Calendar.getInstance().apply {
        time = targetDate
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
    return TimeUnit.MILLISECONDS.toDays(target - today)
}

private fun formatDate(date: java.util.Date): String {
    return SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN).format(date)
}

private fun defaultImage(id: Long): String {
    return "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=300&auto=format&fit=crop&q=80&seed=$id"
}
