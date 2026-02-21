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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.gifticonalarm.ui.feature.shared.components.VoucherDetailTopBar
import com.example.gifticonalarm.ui.feature.shared.fixture.VoucherUiFixtureProvider
import com.example.gifticonalarm.ui.feature.shared.model.VoucherStatus
import com.example.gifticonalarm.ui.feature.shared.text.VoucherText
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

private data class ProductActionButtonStyle(
    val text: String,
    val containerColor: androidx.compose.ui.graphics.Color,
    val contentColor: androidx.compose.ui.graphics.Color,
    val borderColor: androidx.compose.ui.graphics.Color?
)

/**
 * 교환권 상세 UI 모델.
 */
data class ProductVoucherDetailUiModel(
    val couponId: String,
    val brand: String,
    val productName: String,
    val status: VoucherStatus,
    val expireDateText: String,
    val expireBadgeText: String,
    val barcodeNumber: String,
    val exchangePlaceText: String,
    val memoText: String,
    val productImageUrl: String
) {
    companion object {
        fun placeholder(couponId: String): ProductVoucherDetailUiModel =
            VoucherUiFixtureProvider.productVoucher(couponId)
    }
}

/**
 * 교환권 상세 화면.
 */
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
    val actionButtonStyle = resolveProductActionButtonStyle(uiModel.status)

    Scaffold(
        modifier = modifier,
        containerColor = ScreenBackground,
        topBar = {
            VoucherDetailTopBar(
                title = VoucherText.PRODUCT_DETAIL_TITLE,
                containerColor = ScreenBackground,
                contentColor = PrimaryText,
                menuExpanded = isMoreMenuExpanded.value,
                onBackClick = onNavigateBack,
                onExpandMenuClick = { isMoreMenuExpanded.value = true },
                onDismissMenu = { isMoreMenuExpanded.value = false },
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
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
                actionButtonText = actionButtonStyle.text,
                actionButtonContainerColor = actionButtonStyle.containerColor,
                actionButtonContentColor = actionButtonStyle.contentColor,
                actionButtonBorderColor = actionButtonStyle.borderColor,
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
                contentDescription = VoucherText.PRODUCT_IMAGE_DESCRIPTION,
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

private fun resolveProductActionButtonStyle(status: VoucherStatus): ProductActionButtonStyle {
    return if (status == VoucherStatus.USED) {
        ProductActionButtonStyle(
            text = VoucherText.ACTION_CANCEL_USE,
            containerColor = GifticonWhite,
            contentColor = GifticonBlack,
            borderColor = GifticonTextPrimary
        )
    } else {
        ProductActionButtonStyle(
            text = VoucherText.ACTION_USE,
            containerColor = Accent,
            contentColor = GifticonWhite,
            borderColor = null
        )
    }
}
