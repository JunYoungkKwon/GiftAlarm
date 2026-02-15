package com.example.gifticonalarm.ui.feature.home.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.gifticonalarm.ui.theme.GifticonAlarmTheme

private val HomeBackground = Color(0xFFFFFFFF)
private val HomePrimary = Color(0xFF191970)
private val HomeSurface = Color(0xFFF6F6F8)
private val HomeTextPrimary = Color(0xFF111827)
private val HomeTextSecondary = Color(0xFF6B7280)

/**
 * Stitch 홈 대시보드 디자인을 반영한 UI 전용 홈 화면.
 */
@Composable
fun HomeDashboardScreen(
    modifier: Modifier = Modifier,
    state: HomeUiState = HomeUiState.preview(),
    onNotificationClick: () -> Unit = {},
    onPrimaryActionClick: () -> Unit = {},
    onSortClick: () -> Unit = {},
    onCouponClick: (Long) -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        containerColor = HomeBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onPrimaryActionClick,
                containerColor = HomePrimary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = "추가")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(HomeBackground),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 92.dp)
        ) {
            item {
                HomeHeader(onNotificationClick = onNotificationClick)
            }
            item {
                FocusSection(focus = state.focus)
            }
            item {
                CouponSection(
                    coupons = state.coupons,
                    onSortClick = onSortClick,
                    onCouponClick = onCouponClick
                )
            }
        }
    }
}

@Composable
private fun HomeHeader(onNotificationClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "기프트노트",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = HomePrimary
        )

        Box {
            IconButton(onClick = onNotificationClick) {
                Icon(
                    imageVector = Icons.Outlined.NotificationsNone,
                    contentDescription = "알림",
                    tint = HomeTextSecondary
                )
            }
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(Color(0xFFEF4444))
            )
        }
    }
}

@Composable
private fun FocusSection(focus: HomeFocusItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "오늘의 포커스",
            style = MaterialTheme.typography.labelMedium,
            color = HomePrimary.copy(alpha = 0.85f),
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
            ) {
                AsyncImage(
                    model = focus.imageUrl,
                    contentDescription = focus.brand,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0x33000000),
                                    Color(0xCC000000)
                                )
                            )
                        )
                )

                Text(
                    text = focus.dday,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(HomePrimary)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = focus.brand,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = focus.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = focus.expireText,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "사용하기",
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CouponSection(
    coupons: List<HomeCouponItem>,
    onSortClick: () -> Unit,
    onCouponClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "나의 쿠폰 목록",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = HomeTextPrimary
            )
            Row(
                modifier = Modifier.clickable(onClick = onSortClick),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "최신순",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF9CA3AF),
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = Icons.Outlined.ExpandMore,
                    contentDescription = null,
                    tint = Color(0xFF9CA3AF),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        val rows = coupons.chunked(2)
        rows.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { coupon ->
                    CouponCard(
                        item = coupon,
                        modifier = Modifier.weight(1f),
                        onClick = { onCouponClick(coupon.id) }
                    )
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun CouponCard(
    item: HomeCouponItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = HomeSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                if (item.badge != null) {
                    Text(
                        text = item.badge,
                        modifier = Modifier
                            .align(if (item.badgeAtStart) Alignment.TopStart else Alignment.TopEnd)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(item.badgeColor)
                            .padding(horizontal = 6.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = item.badgeTextColor,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (item.isUsed) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.12f))
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = item.brand,
                style = MaterialTheme.typography.labelSmall,
                color = if (item.isUsed) Color(0xFF9CA3AF) else HomePrimary,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodySmall,
                color = if (item.isUsed) Color(0xFF9CA3AF) else HomeTextPrimary,
                fontWeight = FontWeight.Bold,
                textDecoration = if (item.isUsed) TextDecoration.LineThrough else TextDecoration.None,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 2.dp)
            )
            Text(
                text = item.expireText,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF9CA3AF),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * 홈 대시보드용 UI 상태 모델.
 */
data class HomeUiState(
    val focus: HomeFocusItem,
    val coupons: List<HomeCouponItem>
) {
    companion object {
        fun preview(): HomeUiState = HomeUiState(
            focus = HomeFocusItem(
                brand = "스타벅스",
                title = "아이스 아메리카노 T",
                dday = "D-3",
                expireText = "~ 2026.12.31 까지",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCoGcJf9XcwdEWX8oroZjaHnMVVflMpBWyzPmTQgOw9DL5HNIXbQSArKd_ipYeqloBEq8XsONIUl2lJH8SphOZpLfdSrHcSn3XbyYSmf9dFcZOKN7XM4KdKkblrS0a6AjpWdeOfLA5zpRzSqzzqNSWuvOVe2heH-kyCwPU3_b85_wu1RC8FovLF_rhf5ijfKhXEa887p22f7xYDZNhRMx4SVzvoPKbTYmGzm0nz3PRcHTNpWogUc99rAXjTGFHC6AxJ4Pz4mwj_ifj7"
            ),
            coupons = listOf(
                HomeCouponItem(
                    id = 1L,
                    brand = "배스킨라빈스",
                    name = "파인트 아이스크림",
                    expireText = "~ 2026.01.02",
                    badge = "D-5",
                    badgeColor = HomePrimary,
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDQHej9W3cndWU9bq0W3mwkdTyCQhihwXx0lrY45AX-7X_cU_ibnTRdKXIbFX60yPyndbsBM6UIyLiHTq4ek67gmFh8PxbLbSSm9SZoO4AAPYdA48tKrcfykIEkstbpulreS01EkFTgY442fY-R9sAmvN65KHBN8-0s7kZcRdwGOkUKfzeqZcTjUOZ_a6rmYnf3HepcHxHkVRFQBnyS3LyNVhn3bfGdtOPF74_aCbReWow6ZgMeTdTMd3f-JXcs1ynvraXMBzlj9LDo"
                ),
                HomeCouponItem(
                    id = 2L,
                    brand = "도미노피자",
                    name = "포테이토 피자 M + 콜라 1.25L",
                    expireText = "~ 2026.01.04",
                    badge = "D-7",
                    badgeColor = Color(0xFFF97316),
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAKDs1dxnNEaaIPXtK4cQHGYXUa2MSwrhRoIesomDG1nIVW1SlPe1y-TZ5dhpDZo5w2IOXnSIvbJ82koYxiD7yCMCGjNQ6VjWG7QLCFKA7BHdsbx7AEQGCaRRCaQBkELA3jPCswdfTJtMDIG2JL2Mfbnj7ga8WclWjU6Z2-d0Sme7LQ-9oYh8soziR3ETFMwVaHBd7J0xK0g_uhDXY-CytPP7kauEbkm2aRM4He56YX_O2bRolKO0sbgTn3RtZ6nJ5vBQqB2YQWlqVG"
                ),
                HomeCouponItem(
                    id = 3L,
                    brand = "올리브영",
                    name = "기프트카드 10,000원권",
                    expireText = "2026.03.15 까지",
                    badge = "사용가능",
                    badgeAtStart = true,
                    badgeColor = Color(0xFFE5E7EB),
                    badgeTextColor = Color(0xFF6B7280),
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDmt4OHdC-0x4TMK6qtsrP3zBXi4vS00kKXmF1H4o6C6_4GDaCTh5ZP4_ui2B8KwXj2-Wjz5pxfqoyJJTo-JYpslqygca9gk9XUByG4Cv1qJZN_MHbUXIpizQhcCE8eKeqs5aSpSJ7qQZqeYyp8mj6mCflpH9vbq7gScLqtOlfgFT40PG4DNx7Jk8F4W0FTSCbg-KezqV_3smF88vqY5J9y1vb5TxR3E-Le1VF78kYOJIV7MJCi0MbmDJtY9Q77g_eosjNKmuhrHiMd"
                ),
                HomeCouponItem(
                    id = 4L,
                    brand = "교촌치킨",
                    name = "허니콤보 웨지감자 세트",
                    expireText = "2026.02.20 까지",
                    badge = "사용가능",
                    badgeAtStart = true,
                    badgeColor = Color(0xFFE5E7EB),
                    badgeTextColor = Color(0xFF6B7280),
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA1vIbbpxXuq6hh4M6YCGfdDCTq8bXKNtHlvlNpPlmItuzBoj_OJBV4eGNNuBvqVTJw6UqZlYPqjyMoegjikF4N1QrDmKqlH5Inf4iO1pNnBdjwDy79n8GCmJ1VNehzHh3A-mangQ3IEx8ZJQp1X7QWJU8Mx3tFamZnwA2mJS8l57QK6mzINIBFMFFCqR1Tij6Kc4QWdNOJ9WfnwNigDugvL8hyzhz9jBV5L0QC4t_sm-4IX6hrieQfePmdm_A0Okv-0y7BhX3FPg5e"
                ),
                HomeCouponItem(
                    id = 5L,
                    brand = "CU 편의점",
                    name = "모바일 금액권 5,000원",
                    expireText = "2026.06.01 까지",
                    badge = "사용가능",
                    badgeAtStart = true,
                    badgeColor = Color(0xFFE5E7EB),
                    badgeTextColor = Color(0xFF6B7280),
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBMqRc4tprwPTT8YrbjMSQ8pfDkcR4rST4Z9E-N3t2NWT9UijMU1ApVUIPce2HsYXf3jP_Ufc5xygSR76BW0WGSalsG6eOFyTZO_qhy-s4DI3Ik-OMeubg02sRpfkPUjRIYOGKFxxqcVWeLslcvLDWvTD1olHfxw6t6l4Bb40AdJRFxrQSMxWQB1s7-OCuhG9teZJZ8xgagmuhwL3GQyfQHF1B3g3SM4Np-8ykTLzoeMwBTdJTclij3mUdJBM5IuSntyKmuxeHnfyXN"
                ),
                HomeCouponItem(
                    id = 6L,
                    brand = "메가커피",
                    name = "아이스 아메리카노",
                    expireText = "2025.11.10 사용함",
                    badge = "사용완료",
                    badgeAtStart = true,
                    badgeColor = Color(0xFFFEE2E2),
                    badgeTextColor = Color(0xFFDC2626),
                    isUsed = true,
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuADuWGzcRoRFtLGdK-yIET1kPmCZY_1xLt2PtCB9kV1YAuq4B5LqHAplR3ngsx_S3-ZR3YV4JClC1Y_xOvelAlbYLC08RMMIhYxCSsz86XpPoKEsAogOtM8ER40tcmQ1bWIGJLm2niXxUN5zP6v5kW_Yhn3hcUl2qh-tebDi_p6sNfHEHtOurEvDz_XeL5i-N6PgnTkR6SFtV1EGzBmw5x1myq2xSVz_QhlEzzo-pI66B1vN9YDXmBd_0dSmV2jUUT0AwL3d1KAahNV"
                )
            )
        )
    }
}

/**
 * Today Focus 영역 모델.
 */
data class HomeFocusItem(
    val brand: String,
    val title: String,
    val dday: String,
    val expireText: String,
    val imageUrl: String
)

/**
 * 쿠폰 카드 UI 모델.
 */
data class HomeCouponItem(
    val id: Long,
    val brand: String,
    val name: String,
    val expireText: String,
    val imageUrl: String,
    val badge: String? = null,
    val badgeColor: Color = HomePrimary,
    val badgeTextColor: Color = Color.White,
    val badgeAtStart: Boolean = false,
    val isUsed: Boolean = false
)

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 390, heightDp = 844)
@Composable
private fun HomeDashboardScreenPreview() {
    GifticonAlarmTheme {
        HomeDashboardScreen()
    }
}
