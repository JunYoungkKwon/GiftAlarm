package com.example.gifticonalarm.ui.feature.shared.voucherdetailscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.gifticonalarm.R
import com.example.gifticonalarm.ui.feature.shared.components.VoucherDetailMoreMenu
import com.example.gifticonalarm.ui.theme.GifticonBlack
import com.example.gifticonalarm.ui.theme.GifticonBrandPrimary
import com.example.gifticonalarm.ui.theme.GifticonSurfacePill
import com.example.gifticonalarm.ui.theme.GifticonSurfaceUsed
import com.example.gifticonalarm.ui.theme.GifticonTextMuted
import com.example.gifticonalarm.ui.theme.GifticonTextPrimary
import com.example.gifticonalarm.ui.theme.GifticonWhite

private val ScreenBackground = GifticonWhite
private val PrimaryText = GifticonTextPrimary
private val Accent = GifticonBrandPrimary

/**
 * 교환권 상태.
 */
enum class ProductVoucherStatus(val label: String) {
    USABLE("사용 가능"),
    USED("사용 완료"),
    EXPIRED("만료")
}

/**
 * 교환권 상세 UI 모델.
 */
data class ProductVoucherDetailUiModel(
    val couponId: String,
    val brand: String,
    val productName: String,
    val status: ProductVoucherStatus,
    val expireDateText: String,
    val expireBadgeText: String,
    val barcodeNumber: String,
    val exchangePlaceText: String,
    val memoText: String,
    val productImageUrl: String
) {
    companion object {
        fun placeholder(couponId: String): ProductVoucherDetailUiModel = ProductVoucherDetailUiModel(
            couponId = couponId,
            brand = "스타벅스",
            productName = "아이스 아메리카노 T",
            status = ProductVoucherStatus.USABLE,
            expireDateText = "2024. 12. 31 까지",
            expireBadgeText = "D-45",
            barcodeNumber = "1234 5678 9012",
            exchangePlaceText = "스타벅스 전국 매장 (일부 특수 매장 제외)",
            memoText = "생일 축하해! 시원한 아메리카노 한 잔 하고 힘내렴 :)",
            productImageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBiurr-zc8f8sX4Auu40fAUYTJf_d0YFt4JKqX3FLo1lUFrAwvr4DubDpMyK_FwDpMDKaUpkHz2pW_qjYEQBZp9OKk0JIjUgsax6tFqJXIS0KWSXOhWK4fvvj07CMVD85UFEPC0H4qbQQlYUeK3L34HeVPomYC2AUHZicvik-9iCOfd0T2nUAMngz51oIYTK1HNUK6_Wb3Jsu-y8IaPE9N18vINXmV-zlySx6AVerBvwTCJTHxN1yhp5c4olltdW3z3IeLro19188ej"
        )
    }
}

/**
 * 교환권 상세 화면.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductVoucherDetailScreen(
    couponId: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    uiModel: ProductVoucherDetailUiModel = ProductVoucherDetailUiModel.placeholder(couponId),
    onShowBarcodeClick: () -> Unit = {},
    onCopyBarcodeClick: (String) -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    val isMoreMenuExpanded = rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        containerColor = ScreenBackground,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ScreenBackground),
                title = {
                    Text(
                        text = "교환권 상세 정보",
                        style = MaterialTheme.typography.titleMedium,
                        color = PrimaryText,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text(
                            text = "‹",
                            color = PrimaryText,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { isMoreMenuExpanded.value = true }) {
                        Icon(
                            imageVector = Icons.Outlined.MoreHoriz,
                            contentDescription = "더보기",
                            tint = PrimaryText
                        )
                    }
                    VoucherDetailMoreMenu(
                        expanded = isMoreMenuExpanded.value,
                        onDismissRequest = { isMoreMenuExpanded.value = false },
                        onEditClick = onEditClick,
                        onDeleteClick = onDeleteClick
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(ScreenBackground)
        ) {
            ProductVoucherTopSection(
                uiModel = uiModel,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 18.dp)
            )

            VoucherDetailBottomSection(
                uiModel = VoucherDetailBottomSectionUiModel(
                    barcodeNumber = uiModel.barcodeNumber,
                    expireDateText = uiModel.expireDateText,
                    expireBadgeText = uiModel.expireBadgeText,
                    exchangePlaceText = uiModel.exchangePlaceText,
                    memoText = uiModel.memoText
                ),
                onShowBarcodeClick = onShowBarcodeClick,
                onCopyBarcodeClick = onCopyBarcodeClick,
                actionButtonText = if (uiModel.status == ProductVoucherStatus.USED) {
                    "사용취소"
                } else {
                    "사용하기"
                },
                actionButtonContainerColor = if (uiModel.status == ProductVoucherStatus.USED) {
                    GifticonWhite
                } else {
                    Accent
                },
                actionButtonContentColor = if (uiModel.status == ProductVoucherStatus.USED) {
                    GifticonBlack
                } else {
                    GifticonWhite
                },
                actionButtonBorderColor = if (uiModel.status == ProductVoucherStatus.USED) {
                    GifticonTextPrimary
                } else {
                    null
                },
                showActionButtonIcon = false,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ProductVoucherTopSection(
    uiModel: ProductVoucherDetailUiModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(192.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(GifticonSurfaceUsed),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = uiModel.productImageUrl,
                contentDescription = "교환권 상품 이미지",
                modifier = Modifier.fillMaxSize(),
                error = painterResource(id = R.drawable.default_coupon_image),
                fallback = painterResource(id = R.drawable.default_coupon_image)
            )
        }

        Text(
            text = uiModel.brand,
            color = GifticonTextMuted,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = uiModel.productName,
            color = Accent,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Surface(
            color = GifticonSurfacePill,
            shape = RoundedCornerShape(999.dp)
        ) {
            Text(
                text = uiModel.status.label,
                color = Accent,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(2.dp))
    }
}
