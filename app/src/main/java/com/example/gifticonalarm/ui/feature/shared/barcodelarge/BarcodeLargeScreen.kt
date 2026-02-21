package com.example.gifticonalarm.ui.feature.shared.barcodelarge
import com.example.gifticonalarm.ui.feature.shared.text.TextFormatters
import com.example.gifticonalarm.ui.feature.shared.text.VoucherText

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.gifticonalarm.R
import com.example.gifticonalarm.ui.feature.shared.components.BackNavigationIcon
import com.example.gifticonalarm.ui.feature.shared.util.encodeCode128ModulesOrNull
import com.example.gifticonalarm.ui.feature.shared.util.formatBarcodeNumberForDisplay

private val ScreenBackground = Color(0xFFFFFFFF)
private val Primary = Color(0xFF191971)
private val TitleColor = Color(0xFF111827)
private val SubText = Color(0xFF6B7280)
private val Caption = Color(0xFF9CA3AF)
private val SoftBackground = Color(0xFFF9FAFB)

/**
 * 바코드 크게 보기 화면.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarcodeLargeScreen(
    uiModel: BarcodeLargeUiModel,
    onCloseClick: () -> Unit
) {
    Scaffold(
        containerColor = ScreenBackground,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ScreenBackground),
                title = {
                    Text(
                        text = VoucherText.BARCODE_TITLE,
                        style = MaterialTheme.typography.titleMedium,
                        color = TitleColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                },
                navigationIcon = {
                    BackNavigationIcon(onClick = onCloseClick, tint = Primary)
                },
                actions = {
                    Surface(
                        modifier = Modifier.offset(x = (-5).dp, y = 3.dp),
                        color = Color(0xFFF3F4F6),
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.WbSunny,
                                contentDescription = null,
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = VoucherText.BARCODE_BRIGHTNESS_MAX,
                                style = MaterialTheme.typography.labelSmall,
                                color = SubText,
                                fontWeight = FontWeight.Bold

                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                    contentDescription = VoucherText.BARCODE_IMAGE_DESCRIPTION,
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
                text = TextFormatters.exchangePlaceLabel(uiModel.exchangePlaceText),
                style = MaterialTheme.typography.bodySmall,
                color = SubText
            )

            Spacer(modifier = Modifier.height(28.dp))
            BarcodeArea(barcodeNumber = uiModel.barcodeNumber)
            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = formatBarcodeNumberForDisplay(uiModel.barcodeNumber),
                style = MaterialTheme.typography.headlineMedium,
                color = Primary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = VoucherText.BARCODE_HINT_INPUT_NUMBER,
                style = MaterialTheme.typography.bodySmall,
                color = Caption
            )

            Spacer(modifier = Modifier.height(24.dp))
            ExpireChip(expireDateText = uiModel.expireDateText)
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
                text = TextFormatters.expireDateLabel(expireDateText),
                style = MaterialTheme.typography.bodySmall,
                color = SubText,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
