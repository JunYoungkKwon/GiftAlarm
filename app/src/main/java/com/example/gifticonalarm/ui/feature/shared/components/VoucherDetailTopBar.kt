package com.example.gifticonalarm.ui.feature.shared.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import com.example.gifticonalarm.ui.feature.shared.text.VoucherText

/**
 * 상세 화면 공통 상단 바.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoucherDetailTopBar(
    title: String,
    containerColor: Color,
    contentColor: Color,
    menuExpanded: Boolean,
    onBackClick: () -> Unit,
    onExpandMenuClick: () -> Unit,
    onDismissMenu: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = containerColor),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = contentColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 3.dp)
            )
        },
        navigationIcon = {
            BackNavigationIcon(onClick = onBackClick, tint = contentColor)
        },
        actions = {
            IconButton(onClick = onExpandMenuClick) {
                Icon(
                    imageVector = Icons.Outlined.MoreHoriz,
                    contentDescription = VoucherText.ACTION_MORE,
                    tint = contentColor
                )
            }
            VoucherDetailMoreMenu(
                expanded = menuExpanded,
                onDismissRequest = onDismissMenu,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        }
    )
}
