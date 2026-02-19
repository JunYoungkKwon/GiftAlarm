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
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.gifticonalarm.ui.feature.shared.components.VoucherDetailMoreMenu
import com.example.gifticonalarm.ui.feature.shared.voucherdetailscreen.VoucherDetailBottomSection
import com.example.gifticonalarm.ui.feature.shared.voucherdetailscreen.VoucherDetailBottomSectionUiModel
import com.example.gifticonalarm.ui.theme.GifticonAlarmTheme

private val CouponAccent = Color(0xFF191970)
private val CouponBackground = Color(0xFFFFFFFF)
private val CouponTextPrimary = Color(0xFF0F172A)

/**
 * 금액권 상세 화면에 표시할 UI 모델.
 */
data class CashVoucherDetailUiModel(
    val couponId: String,
    val brand: String,
    val title: String,
    val status: String,
    val remainAmountText: String,
    val expireDateText: String,
    val expireBadgeText: String,
    val barcodeNumber: String,
    val exchangePlaceText: String,
    val memo: String,
    val brandLogoUrl: String
) {
    companion object {
        fun placeholder(couponId: String): CashVoucherDetailUiModel = CashVoucherDetailUiModel(
            couponId = couponId,
            brand = "스타벅스",
            title = "e카드 5만원 교환권",
            status = "사용 가능",
            remainAmountText = "32,500",
            expireDateText = "2024. 12. 31 까지",
            expireBadgeText = "D-45",
            barcodeNumber = "1234 5678 9012",
            exchangePlaceText = "스타벅스 전국 매장 (일부 특수 매장 제외)",
            memo = "친구 생일 선물로 받은 기프티콘. 유효기간 연장 1회 완료함.",
            brandLogoUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAXVWYahb8gPbQN0cgsRi5zK9FKYl2Hy4pcMW13H2rafEVzYzFh6EFlFpNyyKaJfmw5hXjnAaGKgOTUme6FFpdjLyt2HfAdvf4AuYNExmaFc95S4Kdzj4UbJuTd-7FnOl4msBfNkyakzIXVuoHQTroSR8iUFfAJFsapq_JyzJqORe8kTGMwr7EjPtINS1mZHKAfAgpxM8R9W5Yl9SjXYIYJVc34QmoWsCCIRG13WJpgzPqVhAhS0wUAK6QV_fZh21BMlltykcQNeYKG"
        )
    }
}

/**
 * 홈 대시보드에서 진입하는 금액권 상세 화면.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashVoucherDetailScreen(
    couponId: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    uiModel: CashVoucherDetailUiModel = CashVoucherDetailUiModel.placeholder(couponId),
    onAddUsageClick: () -> Unit = {},
    onImageClick: () -> Unit = {},
    onShowBarcodeClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    val isMoreMenuExpanded = rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        containerColor = CouponBackground,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CouponBackground),
                title = {
                    Text(
                        text = "금액권 상세 정보",
                        style = MaterialTheme.typography.titleMedium,
                        color = CouponTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text(
                            text = "‹",
                            color = CouponTextPrimary,
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
                            tint = CouponTextPrimary
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
                .background(CouponBackground)
        ) {
            CashVoucherTopSection(
                uiModel = uiModel,
                onAddUsageClick = onAddUsageClick,
                onImageClick = onImageClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )

            CashVoucherBottomSection(
                uiModel = uiModel,
                onShowBarcodeClick = onShowBarcodeClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CashVoucherTopSection(
    uiModel: CashVoucherDetailUiModel,
    onAddUsageClick: () -> Unit,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BalanceCard(uiModel = uiModel)
        ActionButtons(
            onAddUsageClick = onAddUsageClick,
            onImageClick = onImageClick
        )
    }
}

@Composable
private fun CashVoucherBottomSection(
    uiModel: CashVoucherDetailUiModel,
    onShowBarcodeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    VoucherDetailBottomSection(
        uiModel = VoucherDetailBottomSectionUiModel(
            barcodeNumber = uiModel.barcodeNumber,
            expireDateText = uiModel.expireDateText,
            expireBadgeText = uiModel.expireBadgeText,
            exchangePlaceText = uiModel.exchangePlaceText,
            memoText = uiModel.memo
        ),
        onShowBarcodeClick = onShowBarcodeClick,
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
                        color = Color.White
                    ) {
                        AsyncImage(
                            model = uiModel.brandLogoUrl,
                            contentDescription = "브랜드 로고",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = uiModel.brand,
                            color = Color.White.copy(alpha = 0.85f),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = uiModel.title,
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Surface(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = uiModel.status,
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "남은 잔액",
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.labelSmall
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = uiModel.remainAmountText,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "원",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White.copy(alpha = 0.08f))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "유효기간",
                    color = Color.White.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = uiModel.expireDateText,
                    color = Color.White,
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
    onImageClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            modifier = Modifier.weight(1f),
            onClick = onAddUsageClick,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CouponAccent,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            Icon(imageVector = Icons.Outlined.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("사용 내역 추가", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        }

        Button(
            modifier = Modifier.weight(1f),
            onClick = onImageClick,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF1F5F9),
                contentColor = CouponTextPrimary
            ),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            Icon(imageVector = Icons.Outlined.Image, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("교환권 이미지", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 390, heightDp = 844)
@Composable
private fun CashVoucherDetailScreenPreview() {
    GifticonAlarmTheme {
        CashVoucherDetailScreen(
            couponId = "1",
            onNavigateBack = {},
            uiModel = CashVoucherDetailUiModel.placeholder(couponId = "1")
        )
    }
}
