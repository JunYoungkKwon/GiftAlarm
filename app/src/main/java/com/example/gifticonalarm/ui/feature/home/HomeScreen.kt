package com.example.gifticonalarm.ui.feature.home

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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.gifticonalarm.R
import com.example.gifticonalarm.ui.feature.home.model.HomeBadgeType
import com.example.gifticonalarm.ui.feature.home.model.HomeCouponItem
import com.example.gifticonalarm.ui.feature.home.model.HomeFocusItem
import com.example.gifticonalarm.ui.feature.home.model.HomeSortType
import com.example.gifticonalarm.ui.feature.home.model.HomeUiState
import com.example.gifticonalarm.ui.theme.GifticonBrandPrimary
import com.example.gifticonalarm.ui.theme.GifticonDanger
import com.example.gifticonalarm.ui.theme.GifticonDangerBackground
import com.example.gifticonalarm.ui.theme.GifticonDangerStrong
import com.example.gifticonalarm.ui.theme.GifticonOverlayBlack20
import com.example.gifticonalarm.ui.theme.GifticonOverlayBlack80
import com.example.gifticonalarm.ui.theme.GifticonOverlayBlack12
import com.example.gifticonalarm.ui.theme.GifticonSurface
import com.example.gifticonalarm.ui.theme.GifticonTextPrimaryAlt
import com.example.gifticonalarm.ui.theme.GifticonTextSecondary
import com.example.gifticonalarm.ui.theme.GifticonTextTertiary
import com.example.gifticonalarm.ui.theme.GifticonTextUsed
import com.example.gifticonalarm.ui.theme.GifticonWarning
import com.example.gifticonalarm.ui.theme.GifticonWhite

private val HomeBackground = GifticonWhite
private val HomePrimary = GifticonBrandPrimary
private val HomeSurface = GifticonSurface
private val HomeTextPrimary = GifticonTextPrimaryAlt
private val HomeTextSecondary = GifticonTextSecondary

/**
 * Stitch 홈 대시보드 디자인을 반영한 UI 전용 홈 화면.
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeUiState = HomeUiState(
        focus = null,
        coupons = emptyList()
    ),
    onNotificationClick: () -> Unit = {},
    onPrimaryActionClick: () -> Unit = {},
    onSortSelected: (HomeSortType) -> Unit = {},
    onFocusClick: (Long) -> Unit = {},
    onCouponClick: (Long) -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        containerColor = HomeBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onPrimaryActionClick,
                containerColor = HomePrimary,
                contentColor = GifticonWhite,
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
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 28.dp)
        ) {
            item {
                HomeHeader(
                    onNotificationClick = onNotificationClick,
                    hasUnreadNotifications = state.hasUnreadNotifications
                )
            }
            state.focus?.let { focus ->
                item {
                    FocusSection(
                        focus = focus,
                        onFocusClick = onFocusClick
                    )
                }
            }
            if (state.coupons.isEmpty()) {
                item {
                    EmptyCouponSection()
                }
            } else {
                item {
                    CouponSection(
                        selectedSort = state.selectedSort,
                        coupons = state.coupons,
                        onSortSelected = onSortSelected,
                        onCouponClick = onCouponClick
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeHeader(
    onNotificationClick: () -> Unit,
    hasUnreadNotifications: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .height(36.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "기프트노트",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = HomePrimary
        )

        BadgedBox(
            badge = {
                if (hasUnreadNotifications) {
                    Badge(
                        containerColor = GifticonDanger,
                        modifier = Modifier.size(8.dp)
                    )
                }
            }
        ) {
            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.NotificationsNone,
                    contentDescription = "알림",
                    tint = HomeTextSecondary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun FocusSection(
    focus: HomeFocusItem,
    onFocusClick: (Long) -> Unit
) {
    val focusBadgeStyle = resolveHomeFocusBadgeStyle(focus.dday)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "오늘의 포커스",
            style = MaterialTheme.typography.titleMedium,
            color = HomeTextPrimary,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = GifticonWhite),
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
                    modifier = Modifier.fillMaxSize(),
                    error = painterResource(id = R.drawable.default_coupon_image),
                    fallback = painterResource(id = R.drawable.default_coupon_image)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    GifticonOverlayBlack20,
                                    GifticonOverlayBlack80
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
                        .background(focusBadgeStyle.containerColor)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = focusBadgeStyle.textColor,
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
                        color = GifticonWhite.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = focus.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = GifticonWhite,
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
                            color = GifticonWhite.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "사용하기",
                            modifier = Modifier
                                .clickable { onFocusClick(focus.id) }
                                .clip(RoundedCornerShape(12.dp))
                                .background(GifticonWhite.copy(alpha = 0.2f))
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = GifticonWhite,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

private data class FocusBadgeStyle(
    val containerColor: Color,
    val textColor: Color
)

private data class CouponBadgeColors(
    val containerColor: Color,
    val textColor: Color
)

private fun resolveHomeFocusBadgeStyle(ddayText: String): FocusBadgeStyle {
    val normalized = ddayText.trim()
    val dday = normalized.removePrefix("D-").toLongOrNull()

    return when {
        dday != null && dday in 1L..7L -> FocusBadgeStyle(
            containerColor = GifticonDanger,
            textColor = GifticonWhite
        )
        dday != null && dday in 8L..15L -> FocusBadgeStyle(
            containerColor = GifticonBrandPrimary,
            textColor = GifticonWhite
        )
        else -> FocusBadgeStyle(
            containerColor = GifticonWarning,
            textColor = GifticonWhite
        )
    }
}

private fun resolveHomeCouponBadgeColors(type: HomeBadgeType): CouponBadgeColors {
    return when (type) {
        HomeBadgeType.USED -> CouponBadgeColors(
            containerColor = GifticonWhite,
            textColor = GifticonTextUsed
        )

        HomeBadgeType.EXPIRED -> CouponBadgeColors(
            containerColor = GifticonDangerBackground,
            textColor = GifticonDangerStrong
        )

        HomeBadgeType.URGENT -> CouponBadgeColors(
            containerColor = GifticonDanger,
            textColor = GifticonWhite
        )

        HomeBadgeType.NORMAL -> CouponBadgeColors(
            containerColor = GifticonBrandPrimary,
            textColor = GifticonWhite
        )

        HomeBadgeType.SAFE -> CouponBadgeColors(
            containerColor = GifticonWarning,
            textColor = GifticonWhite
        )
    }
}

@Composable
private fun CouponSection(
    selectedSort: HomeSortType,
    coupons: List<HomeCouponItem>,
    onSortSelected: (HomeSortType) -> Unit,
    onCouponClick: (Long) -> Unit
) {
    var sortMenuExpanded by remember { mutableStateOf(false) }

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
            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = { sortMenuExpanded = true })
                        .padding(horizontal = 2.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedSort.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = GifticonTextTertiary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = Icons.Outlined.ExpandMore,
                        contentDescription = null,
                        tint = GifticonTextTertiary,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(top = 1.dp)
                    )
                }

                DropdownMenu(
                    expanded = sortMenuExpanded,
                    onDismissRequest = { sortMenuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(HomeSortType.LATEST.label) },
                        onClick = {
                            onSortSelected(HomeSortType.LATEST)
                            sortMenuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(HomeSortType.EXPIRY_SOON.label) },
                        onClick = {
                            onSortSelected(HomeSortType.EXPIRY_SOON)
                            sortMenuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(HomeSortType.EXPIRY_LATE.label) },
                        onClick = {
                            onSortSelected(HomeSortType.EXPIRY_LATE)
                            sortMenuExpanded = false
                        }
                    )

                }
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
    val badgeColors = resolveHomeCouponBadgeColors(item.badgeType)

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
                    modifier = Modifier.fillMaxSize(),
                    error = painterResource(id = R.drawable.default_coupon_image),
                    fallback = painterResource(id = R.drawable.default_coupon_image)
                )

                if (item.badge != null) {
                    Text(
                        text = item.badge,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(badgeColors.containerColor)
                            .padding(horizontal = 6.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = badgeColors.textColor,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (item.isUsed) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(GifticonOverlayBlack12)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
            ) {
                Text(
                    text = item.brand,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (item.isUsed) GifticonTextTertiary else HomePrimary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (item.isUsed) GifticonTextTertiary else HomeTextPrimary,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (item.isUsed) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = item.expireText,
                    style = MaterialTheme.typography.labelSmall,
                    color = GifticonTextTertiary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyCouponSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "등록된 쿠폰이 없어요.",
            style = MaterialTheme.typography.bodyMedium,
            color = HomeTextSecondary
        )
    }
}
