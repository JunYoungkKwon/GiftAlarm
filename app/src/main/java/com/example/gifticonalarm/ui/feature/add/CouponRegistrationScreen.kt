package com.example.gifticonalarm.ui.feature.add
import com.example.gifticonalarm.ui.feature.shared.text.CommonText
import com.example.gifticonalarm.ui.feature.shared.text.RegistrationText

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gifticonalarm.ui.feature.add.bottomsheet.CouponRegistrationInfoBottomSheet
import com.example.gifticonalarm.ui.feature.add.bottomsheet.CouponRegistrationInfoSheetType
import com.example.gifticonalarm.ui.feature.add.bottomsheet.ExpirationDate
import com.example.gifticonalarm.ui.feature.add.bottomsheet.ExpirationDateSelectionBottomSheet
import com.example.gifticonalarm.ui.feature.shared.components.BottomToastBanner
import com.example.gifticonalarm.ui.feature.shared.effect.AutoDismissToast
import com.example.gifticonalarm.ui.feature.shared.effect.HandleRouteEffect
import com.example.gifticonalarm.ui.feature.shared.util.encodeCode128ModulesOrNull
import com.example.gifticonalarm.ui.feature.shared.util.formatBarcodeNumberForDisplay
import coil3.compose.AsyncImage

private val RegistrationBackground = Color(0xFFFFFFFF)
private val RegistrationAccent = Color(0xFF191970)
private val RegistrationTextPrimary = Color(0xFF111827)
private val RegistrationTextSecondary = Color(0xFF9CA3AF)
private val RegistrationDivider = Color(0xFFF1F5F9)
private val RegistrationSurface = Color(0xFFF3F4F6)
private val RegistrationHintTint = Color(0xFF9CA3AF)
private val RegistrationInfoIconTint = Color(0xFFCBD5E1)
private val RegistrationThumbnailHintTint = Color(0xFF94A3B8)
private val RegistrationThumbnailBorder = Color(0xFFE2E8F0)
private val RegistrationThumbnailWarningTint = Color(0xFFF97316)
private val RegistrationDisabledContainer = Color(0xFFF3F4F6)
private val RegistrationDisabledText = Color(0xFF9CA3AF)
private val RegistrationPageHorizontalPadding = 20.dp
private val RegistrationSectionSpacing = 20.dp
private val RegistrationFieldSpacing = 6.dp
private val RegistrationAlignedStartPadding = 4.dp
private val RegistrationSectionTitleSize = 16.sp

enum class CouponType {
    EXCHANGE,
    AMOUNT
}

private data class BarcodeSectionState(
    val barcode: String,
    val withoutBarcode: Boolean
)

private data class CouponInfoSectionState(
    val place: String,
    val couponName: String,
    val expiryDate: String,
    val memo: String
)

private data class CouponTypeSectionState(
    val couponType: CouponType,
    val amount: String
)

private fun registrationActionText(isEditMode: Boolean): String {
    return if (isEditMode) RegistrationText.TITLE_EDIT else RegistrationText.TITLE_CREATE
}

/**
 * 쿠폰 등록 진입 및 수동 입력 UI를 제공하는 화면.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponRegistrationScreen(
    modifier: Modifier = Modifier,
    couponId: String? = null,
    onCloseClick: () -> Unit = {},
    onThumbnailClick: () -> Unit = {},
    onThumbnailSelected: (Uri) -> Unit = {},
    onNoBarcodeInfoClick: () -> Unit = {},
    onExpiryDateClick: () -> Unit = {},
    onRegisterClick: (Boolean) -> Unit = {}
) {
    val registrationViewModel: CouponRegistrationViewModel = hiltViewModel()
    val editTarget by registrationViewModel.getGifticonForEdit(couponId).observeAsState()
    var toastMessage by remember { mutableStateOf<String?>(null) }
    val formState by registrationViewModel.formState.observeAsState(
        CouponRegistrationFormState(
            couponId = couponId,
            isEditMode = couponId != null
        )
    )
    val infoSheetType by registrationViewModel.infoSheetType.observeAsState(CouponRegistrationInfoSheetType.NONE)
    val isExpiryBottomSheetVisible by registrationViewModel.isExpiryBottomSheetVisible.observeAsState(false)
    val selectedExpiryDate by registrationViewModel.selectedExpiryDate.observeAsState()
    val draftExpiryDate by registrationViewModel.draftExpiryDate.observeAsState(ExpirationDate.today())
    val isRegistering by registrationViewModel.isLoading.observeAsState(false)
    val effect by registrationViewModel.effect.observeAsState()
    val expiryDate = selectedExpiryDate?.toDisplayText().orEmpty()

    LaunchedEffect(couponId, editTarget?.id) {
        registrationViewModel.initializeForm(couponId, editTarget)
    }

    HandleRouteEffect(
        effect = effect,
        onConsumed = registrationViewModel::consumeEffect
    ) { currentEffect ->
        when (currentEffect) {
            is CouponRegistrationEffect.ShowMessage -> {
                toastMessage = currentEffect.message
            }
            CouponRegistrationEffect.RegistrationCompleted -> {
                onRegisterClick(formState.isEditMode)
            }
        }
    }

    AutoDismissToast(message = toastMessage, onDismiss = { toastMessage = null })

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            registrationViewModel.updateThumbnailUri(uri.toString())
            onThumbnailSelected(uri)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = RegistrationBackground,
            topBar = {
                RegistrationTopBar(
                    isEditMode = formState.isEditMode,
                    onCloseClick = onCloseClick
                )
            },
            bottomBar = {
                RegistrationBottomBar(
                    isEditMode = formState.isEditMode,
                    isRegistering = isRegistering,
                    onSubmitClick = registrationViewModel::submit
                )
            }
        ) { innerPadding ->
            RegistrationFormContent(
                formState = formState,
                expiryDate = expiryDate,
                innerPadding = innerPadding,
                onThumbnailClick = {
                    onThumbnailClick()
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onBarcodeChange = registrationViewModel::updateBarcode,
                onWithoutBarcodeChange = registrationViewModel::updateWithoutBarcode,
                onBarcodeInfoClick = {
                    onNoBarcodeInfoClick()
                    registrationViewModel.showBarcodeInfoSheet()
                },
                onPlaceChange = registrationViewModel::updatePlace,
                onCouponNameChange = registrationViewModel::updateCouponName,
                onMemoChange = registrationViewModel::updateMemo,
                onExpiryDateClick = {
                    onExpiryDateClick()
                    registrationViewModel.openExpiryBottomSheet()
                },
                onCouponTypeChange = registrationViewModel::updateCouponType,
                onAmountChange = registrationViewModel::updateAmount,
                onNotificationInfoClick = registrationViewModel::showNotificationInfoSheet
            )
        }

        BottomToastBanner(message = toastMessage)
    }

    if (isExpiryBottomSheetVisible) {
        ExpirationDateSelectionBottomSheet(
            selectedDate = draftExpiryDate,
            onDateSelected = registrationViewModel::updateDraftExpiryDate,
            onConfirmClick = registrationViewModel::confirmExpiryDate,
            onDismissRequest = registrationViewModel::dismissExpiryBottomSheet
        )
    }

    if (infoSheetType != CouponRegistrationInfoSheetType.NONE) {
        CouponRegistrationInfoBottomSheet(
            type = infoSheetType,
            onDismissRequest = registrationViewModel::dismissInfoSheet
        )
    }
}

@Composable
private fun RegistrationFormContent(
    formState: CouponRegistrationFormState,
    expiryDate: String,
    innerPadding: androidx.compose.foundation.layout.PaddingValues,
    onThumbnailClick: () -> Unit,
    onBarcodeChange: (String) -> Unit,
    onWithoutBarcodeChange: (Boolean) -> Unit,
    onBarcodeInfoClick: () -> Unit,
    onPlaceChange: (String) -> Unit,
    onCouponNameChange: (String) -> Unit,
    onMemoChange: (String) -> Unit,
    onExpiryDateClick: () -> Unit,
    onCouponTypeChange: (CouponType) -> Unit,
    onAmountChange: (String) -> Unit,
    onNotificationInfoClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RegistrationBackground)
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
            .padding(horizontal = RegistrationPageHorizontalPadding, vertical = 16.dp)
    ) {
        ThumbnailSection(
            thumbnailUri = formState.thumbnailUri,
            onThumbnailClick = onThumbnailClick
        )
        Spacer(modifier = Modifier.height(24.dp))
        BarcodeSection(
            state = BarcodeSectionState(
                barcode = formState.barcode,
                withoutBarcode = formState.withoutBarcode
            ),
            onBarcodeChange = onBarcodeChange,
            onWithoutBarcodeChange = onWithoutBarcodeChange,
            onBarcodeInfoClick = onBarcodeInfoClick
        )

        CouponInfoSection(
            state = CouponInfoSectionState(
                place = formState.place,
                couponName = formState.couponName,
                expiryDate = expiryDate,
                memo = formState.memo
            ),
            onPlaceChange = onPlaceChange,
            onCouponNameChange = onCouponNameChange,
            onMemoChange = onMemoChange,
            onExpiryDateClick = onExpiryDateClick
        )

        CouponTypeSection(
            state = CouponTypeSectionState(
                couponType = formState.couponType,
                amount = formState.amount
            ),
            onCouponTypeChange = onCouponTypeChange,
            onAmountChange = onAmountChange,
            onNotificationInfoClick = onNotificationInfoClick
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegistrationTopBar(
    isEditMode: Boolean,
    onCloseClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = RegistrationBackground
        ),
        title = {
            Text(
                text = registrationActionText(isEditMode),
                modifier = Modifier.fillMaxWidth(),
                color = RegistrationTextPrimary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = RegistrationText.ACTION_CLOSE_DESCRIPTION,
                    tint = RegistrationTextPrimary
                )
            }
        },
        actions = {
            Spacer(modifier = Modifier.size(48.dp))
        }
    )
}

@Composable
private fun RegistrationBottomBar(
    isEditMode: Boolean,
    isRegistering: Boolean,
    onSubmitClick: () -> Unit
) {
    Surface(
        color = RegistrationBackground,
        tonalElevation = 0.dp,
        border = BorderStroke(1.dp, RegistrationDivider)
    ) {
        Button(
            onClick = onSubmitClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .height(52.dp),
            enabled = !isRegistering,
            colors = ButtonDefaults.buttonColors(
                containerColor = RegistrationAccent,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = registrationActionText(isEditMode),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun RegistrationSectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    alignStart: Boolean = true
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium.copy(fontSize = RegistrationSectionTitleSize),
        fontWeight = FontWeight.Bold,
        color = RegistrationTextPrimary,
        modifier = if (alignStart) {
            modifier.padding(start = RegistrationAlignedStartPadding)
        } else {
            modifier
        }
    )
}

@Composable
private fun BarcodeSection(
    state: BarcodeSectionState,
    onBarcodeChange: (String) -> Unit,
    onWithoutBarcodeChange: (Boolean) -> Unit,
    onBarcodeInfoClick: () -> Unit
) {
    RegistrationSectionTitle(text = RegistrationText.LABEL_BARCODE)
    Spacer(modifier = Modifier.height(RegistrationFieldSpacing))
    UnderlineInputField(
        value = if (state.withoutBarcode) RegistrationText.LABEL_NO_BARCODE else state.barcode,
        onValueChange = onBarcodeChange,
        placeholder = RegistrationText.PLACEHOLDER_BARCODE,
        keyboardType = KeyboardType.Ascii,
        enabled = !state.withoutBarcode,
        disabledContainerColor = RegistrationDisabledContainer,
        disabledTextColor = RegistrationDisabledText
    )
    Spacer(modifier = Modifier.height(16.dp))
    BarcodePreviewCard(barcode = state.barcode)
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        Checkbox(
            checked = state.withoutBarcode,
            onCheckedChange = onWithoutBarcodeChange,
            modifier = Modifier
                .size(20.dp)
                .scale(0.65f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = RegistrationText.ACTION_REGISTER_WITHOUT_BARCODE,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = RegistrationHintTint,
            modifier = Modifier.padding(top = 1.dp)
        )
        Spacer(modifier = Modifier.width(2.dp))
        IconButton(
            onClick = onBarcodeInfoClick,
            modifier = Modifier.size(20.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = RegistrationText.ACTION_BARCODE_HELP,
                tint = RegistrationInfoIconTint,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun CouponInfoSection(
    state: CouponInfoSectionState,
    onPlaceChange: (String) -> Unit,
    onCouponNameChange: (String) -> Unit,
    onMemoChange: (String) -> Unit,
    onExpiryDateClick: () -> Unit
) {
    Spacer(modifier = Modifier.height(RegistrationSectionSpacing))
    RegistrationSectionTitle(text = RegistrationText.SECTION_COUPON_INFO)
    Spacer(modifier = Modifier.height(RegistrationFieldSpacing))
    UnderlineInputField(
        value = state.place,
        onValueChange = onPlaceChange,
        placeholder = CommonText.PLACEHOLDER_STORE_REQUIRED
    )
    Spacer(modifier = Modifier.height(RegistrationFieldSpacing))
    UnderlineInputField(
        value = state.couponName,
        onValueChange = onCouponNameChange,
        placeholder = CommonText.PLACEHOLDER_COUPON_NAME_REQUIRED
    )
    Spacer(modifier = Modifier.height(RegistrationFieldSpacing))
    UnderlineInputField(
        value = state.expiryDate,
        onValueChange = {},
        placeholder = CommonText.PLACEHOLDER_EXPIRY_REQUIRED,
        readOnly = true,
        onClick = onExpiryDateClick,
        showExpandIcon = true
    )

    Spacer(modifier = Modifier.height(RegistrationSectionSpacing))
    RegistrationSectionTitle(text = RegistrationText.SECTION_MEMO)
    Spacer(modifier = Modifier.height(RegistrationFieldSpacing))
    UnderlineInputField(
        value = state.memo,
        onValueChange = onMemoChange,
        placeholder = CommonText.PLACEHOLDER_MEMO
    )
}

@Composable
private fun CouponTypeSection(
    state: CouponTypeSectionState,
    onCouponTypeChange: (CouponType) -> Unit,
    onAmountChange: (String) -> Unit,
    onNotificationInfoClick: () -> Unit
) {
    Spacer(modifier = Modifier.height(RegistrationSectionSpacing))
    RegistrationSectionTitle(text = RegistrationText.SECTION_COUPON_TYPE)
    Spacer(modifier = Modifier.height(RegistrationFieldSpacing))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = state.couponType == CouponType.EXCHANGE,
            onClick = { onCouponTypeChange(CouponType.EXCHANGE) },
            label = { Text(RegistrationText.TYPE_EXCHANGE) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = RegistrationAccent,
                selectedLabelColor = Color.White
            ),
            modifier = Modifier.weight(1f)
        )
        FilterChip(
            selected = state.couponType == CouponType.AMOUNT,
            onClick = { onCouponTypeChange(CouponType.AMOUNT) },
            label = { Text(RegistrationText.TYPE_AMOUNT) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = RegistrationAccent,
                selectedLabelColor = Color.White
            ),
            modifier = Modifier.weight(1f)
        )
    }

    if (state.couponType == CouponType.AMOUNT) {
        Spacer(modifier = Modifier.height(14.dp))
        RegistrationSectionTitle(
            text = RegistrationText.LABEL_AMOUNT_INPUT,
            alignStart = false
        )
        Spacer(modifier = Modifier.height(RegistrationFieldSpacing))
        UnderlineInputField(
            value = state.amount,
            onValueChange = onAmountChange,
            placeholder = RegistrationText.PLACEHOLDER_AMOUNT,
            keyboardType = KeyboardType.Number,
            suffixText = CommonText.UNIT_WON
        )
    }

    Spacer(modifier = Modifier.height(14.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = RegistrationText.ACTION_NOTIFICATION_INFO_DESCRIPTION,
            tint = RegistrationInfoIconTint,
            modifier = Modifier
                .size(16.dp)
                .clickable { onNotificationInfoClick() }
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = RegistrationText.ACTION_NOTIFICATION_INFO,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = RegistrationHintTint
        )
    }
}

@Composable
private fun ThumbnailSection(
    thumbnailUri: String?,
    onThumbnailClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(width = 112.dp, height = 112.dp)
                .border(
                    width = 1.dp,
                    color = RegistrationThumbnailBorder,
                    shape = RoundedCornerShape(14.dp)
                )
                .clickable(onClick = onThumbnailClick),
            contentAlignment = Alignment.Center
        ) {
            if (thumbnailUri == null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = RegistrationText.ACTION_ADD_THUMBNAIL_DESCRIPTION,
                        tint = RegistrationThumbnailHintTint
                    )
                    Text(
                        text = RegistrationText.ACTION_ADD_THUMBNAIL,
                        color = RegistrationThumbnailHintTint,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            } else {
                AsyncImage(
                    model = thumbnailUri,
                    contentDescription = RegistrationText.IMAGE_SELECTED_THUMBNAIL_DESCRIPTION,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(14.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.WarningAmber,
                contentDescription = null,
                tint = RegistrationThumbnailWarningTint,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = RegistrationText.MESSAGE_THUMBNAIL_NOT_RECOGNIZED,
                style = MaterialTheme.typography.labelSmall,
                color = RegistrationInfoIconTint
            )
        }
    }
}

@Composable
private fun UnderlineInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    onClick: (() -> Unit)? = null,
    showExpandIcon: Boolean = false,
    suffixText: String? = null,
    disabledContainerColor: Color = Color.Transparent,
    disabledTextColor: Color = RegistrationTextSecondary
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = RegistrationAlignedStartPadding)
            .heightIn(min = 20.dp)
            .let { baseModifier ->
                if (readOnly && onClick != null) {
                    baseModifier.clickable(onClick = onClick)
                } else {
                    baseModifier
                }
            },
        placeholder = {
            Text(
                text = placeholder,
                color = RegistrationTextSecondary,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
            )
        },
        singleLine = true,
        enabled = enabled,
        readOnly = readOnly,
        trailingIcon = {
            if (showExpandIcon) {
                IconButton(
                    onClick = { onClick?.invoke() },
                    enabled = onClick != null,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ExpandMore,
                        contentDescription = RegistrationText.ACTION_SELECT_EXPIRY_DESCRIPTION,
                        tint = RegistrationThumbnailHintTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        },
        suffix = {
            if (suffixText != null) {
                Text(
                    text = suffixText,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                    color = RegistrationTextSecondary
                )
            }
        },
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = RegistrationTextPrimary,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = disabledContainerColor,
            disabledTextColor = disabledTextColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = RegistrationAccent
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
    HorizontalDivider(
        modifier = Modifier.padding(start = RegistrationAlignedStartPadding),
        color = RegistrationDivider,
        thickness = 1.dp
    )
}

@Composable
private fun BarcodePreviewCard(barcode: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(RegistrationSurface, RoundedCornerShape(14.dp))
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(2.dp))
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .drawBehind {
                        val modules = encodeCode128ModulesOrNull(barcode, width = 600)
                        if (modules != null) {
                            drawRegistrationCode128Bars(modules)
                        } else {
                            drawRegistrationFallbackBars()
                        }
                    }
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatBarcodeNumberForDisplay(barcode),
                color = Color(0xFF6B7280),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawRegistrationCode128Bars(modules: BooleanArray) {
    if (modules.isEmpty()) {
        drawRegistrationFallbackBars()
        return
    }

    val horizontalPadding = 8.dp.toPx()
    val topPadding = 3.dp.toPx()
    val height = size.height - (topPadding * 2f)
    val availableWidth = size.width - (horizontalPadding * 2f)
    if (availableWidth <= 0f || height <= 0f) return

    val targetWidth = availableWidth * 0.66f
    val startX = horizontalPadding + ((availableWidth - targetWidth) / 2f)
    val moduleWidth = (targetWidth / modules.size.toFloat()).coerceAtMost(1.35.dp.toPx())
    modules.forEachIndexed { index, isBlack ->
        if (!isBlack) return@forEachIndexed
        drawRect(
            color = Color.Black,
            topLeft = Offset(startX + (index * moduleWidth), topPadding),
            size = Size(moduleWidth, height)
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawRegistrationFallbackBars() {
    val widths = listOf(1.dp, 2.dp, 1.dp, 1.dp, 2.dp, 1.dp, 1.dp, 1.dp, 2.dp, 1.dp, 2.dp, 1.dp, 2.dp, 1.dp, 2.dp, 1.dp)
    val horizontalPadding = 8.dp.toPx()
    val topPadding = 3.dp.toPx()
    val barHeight = size.height - (topPadding * 2f)
    val gap = 1.dp.toPx()
    val availableWidth = size.width - (horizontalPadding * 2f)
    if (availableWidth <= 0f || barHeight <= 0f) return

    val totalPatternWidth = widths.sumOf { it.toPx().toDouble() }.toFloat() + gap * (widths.size - 1)
    if (totalPatternWidth <= 0f) return

    val targetWidth = availableWidth * 0.54f
    val scale = targetWidth / totalPatternWidth
    var x = horizontalPadding + ((availableWidth - targetWidth) / 2f)
    widths.forEach { widthDp ->
        val barWidth = widthDp.toPx() * scale
        drawRect(
            color = Color.Black,
            topLeft = Offset(x, topPadding),
            size = Size(barWidth, barHeight)
        )
        x += barWidth + (gap * scale)
    }
}
