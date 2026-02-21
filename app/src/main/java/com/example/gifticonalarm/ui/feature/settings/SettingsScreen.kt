package com.example.gifticonalarm.ui.feature.settings
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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gifticonalarm.ui.feature.shared.text.SettingsText
import com.example.gifticonalarm.ui.feature.shared.text.TextFormatters

private val SettingsBackground = Color(0xFFF6F6F8)
private val SettingsSurface = Color.White
private val SettingsPrimary = Color(0xFF191971)
private val SettingsTitle = Color(0xFF0F172A)
private val SettingsSubText = Color(0xFF64748B)
private val SettingsDivider = Color(0xFFE2E8F0)
private val DangerBadge = Color(0xFFDC2626)
private val DangerBadgeBg = Color(0xFFFEE2E2)
private val DefaultCycleBadgeBg = Color(0xFFEFF6FF)
private val PageHorizontalPadding = 20.dp
private val CardHorizontalPadding = 16.dp
private val SectionSpacing = 18.dp

private data class NotificationCycleOption(
    val title: String,
    val day: Int,
    val checked: Boolean,
    val onCheckedChange: (Boolean) -> Unit,
    val badgeBg: Color,
    val badgeTextColor: Color
)

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
    onNotificationTimeClick: () -> Unit
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
                .padding(horizontal = PageHorizontalPadding, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(SectionSpacing)
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
                onTimeClick = onNotificationTimeClick
            )
            SoundAndVibrationSection()
        }
    }
}

@Composable
private fun SettingsHeader() {
    Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SettingsSurface)
                .padding(horizontal = PageHorizontalPadding, vertical = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = SettingsText.TITLE,
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
                .padding(horizontal = CardHorizontalPadding, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = SettingsText.PUSH_TITLE,
                    style = MaterialTheme.typography.bodyLarge,
                    color = SettingsTitle,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = SettingsText.PUSH_DESCRIPTION,
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
    val cycleOptions = listOf(
        NotificationCycleOption(
            title = SettingsText.NOTIFY_30_DAYS,
            day = 30,
            checked = uiState.notify30DaysBefore,
            onCheckedChange = onNotify30DaysChange,
            badgeBg = DefaultCycleBadgeBg,
            badgeTextColor = SettingsPrimary
        ),
        NotificationCycleOption(
            title = SettingsText.NOTIFY_7_DAYS,
            day = 7,
            checked = uiState.notify7DaysBefore,
            onCheckedChange = onNotify7DaysChange,
            badgeBg = DefaultCycleBadgeBg,
            badgeTextColor = SettingsPrimary
        ),
        NotificationCycleOption(
            title = SettingsText.NOTIFY_3_DAYS,
            day = 3,
            checked = uiState.notify3DaysBefore,
            onCheckedChange = onNotify3DaysChange,
            badgeBg = DefaultCycleBadgeBg,
            badgeTextColor = SettingsPrimary
        ),
        NotificationCycleOption(
            title = SettingsText.NOTIFY_1_DAY,
            day = 1,
            checked = uiState.notify1DayBefore,
            onCheckedChange = onNotify1DayChange,
            badgeBg = DangerBadgeBg,
            badgeTextColor = DangerBadge
        )
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle(title = SettingsText.CYCLE_TITLE)
        RoundedSurface {
            Column {
                cycleOptions.forEachIndexed { index, option ->
                    NotificationOptionRow(
                        title = option.title,
                        badgeText = TextFormatters.ddayLabel(option.day),
                        badgeBg = option.badgeBg,
                        badgeTextColor = option.badgeTextColor,
                        checked = option.checked,
                        onCheckedChange = option.onCheckedChange
                    )
                    if (index != cycleOptions.lastIndex) {
                        RowDivider()
                    }
                }
            }
        }
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
            .heightIn(min = 54.dp)
            .padding(horizontal = CardHorizontalPadding, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
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
                    .padding(horizontal = 8.dp, vertical = 3.dp),
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
    onTimeClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle(title = SettingsText.TIME_TITLE)
        RoundedSurface {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onTimeClick)
                    .padding(horizontal = CardHorizontalPadding, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = SettingsText.TIME_PICKER,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SettingsTitle,
                    fontWeight = FontWeight.Medium
                )
                Row(
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
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(18.dp)
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
                .padding(horizontal = CardHorizontalPadding, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = SettingsText.SOUND_AND_VIBRATION,
                style = MaterialTheme.typography.bodyMedium,
                color = SettingsTitle,
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF94A3B8),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp),
        color = SettingsSubText,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 4.dp)
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
            .padding(horizontal = CardHorizontalPadding)
            .height(1.dp)
            .background(SettingsDivider.copy(alpha = 0.5f))
    )
}
