package com.example.gifticonalarm.ui.feature.coupons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.ui.theme.GifticonAlarmTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

private val CouponAccent = Color(0xFF191970)
private val CouponBackground = Color(0xFFFFFFFF)
private val CouponListBackground = Color(0xFFF6F6F8)
private val CouponCardBorder = Color(0xFFE5E7EB)
private val CouponMutedText = Color(0xFF94A3B8)

enum class CouponStatus {
    AVAILABLE,
    EXPIRED,
    USED
}

data class CouponUiModel(
    val id: Long,
    val brand: String,
    val name: String,
    val expiryText: String,
    val statusBadge: String,
    val imageUrl: String,
    val status: CouponStatus
)

/**
 * 쿠폰함 화면 UI.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponBoxScreen(
    modifier: Modifier = Modifier,
    coupons: List<Gifticon> = emptyList(),
    useSampleData: Boolean = false,
    onAddClick: () -> Unit = {},
    onCouponClick: (Long) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    val displayCoupons = remember(coupons, useSampleData) {
        if (useSampleData) sampleCoupons() else coupons.map { it.toCouponUiModel() }
    }
    val filteredCoupons = remember(displayCoupons, searchQuery) {
        if (searchQuery.isBlank()) {
            displayCoupons
        } else {
            displayCoupons.filter {
                it.brand.contains(searchQuery, ignoreCase = true) ||
                    it.name.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = CouponBackground
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CouponBoxHeader(
                onAddClick = onAddClick,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                totalCount = filteredCoupons.size
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CouponListBackground),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredCoupons, key = { it.id }) { coupon ->
                    CouponListItem(
                        coupon = coupon,
                        onClick = { onCouponClick(coupon.id) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CouponBoxHeader(
    onAddClick: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    totalCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CouponBackground)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "쿠폰 목록",
                style = MaterialTheme.typography.headlineSmall,
                color = CouponAccent,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onAddClick) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "쿠폰 추가",
                    tint = CouponAccent
                )
            }
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            placeholder = {
                Text(
                    text = "브랜드 또는 상품명 검색",
                    color = CouponMutedText,
                    style = MaterialTheme.typography.bodySmall
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    tint = CouponMutedText
                )
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF1F5F9),
                unfocusedContainerColor = Color(0xFFF1F5F9),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CouponFilterChip(text = "전체", selected = true)
            CouponFilterChip(text = "사용 가능", selected = false)
            CouponFilterChip(text = "사용 완료", selected = false)
            CouponFilterChip(text = "만료", selected = false)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "전체 카테고리",
                    style = MaterialTheme.typography.labelLarge,
                    color = CouponAccent,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Outlined.ExpandMore,
                    contentDescription = null,
                    tint = CouponAccent,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = "총 ${totalCount}개",
                style = MaterialTheme.typography.labelSmall,
                color = CouponMutedText
            )
        }
    }
}

@Composable
private fun CouponFilterChip(
    text: String,
    selected: Boolean
) {
    AssistChip(
        onClick = {},
        label = {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (selected) CouponAccent else Color(0xFFE2E8F0),
            labelColor = if (selected) Color.White else Color(0xFF64748B)
        ),
        border = null,
        shape = RoundedCornerShape(999.dp)
    )
}

@Composable
private fun CouponListItem(
    coupon: CouponUiModel,
    onClick: () -> Unit
) {
    val badgeColors = when (coupon.status) {
        CouponStatus.AVAILABLE -> Color(0xFFDDE5FF) to CouponAccent
        CouponStatus.EXPIRED -> Color(0xFFFEE2E2) to Color(0xFFEF4444)
        CouponStatus.USED -> Color(0xFFE2E8F0) to Color(0xFF64748B)
    }

    val expiryColor = when (coupon.status) {
        CouponStatus.EXPIRED -> Color(0xFFEF4444)
        CouponStatus.USED -> Color(0xFF94A3B8)
        CouponStatus.AVAILABLE -> Color(0xFF64748B)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (coupon.status == CouponStatus.USED) Color(0xFFF8FAFC) else Color.White)
            .border(1.dp, CouponCardBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF1F5F9)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = coupon.imageUrl,
                contentDescription = coupon.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            if (coupon.status == CouponStatus.USED) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x66000000)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = coupon.brand,
                style = MaterialTheme.typography.labelSmall,
                color = if (coupon.status == CouponStatus.USED) Color(0xFF94A3B8) else CouponAccent,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = coupon.name,
                style = MaterialTheme.typography.titleSmall,
                color = if (coupon.status == CouponStatus.USED) Color(0xFF64748B) else Color(0xFF0F172A),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                textDecoration = if (coupon.status == CouponStatus.USED) TextDecoration.LineThrough else null
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = coupon.expiryText,
                    style = MaterialTheme.typography.labelSmall,
                    color = expiryColor,
                    fontWeight = if (coupon.status == CouponStatus.EXPIRED) FontWeight.Bold else FontWeight.Medium
                )
                Box(
                    modifier = Modifier
                        .background(badgeColors.first, RoundedCornerShape(6.dp))
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = coupon.statusBadge,
                        style = MaterialTheme.typography.labelSmall,
                        color = badgeColors.second,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun Gifticon.toCouponUiModel(): CouponUiModel {
    val dday = calculateDday(expiryDate)
    val status = when {
        isUsed -> CouponStatus.USED
        dday < 0 -> CouponStatus.EXPIRED
        else -> CouponStatus.AVAILABLE
    }
    return CouponUiModel(
        id = id,
        brand = brand,
        name = name,
        expiryText = when (status) {
            CouponStatus.USED -> "${formatDate(expiryDate)} 사용"
            else -> "~${formatDate(expiryDate)} 까지"
        },
        statusBadge = when (status) {
            CouponStatus.USED -> "사용 완료"
            CouponStatus.EXPIRED -> "만료"
            CouponStatus.AVAILABLE -> "D-$dday"
        },
        imageUrl = imageUri.takeUnless { it.isNullOrBlank() } ?: defaultImage(id),
        status = status
    )
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

private fun sampleCoupons(): List<CouponUiModel> = listOf(
    CouponUiModel(
        id = 1,
        brand = "스타벅스",
        name = "아이스 카페 아메리카노 T",
        expiryText = "~2024.05.20 까지",
        statusBadge = "D-15",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDGSCo2UV4Y1_wz3-x4HeZV2PHh-5F2YwaVgskbyNACc0cETCrvJh4nnTgWyN1YmV3Xceu3mW70hMX1wY0RMu_42CnKVMnmq5oloMq1G-uNK02WhkWNG6YxbQEWwviPhstfUem38pASFWMyu8bjpb8ODKVJBCwHodsYnDenNe-liB0BrML8uG2KKE5c3mIsoo7I1jalz2pkVZIJMQ15QueNkF2VjYKD53pcJto1lES4Nxge8cvTmybEndSdTRDSUXxeAUjdBHrm2E1j",
        status = CouponStatus.AVAILABLE
    )
)

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun CouponBoxScreenPreview() {
    GifticonAlarmTheme {
        CouponBoxScreen(useSampleData = true)
    }
}
