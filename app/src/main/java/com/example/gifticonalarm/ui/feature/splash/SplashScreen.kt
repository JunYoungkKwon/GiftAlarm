package com.example.gifticonalarm.ui.feature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gifticonalarm.ui.theme.GifticonBrandPrimary
import com.example.gifticonalarm.ui.theme.GifticonWhite

private val SplashBackground = GifticonWhite
private val SplashPrimary = GifticonBrandPrimary
private val SplashRing = Color(0x1A191970)
private val SplashRibbon = Color(0x33FFFFFF)
private val SplashSubText = Color(0x99191970)
private val SplashGlow = Color(0x0D191970)

/**
 * 스티치 시안을 기반으로 구성한 앱 스플래시 화면.
 */
@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SplashBackground)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-80).dp, y = (-80).dp)
                .size(220.dp)
                .background(SplashGlow, CircleShape)
                .blur(48.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 96.dp, y = 96.dp)
                .size(280.dp)
                .background(SplashGlow, CircleShape)
                .blur(56.dp)
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(132.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(4.dp, SplashRing, CircleShape)
                )

                Box(
                    modifier = Modifier
                        .offset(y = (-24).dp)
                        .size(width = 32.dp, height = 22.dp)
                        .border(
                            width = 4.dp,
                            color = SplashPrimary,
                            shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)
                        )
                )

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(SplashPrimary, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .size(width = 4.dp, height = 80.dp)
                            .background(SplashRibbon)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(width = 80.dp, height = 4.dp)
                            .background(SplashRibbon)
                    )
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "알림",
                        tint = Color.White,
                        modifier = Modifier.size(44.dp)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "기프트알람",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = SplashPrimary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "PREMIUM GIFT MANAGEMENT",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = SplashSubText,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.6.sp
                    )
                )
            }
        }
    }
}
