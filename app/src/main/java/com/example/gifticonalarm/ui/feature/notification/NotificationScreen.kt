package com.example.gifticonalarm.ui.feature.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import com.example.gifticonalarm.ui.feature.shared.components.BackNavigationIcon
import com.example.gifticonalarm.ui.feature.shared.text.NotificationText

private val NotificationBackground = Color(0xFFFFFFFF)
private val NotificationUnreadBackground = Color(0xFFF0F4FF)
private val NotificationPrimary = Color(0xFF191970)
private val NotificationTitle = Color(0xFF111827)
private val NotificationBody = Color(0xFF4B5563)
private val NotificationSubText = Color(0xFF9CA3AF)
private val NotificationDivider = Color(0xFFF3F4F6)
private val NotificationCardShape = RoundedCornerShape(16.dp)
private val NotificationReadBackground = Color(0xFFFDFDFE)
private val NotificationReadIconContainer = Color(0xFFF6F6F8)
private val NotificationShadowAmbient = Color(0x14000000)
private val NotificationShadowSpot = Color(0x1A000000)

private data class NotificationVisualStyle(
    val cardElevation: androidx.compose.ui.unit.Dp,
    val cardBackgroundColor: Color,
    val iconContainerColor: Color,
    val iconTintColor: Color,
    val titleColor: Color,
    val messageColor: Color
)

/**
 * 알림 내역 화면.
 */
@Composable
fun NotificationScreen(
    uiState: NotificationUiState,
    onBackClick: () -> Unit,
    onMarkAllReadClick: () -> Unit,
    onDeleteAllClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NotificationBackground)
    ) {
        NotificationHeader(
            onBackClick = onBackClick,
            onMarkAllReadClick = onMarkAllReadClick,
            onDeleteAllClick = onDeleteAllClick
        )
        if (uiState.notifications.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = NotificationText.EMPTY,
                    style = MaterialTheme.typography.bodyMedium,
                    color = NotificationSubText
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = uiState.notifications,
                    key = { it.id }
                ) { item ->
                    NotificationRow(item = item)
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun NotificationHeader(
    onBackClick: () -> Unit,
    onMarkAllReadClick: () -> Unit,
    onDeleteAllClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = NotificationBackground),
        title = {
            Text(
                text = NotificationText.SCREEN_TITLE,
                style = MaterialTheme.typography.titleMedium,
                color = NotificationTitle,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 3.dp)
            )
        },
        navigationIcon = {
            BackNavigationIcon(onClick = onBackClick, tint = NotificationTitle)
        },
        actions = {
            Row(
                modifier = Modifier.padding(end = 8.dp, top = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = NotificationText.MARK_ALL_READ,
                    style = MaterialTheme.typography.bodySmall,
                    color = NotificationSubText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable(onClick = onMarkAllReadClick),

                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(width = 1.dp, height = 12.dp)
                        .background(NotificationDivider)
                )
                Text(
                    text = NotificationText.DELETE_ALL,
                    style = MaterialTheme.typography.bodySmall,
                    color = NotificationPrimary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable(onClick = onDeleteAllClick)
                )
            }
        }
    )
}

@Composable
private fun NotificationRow(
    item: NotificationItem
) {
    val style = item.toVisualStyle()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .shadow(
                elevation = style.cardElevation,
                shape = NotificationCardShape,
                ambientColor = NotificationShadowAmbient,
                spotColor = NotificationShadowSpot
            )
            .background(
                color = style.cardBackgroundColor,
                shape = NotificationCardShape
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = style.iconContainerColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.resolveIcon(),
                    contentDescription = null,
                    tint = style.iconTintColor
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelSmall,
                        color = style.titleColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.alignByBaseline()
                    )
                    Text(
                        text = item.timeText,
                        style = MaterialTheme.typography.labelSmall,
                        color = NotificationSubText,
                        modifier = Modifier.alignByBaseline()
                    )
                }
                Text(
                    text = item.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = style.messageColor
                )
            }
        }
    }
}

private fun NotificationItem.resolveIcon() = when (type) {
    NotificationType.EXPIRING_SOON -> {
        if (isUnread) {
            Icons.Outlined.NotificationsActive
        } else {
            Icons.Outlined.Notifications
        }
    }
    NotificationType.NEW_COUPON -> Icons.Outlined.CardGiftcard
    NotificationType.INFO -> Icons.Outlined.Notifications
}

private fun NotificationItem.toVisualStyle(): NotificationVisualStyle {
    return if (isUnread) {
        NotificationVisualStyle(
            cardElevation = 6.dp,
            cardBackgroundColor = NotificationUnreadBackground,
            iconContainerColor = Color.White,
            iconTintColor = NotificationPrimary,
            titleColor = NotificationPrimary,
            messageColor = NotificationTitle
        )
    } else {
        NotificationVisualStyle(
            cardElevation = 4.dp,
            cardBackgroundColor = NotificationReadBackground,
            iconContainerColor = NotificationReadIconContainer,
            iconTintColor = NotificationSubText,
            titleColor = NotificationSubText,
            messageColor = NotificationBody
        )
    }
}
