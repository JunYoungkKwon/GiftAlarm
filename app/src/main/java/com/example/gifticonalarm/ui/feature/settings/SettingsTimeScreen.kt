package com.example.gifticonalarm.ui.feature.settings

import android.widget.NumberPicker
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

private val ScreenBackground = Color(0xFFF8FAFC)
private val Surface = Color.White
private val Primary = Color(0xFF191970)
private val SubText = Color(0xFF64748B)
private val PickerHighlight = Color(0x1A191970)

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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .align(Alignment.Center)
                        .background(PickerHighlight, RoundedCornerShape(12.dp))
                )
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    WheelPicker(
                        values = arrayOf("오전", "오후"),
                        selectedIndex = selectedPeriod,
                        onSelectedIndexChange = { selectedPeriod = it },
                        onPickerReady = { periodPickerRef = it },
                        modifier = Modifier.weight(1f)
                    )
                    WheelPicker(
                        values = (1..12).map { "%02d".format(it) }.toTypedArray(),
                        selectedIndex = selectedHour - 1,
                        onSelectedIndexChange = { selectedHour = it + 1 },
                        onPickerReady = { hourPickerRef = it },
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = ":",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Primary,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    WheelPicker(
                        values = (0..59).map { "%02d".format(it) }.toTypedArray(),
                        selectedIndex = selectedMinute,
                        onSelectedIndexChange = { selectedMinute = it },
                        onPickerReady = { minutePickerRef = it },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "설정한 시간에 맞춰 매일 알림을 보내드립니다.",
                style = MaterialTheme.typography.bodyMedium,
                color = SubText
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
                    text = "저장하기",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun Header(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface)
            .padding(top = 14.dp, bottom = 12.dp, start = 8.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Outlined.ArrowBackIosNew,
                contentDescription = "뒤로가기"
            )
        }
        Text(
            text = "알림 수신 시간 설정",
            style = MaterialTheme.typography.titleMedium
        )
    }
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
