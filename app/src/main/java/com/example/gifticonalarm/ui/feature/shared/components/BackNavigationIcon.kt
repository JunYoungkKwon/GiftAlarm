package com.example.gifticonalarm.ui.feature.shared.components

import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.gifticonalarm.ui.feature.shared.text.CommonText

/**
 * 텍스트 셰브론(‹) 형태의 공통 뒤로가기 아이콘.
 */
@Composable
fun BackNavigationIcon(
    onClick: () -> Unit,
    tint: Color,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = CommonText.NAVIGATION_BACK_CHEVRON,
            color = tint,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}
