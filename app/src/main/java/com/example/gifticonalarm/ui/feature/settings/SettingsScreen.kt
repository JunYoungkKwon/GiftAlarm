package com.example.gifticonalarm.ui.feature.settings

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val SettingsBackground = Color(0xFFF6F6F8)
private val SettingsSurface = Color.White
private val SettingsPrimary = Color(0xFF191971)
private val SettingsTitle = Color(0xFF0F172A)
private val SettingsSubText = Color(0xFF64748B)
private val SettingsCaption = Color(0xFF94A3B8)
private val SettingsDivider = Color(0xFFE2E8F0)
private val DangerBadge = Color(0xFFDC2626)
private val DangerBadgeBg = Color(0xFFFEE2E2)

/**
 * 알림 설정 화면.
 */
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onPushEnabledChange: (Boolean) -> Unit,
    onNotify30DaysChange: (Boolean) -> Unit,
    onNotify7DaysChange: (Boolean) -> Unit,
    onNotify3DaysChange: (Boolean) -> Unit,
    onNotify1DayChange: (Boolean) -> Unit,
    onNotificationTimeChange: (Int, Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SettingsBackground)
    ) {
        SettingsHeader()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PushToggleSection(
                enabled = uiState.isPushEnabled,
                onCheckedChange = onPushEnabledChange
            )
            NotificationCycleSection(
                uiState = uiState,
                onNotify30DaysChange = onNotify30DaysChange,
                onNotify7DaysChange = onNotify7DaysChange,
                onNotify3DaysChange = onNotify3DaysChange,
                onNotify1DayChange = onNotify1DayChange
            )
            NotificationTimeSection(
                timeText = uiState.notificationTimeText,
                hour = uiState.notificationHour,
                minute = uiState.notificationMinute,
                onTimeSelected = onNotificationTimeChange
            )
            SoundAndVibrationSection()
            Text(
                text = "설정 변경 사항은 자동으로 저장됩니다.",
                style = MaterialTheme.typography.bodySmall,
                color = SettingsCaption,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp)
            )
        }
    }
}

@Composable
private fun SettingsHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SettingsSurface)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "알림 설정",
            style = MaterialTheme.typography.titleMedium,
            color = SettingsTitle,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PushToggleSection(
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    RoundedSurface {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "푸시 알림 받기",
                    style = MaterialTheme.typography.bodyLarge,
                    color = SettingsTitle,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "기프티콘 만료 전 알림을 받아보세요.",
                    style = MaterialTheme.typography.bodySmall,
                    color = SettingsSubText
                )
            }
            Switch(
                checked = enabled,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = SettingsPrimary,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFCBD5E1)
                )
            )
        }
    }
}

@Composable
private fun NotificationCycleSection(
    uiState: SettingsUiState,
    onNotify30DaysChange: (Boolean) -> Unit,
    onNotify7DaysChange: (Boolean) -> Unit,
    onNotify3DaysChange: (Boolean) -> Unit,
    onNotify1DayChange: (Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle(title = "알림 주기")
        RoundedSurface {
            Column {
                NotificationOptionRow(
                    title = "만료 30일 전",
                    badgeText = "D-30",
                    badgeBg = Color(0xFFEFF6FF),
                    badgeTextColor = SettingsPrimary,
                    checked = uiState.notify30DaysBefore,
                    onCheckedChange = onNotify30DaysChange
                )
                RowDivider()
                NotificationOptionRow(
                    title = "만료 7일 전",
                    badgeText = "D-7",
                    badgeBg = Color(0xFFEFF6FF),
                    badgeTextColor = SettingsPrimary,
                    checked = uiState.notify7DaysBefore,
                    onCheckedChange = onNotify7DaysChange
                )
                RowDivider()
                NotificationOptionRow(
                    title = "만료 3일 전",
                    badgeText = "D-3",
                    badgeBg = Color(0xFFEFF6FF),
                    badgeTextColor = SettingsPrimary,
                    checked = uiState.notify3DaysBefore,
                    onCheckedChange = onNotify3DaysChange
                )
                RowDivider()
                NotificationOptionRow(
                    title = "만료 1일 전",
                    badgeText = "D-1",
                    badgeBg = DangerBadgeBg,
                    badgeTextColor = DangerBadge,
                    checked = uiState.notify1DayBefore,
                    onCheckedChange = onNotify1DayChange
                )
            }
        }
        Text(
            text = "선택한 날짜의 오전에 알림을 보내드립니다.",
            style = MaterialTheme.typography.bodySmall,
            color = SettingsCaption,
            modifier = Modifier.padding(horizontal = 2.dp)
        )
    }
}

@Composable
private fun NotificationOptionRow(
    title: String,
    badgeText: String,
    badgeBg: Color,
    badgeTextColor: Color,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = SettingsTitle,
                fontWeight = FontWeight.Medium
            )
            Box(
                modifier = Modifier
                    .background(badgeBg, CircleShape)
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.labelSmall,
                    color = badgeTextColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = SettingsPrimary,
                uncheckedColor = Color(0xFFCBD5E1),
                checkmarkColor = Color.White
            )
        )
    }
}

@Composable
private fun NotificationTimeSection(
    timeText: String,
    hour: Int,
    minute: Int,
    onTimeSelected: (Int, Int) -> Unit
) {
    val context = LocalContext.current
    val openTimePicker = {
        TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                onTimeSelected(selectedHour, selectedMinute)
            },
            hour,
            minute,
            true
        ).show()
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle(title = "알림 수신 시간")
        RoundedSurface {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "시간 선택",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SettingsTitle,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    modifier = Modifier.clickable(onClick = openTimePicker),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFE8E8F0), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = timeText,
                            color = SettingsPrimary,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Icon(
                        imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8)
                    )
                }
            }
        }
    }
}

@Composable
private fun SoundAndVibrationSection() {
    RoundedSurface {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "소리 및 진동 설정",
                style = MaterialTheme.typography.bodyMedium,
                color = SettingsTitle,
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF94A3B8)
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        color = SettingsSubText,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 2.dp)
    )
}

@Composable
private fun RoundedSurface(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SettingsSurface, RoundedCornerShape(14.dp))
            .border(1.dp, SettingsDivider.copy(alpha = 0.35f), RoundedCornerShape(14.dp)),
        content = content
    )
}

@Composable
private fun RowDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(SettingsDivider.copy(alpha = 0.5f))
    )
}
