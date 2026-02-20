package com.example.gifticonalarm.ui.feature.shared.barcodelarge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.gifticonalarm.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer

private val ScreenBackground = Color(0xFFFFFFFF)
private val Primary = Color(0xFF191971)
private val TitleColor = Color(0xFF111827)
private val SubText = Color(0xFF6B7280)
private val Caption = Color(0xFF9CA3AF)
private val SoftBackground = Color(0xFFF9FAFB)

/**
 * 바코드 크게 보기 화면.
 */
@Composable
fun BarcodeLargeScreen(
    uiModel: BarcodeLargeUiModel,
    onCloseClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Header(onCloseClick = onCloseClick)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .size(192.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFF3F4F6)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = uiModel.productImageUrl,
                    contentDescription = "기프티콘 이미지",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.default_coupon_image),
                    fallback = painterResource(id = R.drawable.default_coupon_image)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = uiModel.title,
                style = MaterialTheme.typography.titleLarge,
                color = TitleColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "교환처: ${uiModel.exchangePlaceText}",
                style = MaterialTheme.typography.bodySmall,
                color = SubText
            )

            Spacer(modifier = Modifier.height(28.dp))
            BarcodeArea(barcodeNumber = uiModel.barcodeNumber)
            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = uiModel.barcodeNumber,
                style = MaterialTheme.typography.headlineMedium,
                color = Primary,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "바코드가 읽히지 않으면 번호를 입력해주세요.",
                style = MaterialTheme.typography.bodySmall,
                color = Caption
            )

            Spacer(modifier = Modifier.height(24.dp))
            ExpireChip(expireDateText = uiModel.expireDateText)
        }
    }
}

@Composable
private fun Header(onCloseClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "닫기",
                    tint = Primary
                )
            }
        }
        Surface(
            color = Color(0xFFF3F4F6),
            shape = RoundedCornerShape(999.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.WbSunny,
                    contentDescription = null,
                    tint = Color(0xFFF59E0B),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "밝기 최대",
                    style = MaterialTheme.typography.labelSmall,
                    color = SubText,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun BarcodeArea(barcodeNumber: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .drawBehind {
                val modules = encodeCode128ModulesOrNull(barcodeNumber)
                if (modules == null || modules.isEmpty()) return@drawBehind

                val horizontalPadding = 10.dp.toPx()
                val verticalPadding = 8.dp.toPx()
                val drawHeight = size.height - (verticalPadding * 2f)
                val drawWidth = size.width - (horizontalPadding * 2f)
                if (drawWidth <= 0f || drawHeight <= 0f) return@drawBehind

                val moduleWidth = drawWidth / modules.size.toFloat()
                modules.forEachIndexed { index, isBlack ->
                    if (!isBlack) return@forEachIndexed
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset(horizontalPadding + (index * moduleWidth), verticalPadding),
                        size = Size(moduleWidth, drawHeight)
                    )
                }
            }
    )
}

@Composable
private fun ExpireChip(expireDateText: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SoftBackground),
        shape = RoundedCornerShape(999.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Schedule,
                contentDescription = null,
                tint = Caption,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "유효기간: $expireDateText",
                style = MaterialTheme.typography.bodySmall,
                color = SubText,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

private fun encodeCode128ModulesOrNull(barcodeNumber: String): BooleanArray? {
    val normalized = barcodeNumber.trim()
    if (normalized.isBlank()) return null
    if (!normalized.all { it.code in 32..126 }) return null

    return runCatching {
        val bitMatrix = Code128Writer().encode(normalized, BarcodeFormat.CODE_128, 800, 1)
        val row = bitMatrix.height / 2
        BooleanArray(bitMatrix.width) { x -> bitMatrix.get(x, row) }
    }.getOrNull()
}
