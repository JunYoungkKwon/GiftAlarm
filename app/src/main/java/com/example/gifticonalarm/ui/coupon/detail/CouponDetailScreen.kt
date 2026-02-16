package com.example.gifticonalarm.ui.coupon.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.LocalCafe
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.gifticonalarm.ui.theme.GifticonAlarmTheme

private val CouponAccent = Color(0xFF191970)
private val CouponBackground = Color(0xFFFFFFFF)
private val CouponSurface = Color(0xFFF6F6F8)
private val CouponTextPrimary = Color(0xFF0F172A)
private val CouponTextSecondary = Color(0xFF64748B)

/**
 * 쿠폰 상세 화면에 표시할 UI 모델.
 */
data class CouponDetailUiModel(
    val couponId: String,
    val brand: String,
    val title: String,
    val status: String,
    val remainAmountText: String,
    val expireDateText: String,
    val barcodeNumber: String,
    val memo: String,
    val brandLogoUrl: String,
    val usageHistories: List<CouponUsageHistoryUiModel>
) {
    companion object {
        fun placeholder(couponId: String): CouponDetailUiModel = CouponDetailUiModel(
            couponId = couponId,
            brand = "스타벅스",
            title = "e카드 5만원 교환권",
            status = "사용가능",
            remainAmountText = "32,500",
            expireDateText = "2024.12.31 까지",
            barcodeNumber = "1234 5678 9012",
            memo = "친구 생일 선물로 받은 기프티콘. 유효기간 연장 1회 완료함.",
            brandLogoUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAXVWYahb8gPbQN0cgsRi5zK9FKYl2Hy4pcMW13H2rafEVzYzFh6EFlFpNyyKaJfmw5hXjnAaGKgOTUme6FFpdjLyt2HfAdvf4AuYNExmaFc95S4Kdzj4UbJuTd-7FnOl4msBfNkyakzIXVuoHQTroSR8iUFfAJFsapq_JyzJqORe8kTGMwr7EjPtINS1mZHKAfAgpxM8R9W5Yl9SjXYIYJVc34QmoWsCCIRG13WJpgzPqVhAhS0wUAK6QV_fZh21BMlltykcQNeYKG",
            usageHistories = listOf(
                CouponUsageHistoryUiModel(
                    place = "스타벅스 강남점",
                    dateTime = "2023.11.15 14:30",
                    amount = "-4,500원",
                    icon = UsageIcon.CAFE
                ),
                CouponUsageHistoryUiModel(
                    place = "스타벅스 홍대입구역점",
                    dateTime = "2023.10.02 09:15",
                    amount = "-6,200원",
                    icon = UsageIcon.CAFE
                ),
                CouponUsageHistoryUiModel(
                    place = "스타벅스 코엑스몰점",
                    dateTime = "2023.09.28 18:45",
                    amount = "-6,800원",
                    icon = UsageIcon.CAKE
                )
            )
        )
    }
}

/**
 * 사용 내역 한 건에 대한 UI 모델.
 */
data class CouponUsageHistoryUiModel(
    val place: String,
    val dateTime: String,
    val amount: String,
    val icon: UsageIcon
)

/**
 * 사용 내역 아이콘 타입.
 */
enum class UsageIcon {
    CAFE,
    CAKE
}

/**
 * 홈 대시보드에서 진입하는 쿠폰 상세 화면.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponDetailScreen(
    couponId: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    uiModel: CouponDetailUiModel = CouponDetailUiModel.placeholder(couponId),
    onAddUsageClick: () -> Unit = {},
    onImageClick: () -> Unit = {},
    onShowBarcodeClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        containerColor = CouponBackground,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CouponBackground
                ),
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
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Outlined.MoreHoriz,
                            contentDescription = "더보기",
                            tint = CouponTextPrimary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CouponBackground)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 230.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { BalanceCard(uiModel = uiModel) }
                item {
                    ActionButtons(
                        onAddUsageClick = onAddUsageClick,
                        onImageClick = onImageClick
                    )
                }
                item { UsageHistorySection(histories = uiModel.usageHistories) }
                item { MemoSection(memo = uiModel.memo) }
            }

            BarcodeBottomSheet(
                barcodeNumber = uiModel.barcodeNumber,
                onShowBarcodeClick = onShowBarcodeClick,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun BalanceCard(uiModel: CouponDetailUiModel) {
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
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = uiModel.brandLogoUrl,
                            contentDescription = "브랜드 로고",
                            modifier = Modifier.fillMaxSize()
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
                    .border(1.dp, Color.White.copy(alpha = 0.12f), RectangleShape)
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
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

@Composable
private fun UsageHistorySection(histories: List<CouponUsageHistoryUiModel>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "사용 내역",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CouponTextPrimary
            )
            Surface(color = Color(0xFFF1F5F9), shape = RoundedCornerShape(6.dp)) {
                Text(
                    text = "총 ${histories.size}건",
                    style = MaterialTheme.typography.labelSmall,
                    color = CouponTextSecondary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }

        Box {
            Box(
                modifier = Modifier
                    .padding(start = 19.dp, top = 18.dp, bottom = 18.dp)
                    .width(1.dp)
                    .height((histories.size * 64).dp)
                    .background(Color(0xFFE2E8F0))
            )
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                histories.forEachIndexed { index, history ->
                    UsageHistoryItem(
                        history = history,
                        isLast = index == histories.lastIndex
                    )
                }
            }
        }
    }
}

@Composable
private fun UsageHistoryItem(
    history: CouponUsageHistoryUiModel,
    isLast: Boolean
) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, Color(0xFFE2E8F0), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = when (history.icon) {
                    UsageIcon.CAFE -> Icons.Outlined.LocalCafe
                    UsageIcon.CAKE -> Icons.Outlined.LocalCafe
                },
                contentDescription = null,
                tint = CouponTextSecondary,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 3.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = history.place,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = CouponTextPrimary
                    )
                    Text(
                        text = history.dateTime,
                        style = MaterialTheme.typography.labelSmall,
                        color = CouponTextSecondary
                    )
                }
                Text(
                    text = history.amount,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = CouponTextPrimary
                )
            }
            if (!isLast) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun MemoSection(memo: String) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = CouponSurface)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.MoreHoriz,
                    contentDescription = null,
                    tint = CouponTextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "메모",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = CouponTextPrimary
                )
            }
            Text(
                text = memo,
                style = MaterialTheme.typography.bodySmall,
                color = CouponTextSecondary
            )
        }
    }
}

@Composable
private fun BarcodeBottomSheet(
    barcodeNumber: String,
    onShowBarcodeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = CouponBackground,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color(0xFFE2E8F0))
            )

            Spacer(modifier = Modifier.height(10.dp))
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth(),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BarcodeGraphic()
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = barcodeNumber,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = CouponTextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Outlined.ContentCopy,
                            contentDescription = "바코드 번호 복사",
                            tint = CouponTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onShowBarcodeClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = CouponAccent
                ),
                elevation = null,
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Image,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "바코드 크게보기",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun BarcodeGraphic() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White)
            .drawBehind {
                val thinBar = 2.dp.toPx()
                val wideGap = 3.dp.toPx()
                var x = 0f
                while (x < size.width) {
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset(x, 0f),
                        size = Size(thinBar, size.height)
                    )
                    x += thinBar + wideGap
                }
            }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 390, heightDp = 844)
@Composable
private fun CouponDetailScreenPreview() {
    GifticonAlarmTheme {
        CouponDetailScreen(
            couponId = "1",
            onNavigateBack = {},
            uiModel = CouponDetailUiModel.placeholder(couponId = "1")
        )
    }
}
