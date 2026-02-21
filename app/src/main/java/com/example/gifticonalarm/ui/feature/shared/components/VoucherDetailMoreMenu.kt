package com.example.gifticonalarm.ui.feature.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.gifticonalarm.ui.feature.shared.text.VoucherText
import com.example.gifticonalarm.ui.theme.GifticonBorderSoft
import com.example.gifticonalarm.ui.theme.GifticonSurfaceSoft
import com.example.gifticonalarm.ui.theme.GifticonTextSlate
import com.example.gifticonalarm.ui.theme.GifticonTextSlateStrong
import com.example.gifticonalarm.ui.theme.GifticonWhite

/**
 * 상세 화면 상단 더보기 액션 메뉴.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoucherDetailMoreMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    if (!expanded) return

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = GifticonWhite,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 8.dp)
                    .size(width = 40.dp, height = 6.dp)
                    .background(GifticonBorderSoft, RoundedCornerShape(999.dp))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            VoucherBottomSheetAction(
                text = VoucherText.ACTION_EDIT,
                tint = GifticonTextSlateStrong,
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = null,
                        tint = GifticonTextSlate
                    )
                },
                onClick = {
                    onDismissRequest()
                    onEditClick()
                }
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 18.dp),
                color = GifticonSurfaceSoft,
                thickness = 1.dp
            )
            VoucherBottomSheetAction(
                text = VoucherText.ACTION_DELETE,
                tint = MaterialTheme.colorScheme.error,
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                onClick = {
                    onDismissRequest()
                    onDeleteClick()
                }
            )
        }
    }
}

@Composable
private fun VoucherBottomSheetAction(
    text: String,
    tint: Color,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    androidx.compose.material3.TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                icon()
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = tint,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}
