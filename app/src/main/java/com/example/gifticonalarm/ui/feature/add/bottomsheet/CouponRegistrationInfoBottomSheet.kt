package com.example.gifticonalarm.ui.feature.add.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val SheetBackground = Color(0xFFFFFFFF)
private val SheetAccent = Color(0xFF191970)
private val SheetTitle = Color(0xFF111827)
private val SheetDescription = Color(0xFF4B5563)
private val SheetInfoCard = Color(0xFFF3F4F6)
private val SheetInfoAccent = Color(0xFFEF4444)

enum class CouponRegistrationInfoSheetType {
    NONE,
    BARCODE_INFO,
    NOTIFICATION_INFO
}

/**
 * 쿠폰 등록 화면의 안내용 바텀시트.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponRegistrationInfoBottomSheet(
    type: CouponRegistrationInfoSheetType,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (type == CouponRegistrationInfoSheetType.NONE) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = SheetBackground,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 8.dp)
                    .width(40.dp)
                    .height(6.dp)
                    .background(Color(0xFFD1D5DB), RoundedCornerShape(999.dp))
            )
        }
    ) {
        InfoSheetContent(
            type = type,
            onCloseClick = onDismissRequest,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun InfoSheetContent(
    type: CouponRegistrationInfoSheetType,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val title = when (type) {
        CouponRegistrationInfoSheetType.BARCODE_INFO -> "바코드 번호 없이 등록하기"
        CouponRegistrationInfoSheetType.NOTIFICATION_INFO -> "만료일 알림은 언제 오나요?"
        CouponRegistrationInfoSheetType.NONE -> ""
    }

    val description = when (type) {
        CouponRegistrationInfoSheetType.BARCODE_INFO -> "바코드가 없어서 매장에서 바로 사용하지는 못하지만, 등록된 유효기한을 기준으로 만료일 알림을 보내드려요."
        CouponRegistrationInfoSheetType.NOTIFICATION_INFO -> "소중한 쿠폰, 만료되기 전에 쓸 수 있도록 미리 알려드릴게요!"
        CouponRegistrationInfoSheetType.NONE -> ""
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = SheetTitle
        )

        if (type == CouponRegistrationInfoSheetType.BARCODE_INFO) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SheetInfoCard, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.WarningAmber,
                        contentDescription = null,
                        tint = SheetInfoAccent,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "유의 사항",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = SheetTitle
                    )
                }
                Text(
                    text = "• $description",
                    style = MaterialTheme.typography.bodySmall,
                    color = SheetDescription
                )
            }
        } else if (type == CouponRegistrationInfoSheetType.NOTIFICATION_INFO) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = SheetDescription
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SheetInfoCard, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Alarm,
                        contentDescription = null,
                        tint = SheetInfoAccent,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "알림 발송 시점",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = SheetTitle
                    )
                }
                Text(
                    text = "• 만료일의 7일 전, 3일 전, 그리고 당일 오전 11시",
                    style = MaterialTheme.typography.bodySmall,
                    color = SheetDescription
                )
                Text(
                    text = "• 당일에는 오후 3시에 한 번 더 발송돼요.",
                    style = MaterialTheme.typography.bodySmall,
                    color = SheetDescription
                )
            }
        }

        Button(
            onClick = onCloseClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = SheetAccent
            )
        ) {
            Text(
                text = "확인",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
