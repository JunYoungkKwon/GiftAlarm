package com.example.gifticonalarm.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import com.example.gifticonalarm.R

val PretendardFontFamily = FontFamily(
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),
    Font(R.font.pretendard_bold, FontWeight.Bold)
)

private val DefaultTypography = Typography()

private fun TextStyle.withPretendard(): TextStyle = copy(fontFamily = PretendardFontFamily)

val Typography = Typography(
    displayLarge = DefaultTypography.displayLarge.withPretendard(),
    displayMedium = DefaultTypography.displayMedium.withPretendard(),
    displaySmall = DefaultTypography.displaySmall.withPretendard(),
    headlineLarge = DefaultTypography.headlineLarge.withPretendard(),
    headlineMedium = DefaultTypography.headlineMedium.withPretendard(),
    headlineSmall = DefaultTypography.headlineSmall.withPretendard(),
    titleLarge = DefaultTypography.titleLarge.withPretendard(),
    titleMedium = DefaultTypography.titleMedium.withPretendard(),
    titleSmall = DefaultTypography.titleSmall.withPretendard(),
    bodyLarge = DefaultTypography.bodyLarge.withPretendard(),
    bodyMedium = DefaultTypography.bodyMedium.withPretendard(),
    bodySmall = DefaultTypography.bodySmall.withPretendard(),
    labelLarge = DefaultTypography.labelLarge.withPretendard(),
    labelMedium = DefaultTypography.labelMedium.withPretendard(),
    labelSmall = DefaultTypography.labelSmall.withPretendard()
)
