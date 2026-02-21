package com.example.gifticonalarm.ui.feature.shared.cashvoucherdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Fullscreen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.gifticonalarm.ui.feature.shared.text.CommonText
import com.example.gifticonalarm.ui.feature.shared.text.VoucherText
import com.example.gifticonalarm.ui.feature.shared.voucherdetailscreen.VoucherDetailBottomSection
import com.example.gifticonalarm.ui.feature.shared.voucherdetailscreen.VoucherDetailBottomSectionUiModel
import com.example.gifticonalarm.ui.theme.GifticonBrandPrimary
import com.example.gifticonalarm.ui.theme.GifticonSurfaceSoft
import com.example.gifticonalarm.ui.theme.GifticonTextPrimary
import com.example.gifticonalarm.ui.theme.GifticonWhite

private val CouponAccent = GifticonBrandPrimary
private val CouponBackground = GifticonWhite
private val CouponTextPrimary = GifticonTextPrimary

/**
 * 금액권 상세 화면에 표시할 UI 모델.
 */
data class CashVoucherDetailUiModel(
    val couponId: String,
    val brand: String,
    val title: String,
    val status: VoucherStatus,
    val remainAmountText: String,
    val usageHistoryText: String,
    val expireDateText: String,
    val expireBadgeText: String,
    val barcodeNumber: String,
    val exchangePlaceText: String,
    val memo: String,
    val brandLogoUrl: String
) {
    companion object {
        fun placeholder(couponId: String): CashVoucherDetailUiModel =
            VoucherUiFixtureProvider.cashVoucher(couponId)
    }
}

/**
 * 홈 대시보드에서 진입하는 금액권 상세 화면.
 */
@Composable
fun CashVoucherDetailScreen(
    couponId: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    uiModel: CashVoucherDetailUiModel = CashVoucherDetailUiModel.placeholder(couponId),
    onAddUsageClick: () -> Unit = {},
    onShowBarcodeClick: () -> Unit = {},
    onCopyBarcodeClick: (String) -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    val isMoreMenuExpanded = rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        containerColor = CouponBackground,
        topBar = {
            VoucherDetailTopBar(
                title = VoucherText.CASH_DETAIL_TITLE,
                containerColor = CouponBackground,
                contentColor = CouponTextPrimary,
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
                .background(CouponBackground)
        ) {
            CashVoucherTopSection(
                uiModel = uiModel,
                onAddUsageClick = onAddUsageClick,
                onShowBarcodeClick = onShowBarcodeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 18.dp)
            )

            CashVoucherBottomSection(
                uiModel = uiModel,
                onShowBarcodeClick = onShowBarcodeClick,
                onCopyBarcodeClick = onCopyBarcodeClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CashVoucherTopSection(
    uiModel: CashVoucherDetailUiModel,
    onAddUsageClick: () -> Unit,
    onShowBarcodeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BalanceCard(uiModel = uiModel)
        ActionButtons(
            onAddUsageClick = onAddUsageClick,
            onShowBarcodeClick = onShowBarcodeClick
        )
    }
}

@Composable
private fun CashVoucherBottomSection(
    uiModel: CashVoucherDetailUiModel,
    onShowBarcodeClick: () -> Unit,
    onCopyBarcodeClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    VoucherDetailBottomSection(
        uiModel = VoucherDetailBottomSectionUiModel(
            barcodeNumber = uiModel.barcodeNumber,
            usageHistoryText = uiModel.usageHistoryText,
            expireDateText = uiModel.expireDateText,
            expireBadgeText = uiModel.expireBadgeText,
            exchangePlaceText = uiModel.exchangePlaceText,
            memoText = uiModel.memo
        ),
        onShowBarcodeClick = onShowBarcodeClick,
        onCopyBarcodeClick = onCopyBarcodeClick,
        showActionButton = false,
        modifier = modifier
    )
}

@Composable
private fun BalanceCard(uiModel: CashVoucherDetailUiModel) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CouponAccent),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(28.dp),
                        shape = CircleShape,
                        color = GifticonWhite
                    ) {
                        AsyncImage(
                            model = uiModel.brandLogoUrl,
                            contentDescription = VoucherText.BRAND_LOGO_DESCRIPTION,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp),
                            error = painterResource(id = R.drawable.default_coupon_image),
                            fallback = painterResource(id = R.drawable.default_coupon_image)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = uiModel.brand,
                            color = GifticonWhite.copy(alpha = 0.85f),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = uiModel.title,
                            color = GifticonWhite,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Surface(
                    color = GifticonWhite.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = uiModel.status.label,
                        color = GifticonWhite,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = VoucherText.LABEL_REMAIN_BALANCE,
                color = GifticonWhite.copy(alpha = 0.8f),
                style = MaterialTheme.typography.labelSmall
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = uiModel.remainAmountText,
                    color = GifticonWhite,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = CommonText.UNIT_WON,
                    color = GifticonWhite,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(GifticonWhite.copy(alpha = 0.08f))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = VoucherText.LABEL_EXPIRE_DATE,
                    color = GifticonWhite.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = uiModel.expireDateText,
                    color = GifticonWhite,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ActionButtons(
    onAddUsageClick: () -> Unit,
    onShowBarcodeClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DetailActionButton(
            modifier = Modifier.weight(1f),
            onClick = onAddUsageClick,
            icon = {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            },
            label = VoucherText.ACTION_ADD_USAGE,
            containerColor = CouponAccent,
            contentColor = GifticonWhite,
            labelWeight = FontWeight.Bold
        )

        DetailActionButton(
            modifier = Modifier.weight(1f),
            onClick = onShowBarcodeClick,
            icon = {
                Icon(imageVector = Icons.Outlined.Fullscreen, contentDescription = null, modifier = Modifier.size(16.dp))
            },
            label = VoucherText.ACTION_SHOW_LARGE_BARCODE,
            containerColor = GifticonSurfaceSoft,
            contentColor = CouponTextPrimary,
            labelWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun DetailActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: String,
    containerColor: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color,
    labelWeight: FontWeight
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        icon()
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = labelWeight
        )
    }
}
