package com.example.gifticonalarm.ui.feature.shared.cashusage
import com.example.gifticonalarm.ui.feature.shared.text.VoucherText
import com.example.gifticonalarm.ui.feature.shared.text.CommonText
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gifticonalarm.ui.feature.add.bottomsheet.ExpirationDateSelectionBottomSheet
import com.example.gifticonalarm.ui.feature.shared.components.BackNavigationIcon
import com.example.gifticonalarm.ui.feature.shared.util.formatAmountWithComma
import com.example.gifticonalarm.ui.feature.shared.util.parseUsedAtTextOrToday
import com.example.gifticonalarm.ui.feature.shared.util.toUsedAtText
import com.example.gifticonalarm.ui.theme.GifticonTextPrimary

private val Primary = Color(0xFF191970)
private val Background = Color.White
private val Surface = Color.White
private val FieldLine = Color(0xFFE2E8F0)
private val FieldLabel = Color(0xFF334155)
private val FieldText = GifticonTextPrimary
private val FieldHint = Color(0xFF94A3B8)
private val BalanceBorder = Color(0xFFE2E8F0)
private val InputSectionBackground = Color(0xFFFFFFFF)
private val BalanceCardBackground = Color(0xFFF6F6F8)
private val FieldIconSize = 18.dp

/**
 * 금액권 사용 내역 추가 화면.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashUsageAddScreen(
    uiState: CashUsageAddUiState,
    onBackClick: () -> Unit,
    onAmountChange: (String) -> Unit,
    onStoreChange: (String) -> Unit,
    onUsedAtChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    var isUsedDateBottomSheetVisible by remember { mutableStateOf(false) }
    val draftUsedDateState = remember { mutableStateOf(parseUsedAtTextOrToday(uiState.usedAtText)) }

    LaunchedEffect(uiState.usedAtText, isUsedDateBottomSheetVisible) {
        if (!isUsedDateBottomSheetVisible) {
            draftUsedDateState.value = parseUsedAtTextOrToday(uiState.usedAtText)
        }
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface),
                title = {
                    Text(
                        text = VoucherText.ACTION_ADD_USAGE,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = FieldText,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                },
                navigationIcon = {
                    BackNavigationIcon(onClick = onBackClick, tint = FieldText)
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Surface)
                    .padding(horizontal = 24.dp, vertical = 18.dp)
            ) {
                Button(
                    onClick = onSaveClick,
                    enabled = !uiState.isSaving,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text(
                        text = VoucherText.ACTION_RECORD_USAGE,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BalanceCard(currentBalance = uiState.currentBalance)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = InputSectionBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Label(VoucherText.LABEL_USAGE_AMOUNT)
                        MoneyUnderlineField(
                            value = uiState.amountText,
                            onValueChange = onAmountChange
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Label(VoucherText.LABEL_USAGE_STORE)
                        UnderlineField(
                            value = uiState.storeText,
                            onValueChange = onStoreChange,
                            placeholder = VoucherText.PLACEHOLDER_USAGE_STORE,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Storefront,
                                    contentDescription = null,
                                    tint = FieldHint,
                                    modifier = Modifier.size(FieldIconSize)
                                )
                            }
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Label(VoucherText.LABEL_USAGE_DATE)
                        UnderlineField(
                            value = uiState.usedAtText,
                            onValueChange = {},
                            placeholder = VoucherText.PLACEHOLDER_DATE_DASH,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.CalendarToday,
                                    contentDescription = VoucherText.DESCRIPTION_SELECT_DATE,
                                    tint = FieldHint,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable {
                                            draftUsedDateState.value = parseUsedAtTextOrToday(uiState.usedAtText)
                                            isUsedDateBottomSheetVisible = true
                                        }
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    if (isUsedDateBottomSheetVisible) {
        ExpirationDateSelectionBottomSheet(
            selectedDate = draftUsedDateState.value,
            onDateSelected = { draftUsedDateState.value = it },
            onConfirmClick = {
                onUsedAtChange(draftUsedDateState.value.toUsedAtText())
                isUsedDateBottomSheetVisible = false
            },
            onDismissRequest = { isUsedDateBottomSheetVisible = false },
            titleText = VoucherText.TITLE_USAGE_DATE_PICKER,
            confirmButtonText = { it.toUsedAtText() }
        )
    }
}

@Composable
private fun BalanceCard(currentBalance: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = BalanceBorder,
                shape = RoundedCornerShape(18.dp)
            )
            .background(color = BalanceCardBackground, shape = RoundedCornerShape(18.dp))
            .padding(vertical = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = VoucherText.LABEL_CURRENT_BALANCE,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF64748B)
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = formatAmountWithComma(currentBalance),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = FieldText
                )
                Text(
                    text = CommonText.UNIT_WON,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF64748B),
                    modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun Label(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = FieldLabel,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun MoneyUnderlineField(
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            textStyle = TextStyle(
                color = FieldText,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            ),
            decorationBox = { innerTextField ->
                if (value.isBlank()) {
                    Text(
                        text = CommonText.PLACEHOLDER_AMOUNT_ZERO,
                        color = FieldHint,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                innerTextField()
            }
        )
        Text(
            text = CommonText.UNIT_WON,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF64748B)
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(FieldLine)
    )
}

@Composable
private fun UnderlineField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingIcon()
        Spacer(modifier = Modifier.width(10.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            textStyle = TextStyle(
                color = FieldText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            ),
            decorationBox = { innerTextField ->
                if (value.isBlank()) {
                    Text(
                        text = placeholder,
                        color = FieldHint,
                        fontSize = 11.sp
                    )
                }
                innerTextField()
            }
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(FieldLine)
    )
}
