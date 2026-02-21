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
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Fullscreen
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
import com.example.gifticonalarm.ui.theme.GifticonBorderSoft
import com.example.gifticonalarm.ui.theme.GifticonBorderWarm
import com.example.gifticonalarm.ui.theme.GifticonBlack
import com.example.gifticonalarm.ui.theme.GifticonBrandPrimary
import com.example.gifticonalarm.ui.theme.GifticonDanger
import com.example.gifticonalarm.ui.theme.GifticonDangerBackground
import com.example.gifticonalarm.ui.theme.GifticonDangerStrong
import com.example.gifticonalarm.ui.theme.GifticonInfoBackground
import com.example.gifticonalarm.ui.theme.GifticonSurfaceSoft
import com.example.gifticonalarm.ui.theme.GifticonSurfaceUsed
import com.example.gifticonalarm.ui.theme.GifticonTextMuted
import com.example.gifticonalarm.ui.theme.GifticonTextPrimary
import com.example.gifticonalarm.ui.theme.GifticonTextSlate
import com.example.gifticonalarm.ui.theme.GifticonTextSlateStrong
import com.example.gifticonalarm.ui.theme.GifticonWarning
import com.example.gifticonalarm.ui.theme.GifticonWarningBackground
import com.example.gifticonalarm.ui.theme.GifticonWarningSoftBackground
import com.example.gifticonalarm.ui.theme.GifticonWhite

private val BottomBackground = GifticonSurfaceUsed
private val CardBorder = GifticonBorderSoft
private val PrimaryText = GifticonTextPrimary
private val SecondaryText = GifticonTextMuted
private val Accent = GifticonBrandPrimary
private const val UNREGISTERED_BARCODE = "미등록"

private data class ExpireBadgeStyle(
    val containerColor: Color,
    val borderColor: Color,
    val textColor: Color
)

/**
 * 상세 화면 하단 섹션(UI 교체 범위) 데이터.
 */
data class VoucherDetailBottomSectionUiModel(
    val barcodeNumber: String,
    val usageHistoryText: String? = null,
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
    onCopyBarcodeClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    actionButtonText: String = "바코드 크게 보기",
    actionButtonContainerColor: Color = Accent,
    actionButtonContentColor: Color = GifticonWhite,
    actionButtonBorderColor: Color? = null,
    showActionButtonIcon: Boolean = true,
    showActionButton: Boolean = true
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
                    .padding(
                        PaddingValues(
                            start = 24.dp,
                            end = 24.dp,
                            top = 24.dp,
                            bottom = if (showActionButton) 120.dp else 28.dp
                        )
                    ),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                BarcodeCard(
                    barcodeNumber = uiModel.barcodeNumber,
                    onCopyBarcodeClick = onCopyBarcodeClick
                )
                UsageHistoryBlock(usageHistoryText = uiModel.usageHistoryText)
                ExpireInfo(expireDateText = uiModel.expireDateText, expireBadgeText = uiModel.expireBadgeText)
                InfoBlock(content = uiModel.exchangePlaceText)
                MemoBlock(memoText = uiModel.memoText)
            }
        }

        if (showActionButton) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                color = GifticonWhite,
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
}

@Composable
private fun BarcodeCard(
    barcodeNumber: String,
    onCopyBarcodeClick: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = GifticonWhite),
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
            BarcodeGraphic(barcodeNumber = barcodeNumber)
            Spacer(modifier = Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = barcodeNumber,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryText
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = { onCopyBarcodeClick(barcodeNumber) },
                    modifier = Modifier.size(22.dp)
                ) {
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
}

@Composable
private fun UsageHistoryBlock(usageHistoryText: String?) {
    val usageItems = usageHistoryText
        ?.lines()
        ?.map { rawLine ->
            val line = rawLine.trim().removePrefix("•").trim()
            val chunks = line.split(" / ").map { it.trim() }.filter { it.isNotBlank() }
            UsageHistoryUiItem(
                store = chunks.getOrNull(0).orEmpty(),
                usedAt = chunks.getOrNull(1).orEmpty(),
                amount = chunks.getOrNull(2).orEmpty()
            )
        }
        ?.filter { it.store.isNotBlank() || it.usedAt.isNotBlank() || it.amount.isNotBlank() }
        .orEmpty()
    if (usageItems.isEmpty()) return

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "사용내역",
            style = MaterialTheme.typography.labelSmall,
            color = SecondaryText,
            fontWeight = FontWeight.Bold
        )
        Card(
            colors = CardDefaults.cardColors(containerColor = GifticonWhite),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, GifticonSurfaceSoft)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                usageItems.forEachIndexed { index, item ->
                    UsageHistoryRow(
                        item = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                    )
                    if (index != usageItems.lastIndex) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(GifticonSurfaceSoft)
                        )
                    }
                }
            }
        }
    }
}

private data class UsageHistoryUiItem(
    val store: String,
    val usedAt: String,
    val amount: String
)

@Composable
private fun UsageHistoryRow(
    item: UsageHistoryUiItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Storefront,
                    contentDescription = null,
                    tint = SecondaryText,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = item.store.ifBlank { "사용처 미입력" },
                    style = MaterialTheme.typography.bodyMedium,
                    color = PrimaryText,
                    fontWeight = FontWeight.Bold
                )
            }

            Surface(
                color = Accent.copy(alpha = 0.06f),
                shape = RoundedCornerShape(999.dp)
            ) {
                Text(
                    text = formatUsageAmountText(item.amount),
                    style = MaterialTheme.typography.labelLarge,
                    color = Accent,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        if (item.usedAt.isNotBlank()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.CalendarToday,
                    contentDescription = null,
                    tint = SecondaryText,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = item.usedAt,
                    style = MaterialTheme.typography.bodySmall,
                    color = GifticonTextMuted
                )
            }
        }
    }
}

private fun formatUsageAmountText(rawAmount: String): String {
    val numeric = Regex("""([0-9,]+)""")
        .find(rawAmount)
        ?.groupValues
        ?.getOrNull(1)
        ?.trim()
        .orEmpty()
    if (numeric.isBlank()) return rawAmount
    return "-${numeric}원"
}

@Composable
private fun ExpireInfo(expireDateText: String, expireBadgeText: String) {
    val badgeStyle = resolveExpireBadgeStyle(expireBadgeText)

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
                color = badgeStyle.containerColor,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, badgeStyle.borderColor)
            ) {
                Text(
                    text = expireBadgeText,
                    style = MaterialTheme.typography.labelSmall,
                    color = badgeStyle.textColor,
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
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "메모",
            style = MaterialTheme.typography.labelSmall,
            color = SecondaryText,
            fontWeight = FontWeight.Bold
        )
        Card(
            colors = CardDefaults.cardColors(containerColor = GifticonWhite),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, GifticonSurfaceSoft)
        ) {
            Text(
                text = memoText,
                style = MaterialTheme.typography.bodyMedium,
                color = GifticonTextSlateStrong,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 14.dp)
            )
        }
    }
}

@Composable
private fun BarcodeGraphic(barcodeNumber: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(GifticonWhite)
            .drawBehind {
                val modules = encodeCode128ModulesOrNull(barcodeNumber)
                if (modules != null) {
                    drawCode128Bars(modules)
                } else {
                    drawFallbackBars()
                }
            }
            .border(1.dp, CardBorder, RoundedCornerShape(8.dp))
    )
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCode128Bars(modules: BooleanArray) {
    if (modules.isEmpty()) {
        drawFallbackBars()
        return
    }

    val horizontalPadding = 8.dp.toPx()
    val topPadding = 8.dp.toPx()
    val height = size.height - (topPadding * 2f)
    val availableWidth = size.width - (horizontalPadding * 2f)
    if (availableWidth <= 0f || height <= 0f) return

    val moduleWidth = availableWidth / modules.size.toFloat()
    modules.forEachIndexed { index, isBlack ->
        if (!isBlack) return@forEachIndexed
        drawRect(
            color = GifticonBlack,
            topLeft = Offset(horizontalPadding + (index * moduleWidth), topPadding),
            size = Size(moduleWidth, height)
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawFallbackBars() {
    val widths = listOf(1.dp, 2.dp, 1.dp, 1.dp, 2.dp, 1.dp, 1.dp, 1.dp, 2.dp, 1.dp, 2.dp, 1.dp, 2.dp, 1.dp, 2.dp, 1.dp)
    val horizontalPadding = 8.dp.toPx()
    val topPadding = 8.dp.toPx()
    val barHeight = size.height - (topPadding * 2f)
    val gap = 1.dp.toPx()
    val availableWidth = size.width - (horizontalPadding * 2f)
    if (availableWidth <= 0f || barHeight <= 0f) return

    val totalPatternWidth = widths.sumOf { it.toPx().toDouble() }.toFloat() + gap * (widths.size - 1)
    if (totalPatternWidth <= 0f) return

    val targetWidth = availableWidth * 0.5f
    val scale = targetWidth / totalPatternWidth
    var x = horizontalPadding + ((availableWidth - targetWidth) / 2f)
    widths.forEach { widthDp ->
        val barWidth = widthDp.toPx() * scale
        drawRect(
            color = GifticonBlack,
            topLeft = Offset(x, topPadding),
            size = Size(barWidth, barHeight)
        )
        x += barWidth + (gap * scale)
    }
}

private fun encodeCode128ModulesOrNull(barcodeNumber: String): BooleanArray? {
    val normalizedBarcode = barcodeNumber.trim()
    if (normalizedBarcode.isBlank() || normalizedBarcode == UNREGISTERED_BARCODE) return null
    if (!normalizedBarcode.all { it.code in 32..126 }) return null

    return runCatching {
        val bitMatrix = Code128Writer().encode(normalizedBarcode, BarcodeFormat.CODE_128, 600, 1)
        val row = bitMatrix.height / 2
        BooleanArray(bitMatrix.width) { x -> bitMatrix.get(x, row) }
    }.getOrNull()
}

private fun resolveExpireBadgeStyle(expireBadgeText: String): ExpireBadgeStyle {
    val normalized = expireBadgeText.trim()
    val dday = normalized.removePrefix("D-").toLongOrNull()

    return when {
        normalized == "사용 완료" -> ExpireBadgeStyle(
            containerColor = GifticonBorderSoft,
            borderColor = GifticonBorderSoft,
            textColor = GifticonTextSlate
        )
        normalized == "만료" || (dday != null && dday < 0) -> ExpireBadgeStyle(
            containerColor = GifticonDangerBackground,
            borderColor = GifticonBorderSoft,
            textColor = GifticonDanger
        )
        dday != null && dday in 1L..7L -> ExpireBadgeStyle(
            containerColor = GifticonDangerBackground,
            borderColor = GifticonDangerBackground,
            textColor = GifticonDangerStrong
        )
        dday != null && dday in 8L..15L -> ExpireBadgeStyle(
            containerColor = GifticonInfoBackground,
            borderColor = GifticonInfoBackground,
            textColor = Accent
        )
        dday != null -> ExpireBadgeStyle(
            containerColor = GifticonWarningBackground,
            borderColor = GifticonWarningBackground,
            textColor = GifticonWarning
        )
        else -> ExpireBadgeStyle(
            containerColor = GifticonWarningSoftBackground,
            borderColor = GifticonBorderWarm,
            textColor = GifticonWarning
        )
    }
}
