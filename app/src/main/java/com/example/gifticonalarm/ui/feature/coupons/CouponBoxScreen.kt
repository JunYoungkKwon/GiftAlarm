package com.example.gifticonalarm.ui.feature.coupons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.gifticonalarm.R
import com.example.gifticonalarm.ui.feature.coupons.model.CouponCategoryType
import com.example.gifticonalarm.ui.feature.coupons.model.CouponFilterType
import com.example.gifticonalarm.ui.feature.coupons.model.CouponStatus
import com.example.gifticonalarm.ui.feature.coupons.model.CouponUiModel
import com.example.gifticonalarm.ui.feature.shared.components.SelectableDropdownMenuItem
import com.example.gifticonalarm.ui.feature.shared.text.CouponText
import com.example.gifticonalarm.ui.theme.GifticonBorder
import com.example.gifticonalarm.ui.theme.GifticonBorderSoft
import com.example.gifticonalarm.ui.theme.GifticonBrandPrimary
import com.example.gifticonalarm.ui.theme.GifticonDanger
import com.example.gifticonalarm.ui.theme.GifticonDangerBackground
import com.example.gifticonalarm.ui.theme.GifticonDangerStrong
import com.example.gifticonalarm.ui.theme.GifticonInfoBackground
import com.example.gifticonalarm.ui.theme.GifticonOverlayBlack40
import com.example.gifticonalarm.ui.theme.GifticonSurface
import com.example.gifticonalarm.ui.theme.GifticonSurfaceSoft
import com.example.gifticonalarm.ui.theme.GifticonSurfaceUsed
import com.example.gifticonalarm.ui.theme.GifticonTextMuted
import com.example.gifticonalarm.ui.theme.GifticonTextPrimary
import com.example.gifticonalarm.ui.theme.GifticonTextSlate
import com.example.gifticonalarm.ui.theme.GifticonTextSlateStrong
import com.example.gifticonalarm.ui.theme.GifticonWarning
import com.example.gifticonalarm.ui.theme.GifticonWarningBackground
import com.example.gifticonalarm.ui.theme.GifticonWhite

private val CouponAccent = GifticonBrandPrimary
private val CouponBackground = GifticonWhite
private val CouponListBackground = GifticonSurface
private val CouponCardBorder = GifticonBorder
private val CouponMutedText = GifticonTextMuted
private val CouponMenuSurface = Color(0xFFFAFBFD)
private val CouponMenuBorder = Color(0xFFE7ECF3)
private val CouponMenuSelected = Color(0xFFEDEEFE)

private data class CouponFilterOption(
    val label: String,
    val type: CouponFilterType
)

private val CouponFilterOptions = listOf(
    CouponFilterOption(label = CouponText.FILTER_ALL, type = CouponFilterType.ALL),
    CouponFilterOption(label = CouponText.FILTER_AVAILABLE, type = CouponFilterType.AVAILABLE),
    CouponFilterOption(label = CouponText.FILTER_USED, type = CouponFilterType.USED),
    CouponFilterOption(label = CouponText.FILTER_EXPIRED, type = CouponFilterType.EXPIRED)
)

/**
 * 쿠폰함 화면 UI.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponBoxScreen(
    modifier: Modifier = Modifier,
    coupons: List<CouponUiModel> = emptyList(),
    searchQuery: String = "",
    selectedFilter: CouponFilterType = CouponFilterType.ALL,
    selectedCategory: CouponCategoryType = CouponCategoryType.ALL,
    onSearchQueryChange: (String) -> Unit = {},
    onFilterSelected: (CouponFilterType) -> Unit = {},
    onCategorySelected: (CouponCategoryType) -> Unit = {},
    onAddClick: () -> Unit = {},
    onCouponClick: (Long) -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        color = CouponBackground
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CouponBoxHeader(
                onAddClick = onAddClick,
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                selectedFilter = selectedFilter,
                onFilterSelected = onFilterSelected,
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected,
                totalCount = coupons.size
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CouponListBackground),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(coupons, key = { it.id }) { coupon ->
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
    selectedFilter: CouponFilterType,
    onFilterSelected: (CouponFilterType) -> Unit,
    selectedCategory: CouponCategoryType,
    onCategorySelected: (CouponCategoryType) -> Unit,
    totalCount: Int
) {
    var categoryMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CouponBackground)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = CouponText.TITLE_COUPON_LIST,
                style = MaterialTheme.typography.titleLarge,
                color = CouponAccent,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = onAddClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = CouponText.DESCRIPTION_ADD_COUPON,
                    tint = CouponAccent,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(top = 10.dp)
                .padding(bottom = 10.dp),
            textStyle = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp
            ),
            placeholder = {
                Text(
                    text = CouponText.PLACEHOLDER_SEARCH,
                    color = CouponMutedText,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp
                    )
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
                focusedContainerColor = GifticonSurfaceSoft,
                unfocusedContainerColor = GifticonSurfaceSoft,
                focusedTextColor = GifticonTextPrimary,
                unfocusedTextColor = GifticonTextPrimary,
                focusedPlaceholderColor = CouponMutedText,
                unfocusedPlaceholderColor = CouponMutedText,
                cursorColor = CouponAccent,
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
            CouponFilterOptions.forEach { option ->
                CouponFilterChip(
                    text = option.label,
                    selected = selectedFilter == option.type,
                    onClick = { onFilterSelected(option.type) }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Row(
                    modifier = Modifier.clickable { categoryMenuExpanded = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedCategory.label,
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

                DropdownMenu(
                    expanded = categoryMenuExpanded,
                    onDismissRequest = { categoryMenuExpanded = false },
                    shape = RoundedCornerShape(14.dp),
                    containerColor = CouponMenuSurface,
                    tonalElevation = 0.dp,
                    shadowElevation = 8.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CouponMenuBorder)
                ) {
                    SelectableDropdownMenuItem(
                        label = CouponCategoryType.ALL.label,
                        selected = selectedCategory == CouponCategoryType.ALL,
                        selectedBackgroundColor = CouponMenuSelected,
                        textColor = GifticonTextPrimary,
                        checkTintColor = CouponAccent,
                        onClick = {
                            onCategorySelected(CouponCategoryType.ALL)
                            categoryMenuExpanded = false
                        }
                    )
                    SelectableDropdownMenuItem(
                        label = CouponCategoryType.EXCHANGE.label,
                        selected = selectedCategory == CouponCategoryType.EXCHANGE,
                        selectedBackgroundColor = CouponMenuSelected,
                        textColor = GifticonTextPrimary,
                        checkTintColor = CouponAccent,
                        onClick = {
                            onCategorySelected(CouponCategoryType.EXCHANGE)
                            categoryMenuExpanded = false
                        }
                    )
                    SelectableDropdownMenuItem(
                        label = CouponCategoryType.AMOUNT.label,
                        selected = selectedCategory == CouponCategoryType.AMOUNT,
                        selectedBackgroundColor = CouponMenuSelected,
                        textColor = GifticonTextPrimary,
                        checkTintColor = CouponAccent,
                        onClick = {
                            onCategorySelected(CouponCategoryType.AMOUNT)
                            categoryMenuExpanded = false
                        }
                    )

                }
            }
            Text(
                text = CouponText.totalCountLabel(totalCount),
                style = MaterialTheme.typography.labelSmall,
                color = CouponMutedText
            )
        }
    }
}

@Composable
private fun CouponFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    AssistChip(
        onClick = onClick,
        label = {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (selected) CouponAccent else GifticonBorderSoft,
            labelColor = if (selected) GifticonWhite else GifticonTextSlate
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
    val badgeStyle = badgeStyleByCoupon(coupon)
    val rowStyle = rowStyleByCoupon(coupon)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(rowStyle.backgroundColor)
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
                .background(GifticonSurfaceSoft),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = coupon.imageUrl,
                contentDescription = coupon.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.default_coupon_image),
                fallback = painterResource(id = R.drawable.default_coupon_image)
            )
            if (coupon.status == CouponStatus.USED) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(GifticonOverlayBlack40),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.used_coupon_overlay),
                        contentDescription = CouponText.DESCRIPTION_USED_COUPON,
                        modifier = Modifier.size(32.dp)
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
                color = if (rowStyle.inactive) GifticonTextSlateStrong else CouponAccent,
                fontWeight = FontWeight.Bold,
                textDecoration = if (rowStyle.inactive) TextDecoration.LineThrough else null
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = coupon.name,
                style = MaterialTheme.typography.titleSmall,
                color = if (rowStyle.inactive) GifticonTextSlateStrong else GifticonTextPrimary,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                textDecoration = if (rowStyle.inactive) TextDecoration.LineThrough else null
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
                    color = if (rowStyle.inactive) GifticonTextSlateStrong else rowStyle.expiryColor,
                    fontWeight = if (coupon.status == CouponStatus.EXPIRED) FontWeight.Bold else FontWeight.Medium,
                    textDecoration = if (rowStyle.inactive) TextDecoration.LineThrough else null
                )
                Box(
                    modifier = Modifier
                        .background(badgeStyle.containerColor, RoundedCornerShape(6.dp))
                        .border(1.dp, badgeStyle.borderColor, RoundedCornerShape(6.dp))
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = coupon.statusBadge,
                        style = MaterialTheme.typography.labelSmall,
                        color = badgeStyle.textColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private data class CouponBadgeStyle(
    val containerColor: Color,
    val borderColor: Color,
    val textColor: Color
)

private data class CouponRowStyle(
    val inactive: Boolean,
    val expiryColor: Color,
    val backgroundColor: Color
)

private fun rowStyleByCoupon(coupon: CouponUiModel): CouponRowStyle {
    val inactive = coupon.status == CouponStatus.USED || coupon.status == CouponStatus.EXPIRED
    val expiryColor = when (coupon.status) {
        CouponStatus.EXPIRED -> GifticonDanger
        CouponStatus.USED -> GifticonTextSlateStrong
        CouponStatus.AVAILABLE -> {
            if ((coupon.dday ?: Long.MAX_VALUE) in 0L..7L) GifticonDanger else GifticonTextSlate
        }
    }
    return CouponRowStyle(
        inactive = inactive,
        expiryColor = expiryColor,
        backgroundColor = if (coupon.status == CouponStatus.USED) GifticonSurfaceUsed else GifticonWhite
    )
}

private fun badgeStyleByCoupon(coupon: CouponUiModel): CouponBadgeStyle {
    return when (coupon.status) {
        CouponStatus.USED -> CouponBadgeStyle(
            containerColor = GifticonBorderSoft,
            borderColor = GifticonBorderSoft,
            textColor = GifticonTextSlate
        )
        CouponStatus.EXPIRED -> CouponBadgeStyle(
            containerColor = GifticonBorderSoft,
            borderColor = GifticonBorderSoft,
            textColor = GifticonDanger
        )
        CouponStatus.AVAILABLE -> {
            val dday = coupon.dday ?: Long.MAX_VALUE
            when {
                dday in 0L..7L -> CouponBadgeStyle(
                    containerColor = GifticonDangerBackground,
                    borderColor = GifticonDangerBackground,
                    textColor = GifticonDangerStrong
                )
                dday in 16L..Long.MAX_VALUE -> CouponBadgeStyle(
                    containerColor = GifticonWarningBackground,
                    borderColor = GifticonWarningBackground,
                    textColor = GifticonWarning
                )
                else -> CouponBadgeStyle(
                    containerColor = GifticonInfoBackground,
                    borderColor = GifticonInfoBackground,
                    textColor = CouponAccent
                )
            }
        }
    }
}
