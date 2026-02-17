package com.example.gifticonalarm.ui.coupon.registration

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gifticonalarm.ui.theme.GifticonAlarmTheme
import coil3.compose.AsyncImage

private val RegistrationBackground = Color(0xFFFFFFFF)
private val RegistrationAccent = Color(0xFF191970)
private val RegistrationTextPrimary = Color(0xFF111827)
private val RegistrationTextSecondary = Color(0xFF9CA3AF)
private val RegistrationDivider = Color(0xFFF1F5F9)
private val RegistrationSurface = Color(0xFFF3F4F6)

/**
 * 쿠폰 등록 진입 및 수동 입력 UI를 제공하는 화면.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponRegistrationScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onThumbnailClick: () -> Unit = {},
    onThumbnailSelected: (Uri) -> Unit = {},
    onNoBarcodeInfoClick: () -> Unit = {},
    onExpiryDateClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    var barcode by rememberSaveable { mutableStateOf("") }
    var place by rememberSaveable { mutableStateOf("") }
    var couponName by rememberSaveable { mutableStateOf("") }
    var expiryDate by rememberSaveable { mutableStateOf("") }
    var withoutBarcode by rememberSaveable { mutableStateOf(false) }
    var thumbnailUri by rememberSaveable { mutableStateOf<String?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            thumbnailUri = uri.toString()
            onThumbnailSelected(uri)
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = RegistrationBackground,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RegistrationBackground
                ),
                title = {
                    Text(
                        text = "등록하기",
                        modifier = Modifier.fillMaxWidth(),
                        color = RegistrationTextPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCloseClick) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "닫기",
                            tint = RegistrationTextPrimary
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.size(48.dp))
                }
            )
        },
        bottomBar = {
            Surface(
                color = RegistrationBackground,
                tonalElevation = 0.dp,
                border = BorderStroke(1.dp, RegistrationDivider)
            ) {
                Button(
                    onClick = onRegisterClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RegistrationAccent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "등록하기",
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
                .background(RegistrationBackground)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            ThumbnailSection(
                thumbnailUri = thumbnailUri,
                onThumbnailClick = {
                    onThumbnailClick()
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            )
            Spacer(modifier = Modifier.height(26.dp))
            Text(
                text = "바코드 번호",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = RegistrationTextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            UnderlineInputField(
                value = barcode,
                onValueChange = { barcode = it },
                placeholder = "바코드 번호를 입력해주세요",
                keyboardType = KeyboardType.Ascii
            )
            Spacer(modifier = Modifier.height(16.dp))
            BarcodePreviewCard(barcode = barcode)
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = withoutBarcode,
                    onCheckedChange = { withoutBarcode = it }
                )
                Text(
                    text = "바코드 번호 없이 등록하기",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280)
                )
                IconButton(
                    onClick = onNoBarcodeInfoClick,
                    modifier = Modifier.size(22.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "바코드 도움말",
                        tint = Color(0xFFCBD5E1),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "쿠폰 정보",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = RegistrationTextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            UnderlineInputField(
                value = place,
                onValueChange = { place = it },
                placeholder = "사용처를 입력해 주세요."
            )
            Spacer(modifier = Modifier.height(8.dp))
            UnderlineInputField(
                value = couponName,
                onValueChange = { couponName = it },
                placeholder = "쿠폰명을 입력해 주세요. (필수)"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onExpiryDateClick)
                    .padding(top = 6.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (expiryDate.isEmpty()) "유효기한을 선택해주세요." else expiryDate,
                    color = RegistrationTextSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Outlined.ExpandMore,
                    contentDescription = "유효기한 선택",
                    tint = Color(0xFF94A3B8)
                )
            }
            HorizontalDivider(color = RegistrationDivider, thickness = 1.dp)

            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = Color(0xFFCBD5E1),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "만료일 알람은 언제 오나요?",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF9CA3AF)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
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
                    color = Color(0xFFE2E8F0),
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
                        contentDescription = "썸네일 추가",
                        tint = Color(0xFF94A3B8)
                    )
                    Text(
                        text = "썸네일 추가",
                        color = Color(0xFF94A3B8),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            } else {
                AsyncImage(
                    model = thumbnailUri,
                    contentDescription = "선택한 썸네일",
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
                tint = Color(0xFFF97316),
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "썸네일 이미지는 자동인식되지 않아요.",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFCBD5E1)
            )
        }
    }
}

@Composable
private fun UnderlineInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                color = RegistrationTextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = RegistrationTextPrimary,
            fontWeight = FontWeight.Medium
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = RegistrationAccent
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
    HorizontalDivider(color = RegistrationDivider, thickness = 1.dp)
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
            Row(
                modifier = Modifier.height(26.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                val pattern = listOf(2, 1, 3, 1, 4, 2, 1, 3, 2, 1, 4, 2, 1, 3, 2, 4, 1, 2, 3, 1, 4)
                pattern.forEach { width ->
                    Box(
                        modifier = Modifier
                            .width(width.dp)
                            .height(24.dp)
                            .background(Color.Black)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = barcode.ifBlank { "1i1i w2i2 1111 11" },
                color = Color(0xFF6B7280),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun CouponRegistrationScreenPreview() {
    GifticonAlarmTheme {
        CouponRegistrationScreen()
    }
}
