package com.example.gifticonalarm.ui.feature.shared.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

/**
 * 상세 화면 상단 더보기 액션 메뉴.
 */

@Composable

fun VoucherDetailMoreMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            text = { Text(text = "수정하기") },
            onClick = {
                onDismissRequest()
                onEditClick()
            }
        )
        DropdownMenuItem(
            text = {
                Text(
                    text = "삭제하기",
                    color = MaterialTheme.colorScheme.error
                )
            },
            onClick = {
                onDismissRequest()
                onDeleteClick()
            }
        )
    }
}
