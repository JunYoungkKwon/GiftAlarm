package com.example.gifticonalarm.ui.feature.add.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged

private val BottomSheetBackground = Color(0xFFFFFFFF)
private val BottomSheetAccent = Color(0xFF191970)
private val BottomSheetActiveText = Color(0xFF111827)
private val BottomSheetInactiveText = Color(0xFFCBD5E1)
private val BottomSheetHighlight = Color(0xFFF3F4F6)
private val BottomSheetBorder = Color(0xFFE5E7EB)
private const val YearRangeSize = 30
private const val WheelVisibleCount = 5

/**
 * 바텀시트에서 사용하는 만료일 모델.
 */
data class ExpirationDate(
    val year: Int,
    val month: Int,
    val day: Int
) {
    fun toDisplayText(): String = "%04d.%02d.%02d".format(year, month, day)

    fun toConfirmText(): String = "${toDisplayText()} 까지"

    fun withYear(newYear: Int): ExpirationDate =
        ExpirationDate(newYear, month, day.coerceAtMost(maxDayOfMonth(newYear, month)))

    fun withMonth(newMonth: Int): ExpirationDate {
        val safeMonth = newMonth.coerceIn(1, 12)
        return ExpirationDate(year, safeMonth, day.coerceAtMost(maxDayOfMonth(year, safeMonth)))
    }

    fun withDay(newDay: Int): ExpirationDate =
        ExpirationDate(year, month, newDay.coerceIn(1, maxDayOfMonth(year, month)))

    companion object {
        fun today(): ExpirationDate {
            val calendar = java.util.Calendar.getInstance()
            return ExpirationDate(
                year = calendar.get(java.util.Calendar.YEAR),
                month = calendar.get(java.util.Calendar.MONTH) + 1,
                day = calendar.get(java.util.Calendar.DAY_OF_MONTH)
            )
        }

        fun maxDayOfMonth(year: Int, month: Int): Int {
            val calendar = java.util.Calendar.getInstance()
            calendar.set(java.util.Calendar.YEAR, year)
            calendar.set(java.util.Calendar.MONTH, month - 1)
            return calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH)
        }
    }
}

/**
 * 유효기간 선택 바텀시트.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpirationDateSelectionBottomSheet(
    selectedDate: ExpirationDate,
    onDateSelected: (ExpirationDate) -> Unit,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    titleText: String = "유효기간 설정",
    confirmButtonText: (ExpirationDate) -> String = { it.toConfirmText() }
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = BottomSheetBackground,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 8.dp)
                    .width(40.dp)
                    .height(6.dp)
                    .background(Color(0xFFD1D5DB), RoundedCornerShape(999.dp))
            )
        }
    ) {
        ExpirationDateSelectionContent(
            selectedDate = selectedDate,
            onDateSelected = onDateSelected,
            onConfirmClick = onConfirmClick,
            titleText = titleText,
            confirmButtonText = confirmButtonText,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ExpirationDateSelectionContent(
    selectedDate: ExpirationDate,
    onDateSelected: (ExpirationDate) -> Unit,
    onConfirmClick: () -> Unit,
    titleText: String,
    confirmButtonText: (ExpirationDate) -> String,
    modifier: Modifier = Modifier
) {
    val currentYear = ExpirationDate.today().year
    val minYear = currentYear
    val maxYear = currentYear + YearRangeSize
    val clampedYear = selectedDate.year.coerceIn(minYear, maxYear)
    val normalizedDate = selectedDate.withYear(clampedYear)

    LaunchedEffect(selectedDate, normalizedDate) {
        if (selectedDate != normalizedDate) {
            onDateSelected(normalizedDate)
        }
    }

    val yearOptions = (minYear..maxYear).toList()
    val monthOptions = (1..12).toList()
    val dayOptions = (1..ExpirationDate.maxDayOfMonth(normalizedDate.year, normalizedDate.month)).toList()

    Column(
        modifier = modifier
            .background(BottomSheetBackground)
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
    ) {
        Text(
            text = titleText,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = BottomSheetActiveText,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp)
                .border(
                    width = 1.dp,
                    color = BottomSheetBorder,
                    shape = RoundedCornerShape(16.dp)
                )
                .background(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 12.dp, vertical = 14.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(BottomSheetHighlight, RoundedCornerShape(10.dp))
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DateWheelColumn(
                    values = yearOptions,
                    selectedValue = normalizedDate.year,
                    itemLabel = { "${it}년" },
                    onValueChange = { onDateSelected(normalizedDate.withYear(it)) },
                    modifier = Modifier.weight(1f)
                )
                DateWheelColumn(
                    values = monthOptions,
                    selectedValue = normalizedDate.month,
                    itemLabel = { "${it}월" },
                    onValueChange = { onDateSelected(normalizedDate.withMonth(it)) },
                    modifier = Modifier.weight(1f)
                )
                DateWheelColumn(
                    values = dayOptions,
                    selectedValue = normalizedDate.day.coerceAtMost(dayOptions.last()),
                    itemLabel = { "${it}일" },
                    onValueChange = { onDateSelected(normalizedDate.withDay(it)) },
                    modifier = Modifier.weight(1f)
                )
            }
        }



        Button(
            onClick = onConfirmClick,
            modifier = Modifier
                .padding(top = 14.dp)
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BottomSheetAccent,
                contentColor = Color.White
            )
        ) {
            Text(
                text = confirmButtonText(normalizedDate),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DateWheelColumn(
    values: List<Int>,
    selectedValue: Int,
    itemLabel: (Int) -> String,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (values.isEmpty()) return

    val selectedIndex = values.indexOf(selectedValue).let { if (it >= 0) it else 0 }
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex)

    LaunchedEffect(selectedValue, values) {
        val targetIndex = values.indexOf(selectedValue).let { if (it >= 0) it else 0 }
        if (!listState.isScrollInProgress && listState.firstVisibleItemIndex != targetIndex) {
            listState.scrollToItem(targetIndex)
        }
    }

    LaunchedEffect(values, listState, selectedValue) {
        snapshotFlow {
            Triple(
                listState.firstVisibleItemIndex,
                listState.firstVisibleItemScrollOffset,
                listState.isScrollInProgress
            )
        }
            .distinctUntilChanged()
            .collect { (index, _, isScrolling) ->
                val clampedIndex = index.coerceIn(0, values.lastIndex)
                val value = values[clampedIndex]
                if (value != selectedValue) {
                    onValueChange(value)
                }
                if (!isScrolling && listState.firstVisibleItemIndex != clampedIndex) {
                    listState.animateScrollToItem(clampedIndex)
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.height((48 * WheelVisibleCount).dp),
        userScrollEnabled = true
    ) {
        items(2) {
            Box(modifier = Modifier.height(48.dp))
        }
        items(values.size) { index ->
            val value = values[index]
            val isSelected = value == selectedValue
            Text(
                text = itemLabel(value),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable { onValueChange(value) }
                    .padding(vertical = 12.dp),
                color = if (isSelected) BottomSheetActiveText else BottomSheetInactiveText,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        }
        items(2) {
            Box(modifier = Modifier.height(48.dp))
        }
    }
}
