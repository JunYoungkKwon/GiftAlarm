package com.example.gifticonalarm.ui.feature.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 선택 상태를 표시하는 공통 드롭다운 메뉴 아이템.
 */
@Composable
fun SelectableDropdownMenuItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    selectedBackgroundColor: Color,
    textColor: Color,
    checkTintColor: Color
) {
    DropdownMenuItem(
        text = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            )
        },
        onClick = onClick,
        trailingIcon = {
            if (selected) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = checkTintColor
                )
            }
        },
        colors = MenuDefaults.itemColors(
            textColor = textColor,
            trailingIconColor = checkTintColor
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) selectedBackgroundColor else Color.Transparent)
    )
}
