package com.example.gifticonalarm.ui.feature.settings

import android.widget.NumberPicker
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.gifticonalarm.ui.feature.shared.components.BackNavigationIcon
import com.example.gifticonalarm.ui.feature.shared.text.SettingsText
import com.example.gifticonalarm.ui.feature.shared.util.formatHourMinute
import com.example.gifticonalarm.ui.feature.shared.util.formatTwoDigits

private val ScreenBackground = Color(0xFFF8FAFC)
private val Surface = Color.White
private val Primary = Color(0xFF191970)
private val SubText = Color(0xFF64748B)
private val PickerHighlight = Color(0x1A191970)
private val PickerBorder = Color(0xFFE2E8F0)
private val PickerSurface = Color(0xFFFDFEFF)

/**
 * 알림 수신 시간 선택 화면.
 */
@Composable
fun SettingsTimeScreen(
    uiState: SettingsTimeUiState,
    onBackClick: () -> Unit,
    onSaveClick: (periodIndex: Int, hour12: Int, minute: Int) -> Unit
) {
    var selectedPeriod by remember(uiState.periodIndex) { mutableIntStateOf(uiState.periodIndex) }
    var selectedHour by remember(uiState.hour12) { mutableIntStateOf(uiState.hour12) }
    var selectedMinute by remember(uiState.minute) { mutableIntStateOf(uiState.minute) }
    var periodPickerRef by remember { mutableStateOf<NumberPicker?>(null) }
    var hourPickerRef by remember { mutableStateOf<NumberPicker?>(null) }
    var minutePickerRef by remember { mutableStateOf<NumberPicker?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        Header(onBackClick = onBackClick)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = SettingsText.TIME_SELECTED,
                style = MaterialTheme.typography.labelLarge,
                color = SubText
            )
            Text(
                text = selectedTimeLabel(
                    periodIndex = selectedPeriod,
                    hour = selectedHour,
                    minute = selectedMinute
                ),
                style = MaterialTheme.typography.headlineSmall,
                color = Primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(244.dp)
                    .border(
                        width = 1.dp,
                        color = PickerBorder,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(
                        color = PickerSurface,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .align(Alignment.Center)
                        .background(PickerHighlight, RoundedCornerShape(10.dp))
                )
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    WheelPicker(
                        values = arrayOf(SettingsText.PERIOD_AM, SettingsText.PERIOD_PM),
                        selectedIndex = selectedPeriod,
                        onSelectedIndexChange = { selectedPeriod = it },
                        onPickerReady = { periodPickerRef = it },
                        modifier = Modifier.weight(1f)
                    )
                    WheelPicker(
                        values = (1..12).map(::formatTwoDigits).toTypedArray(),
                        selectedIndex = selectedHour - 1,
                        onSelectedIndexChange = { selectedHour = it + 1 },
                        onPickerReady = { hourPickerRef = it },
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = ":",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Primary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    WheelPicker(
                        values = (0..59).map(::formatTwoDigits).toTypedArray(),
                        selectedIndex = selectedMinute,
                        onSelectedIndexChange = { selectedMinute = it },
                        onPickerReady = { minutePickerRef = it },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = SettingsText.TIME_GUIDE,
                style = MaterialTheme.typography.bodyMedium,
                color = SubText,
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Surface)
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Button(
                onClick = {
                    val actualPeriod = periodPickerRef?.value ?: selectedPeriod
                    val actualHour = (hourPickerRef?.value ?: (selectedHour - 1)) + 1
                    val actualMinute = minutePickerRef?.value ?: selectedMinute
                    onSaveClick(actualPeriod, actualHour, actualMinute)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary
                )
            ) {
                Text(
                    text = SettingsText.TIME_SAVE,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

private fun selectedTimeLabel(periodIndex: Int, hour: Int, minute: Int): String {
    val periodLabel = if (periodIndex == 0) SettingsText.PERIOD_AM else SettingsText.PERIOD_PM
    return "$periodLabel ${formatHourMinute(hour, minute)}"
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Header(onBackClick: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface),
        title = {
            Text(
                text = SettingsText.TIME_SCREEN_TITLE,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF111827),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 3.dp)
            )
        },
        navigationIcon = {
            BackNavigationIcon(
                onClick = onBackClick,
                tint = Color(0xFF111827)
            )
        }
    )
}

@Composable
private fun WheelPicker(
    values: Array<String>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    onPickerReady: (NumberPicker) -> Unit,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier.height(220.dp),
        factory = { context ->
            NumberPicker(context).apply {
                minValue = 0
                maxValue = values.lastIndex
                wrapSelectorWheel = true
                descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                displayedValues = values
                value = selectedIndex.coerceIn(0, values.lastIndex)
                setOnValueChangedListener { _, _, newVal ->
                    onSelectedIndexChange(newVal)
                }
                onPickerReady(this)
            }
        },
        update = { picker ->
            picker.displayedValues = null
            picker.minValue = 0
            picker.maxValue = values.lastIndex
            picker.displayedValues = values
            val bounded = selectedIndex.coerceIn(0, values.lastIndex)
            if (picker.value != bounded) {
                picker.value = bounded
            }
        }
    )
}
