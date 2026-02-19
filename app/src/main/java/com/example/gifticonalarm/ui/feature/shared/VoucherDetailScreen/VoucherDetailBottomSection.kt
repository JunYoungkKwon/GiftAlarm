package com.example.gifticonalarm.ui.feature.shared.voucherdetailscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Fullscreen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val BottomBackground = Color(0xFFF8FAFC)
private val CardBorder = Color(0xFFE2E8F0)
private val PrimaryText = Color(0xFF0F172A)
private val SecondaryText = Color(0xFF94A3B8)
private val Accent = Color(0xFF191970)

/**
 * 상세 화면 하단 섹션(UI 교체 범위) 데이터.
 */
data class VoucherDetailBottomSectionUiModel(
    val barcodeNumber: String,
    val expireDateText: String,
    val expireBadgeText: String,
    val exchangePlaceText: String,
    val memoText: String
)

/**
 * Stitch 기반 상세 화면 하단 섹션.
 */
@Composable
fun VoucherDetailBottomSection(
    uiModel: VoucherDetailBottomSectionUiModel,
    onShowBarcodeClick: () -> Unit,
    modifier: Modifier = Modifier,
    actionButtonText: String = "바코드 크게 보기",
    actionButtonContainerColor: Color = Accent,
    actionButtonContentColor: Color = Color.White,
    actionButtonBorderColor: Color? = null,
    showActionButtonIcon: Boolean = true
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = BottomBackground,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(PaddingValues(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 120.dp)),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                BarcodeCard(barcodeNumber = uiModel.barcodeNumber)
                ExpireInfo(expireDateText = uiModel.expireDateText, expireBadgeText = uiModel.expireBadgeText)
                InfoBlock(content = uiModel.exchangePlaceText)
                MemoBlock(memoText = uiModel.memoText)
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            color = Color.White,
            shadowElevation = 10.dp
        ) {
            Button(
                onClick = onShowBarcodeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                shape = RoundedCornerShape(16.dp),
                border = actionButtonBorderColor?.let { BorderStroke(1.dp, it) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = actionButtonContainerColor,
                    contentColor = actionButtonContentColor
                ),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                if (showActionButtonIcon) {
                    Icon(
                        imageVector = Icons.Outlined.Fullscreen,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(text = actionButtonText, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
private fun BarcodeCard(barcodeNumber: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BarcodeGraphic()
            Spacer(modifier = Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = barcodeNumber,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryText,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Outlined.ContentCopy,
                    contentDescription = "바코드 번호 복사",
                    tint = SecondaryText,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun ExpireInfo(expireDateText: String, expireBadgeText: String) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "유효기간",
            style = MaterialTheme.typography.labelSmall,
            color = SecondaryText,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = expireDateText,
                style = MaterialTheme.typography.titleMedium,
                color = PrimaryText,
                fontWeight = FontWeight.Bold
            )
            Surface(
                color = Color(0xFFFFF7ED),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFFFED7AA))
            ) {
                Text(
                    text = expireBadgeText,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFFEA580C),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun InfoBlock(content: String) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "교환처",
            style = MaterialTheme.typography.labelSmall,
            color = SecondaryText,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge,
            color = PrimaryText,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun MemoBlock(memoText: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "메모",
                style = MaterialTheme.typography.labelSmall,
                color = SecondaryText,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = memoText,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF475569)
            )
        }
    }
}

@Composable
private fun BarcodeGraphic() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .drawBehind {
                val widths = listOf(3.dp, 1.dp, 4.dp, 2.dp, 1.dp, 5.dp, 2.dp, 1.dp, 3.dp, 1.dp, 4.dp, 2.dp)
                val unit = 1.dp.toPx()
                var x = 8.dp.toPx()
                while (x < size.width - 8.dp.toPx()) {
                    widths.forEach { widthDp ->
                        if (x >= size.width - 8.dp.toPx()) return@forEach
                        drawRect(
                            color = Color.Black,
                            topLeft = Offset(x, 8.dp.toPx()),
                            size = Size(widthDp.toPx(), size.height - 16.dp.toPx())
                        )
                        x += widthDp.toPx() + unit
                    }
                }
            }
            .border(1.dp, CardBorder, RoundedCornerShape(8.dp))
    )
}
