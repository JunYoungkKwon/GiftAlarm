package com.example.gifticonalarm.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoFixHigh
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

private val OnboardingBackground = Color(0xFFFFFFFF)
private val OnboardingAccent = Color(0xFF191970)
private val OnboardingSubText = Color(0xFF6B7280)
private val OnboardingMuted = Color(0xFFE5E7EB)

/**
 * 온보딩 3페이지를 표시하는 컨테이너 화면.
 */
@Composable
fun OnboardingScreen(
    onStartClick: () -> Unit
) {
    val pages = onboardingPages()
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()
    val lastPageIndex = pages.lastIndex
    val isLastPage = pagerState.currentPage == lastPageIndex

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OnboardingBackground)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(lastPageIndex)
                    }
                }
            ) {
                Text(
                    text = "건너뛰기",
                    color = OnboardingAccent,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { pageIndex ->
            OnboardingPageContent(page = pages[pageIndex])
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pages.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(width = if (isSelected) 28.dp else 8.dp, height = 8.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) OnboardingAccent else OnboardingMuted)
                )
            }
        }

        Button(
            onClick = {
                if (isLastPage) {
                    onStartClick()
                    return@Button
                }
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = OnboardingAccent,
                contentColor = Color.White
            )
        ) {
            Text(
                text = if (isLastPage) "시작하기" else "다음",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPageUiModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (page.type) {
            OnboardingType.AUTO_REGISTRATION -> AutoRegistrationIllustration()
            OnboardingType.EXPIRATION_MANAGEMENT -> ExpirationManagementIllustration()
            OnboardingType.NOTIFICATION -> NotificationIllustration()
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = OnboardingAccent,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = page.description,
            style = MaterialTheme.typography.titleMedium,
            lineHeight = 28.sp,
            color = OnboardingSubText,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AutoRegistrationIllustration() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(270.dp)
                .clip(CircleShape)
                .background(Color(0x1A191970))
        )

        Card(
            modifier = Modifier.size(width = 190.dp, height = 160.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1F2937))
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(92.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF374151))
                        .border(8.dp, Color(0xFF9CA3AF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Coffee,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 30.dp)
                .size(width = 106.dp, height = 136.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFEF3C7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Coffee,
                        contentDescription = null,
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(28.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE5E7EB))
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE5E7EB))
                )
            }
        }

        Card(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 46.dp)
                .size(width = 126.dp, height = 46.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AutoFixHigh,
                    contentDescription = null,
                    tint = OnboardingAccent
                )
                Text(
                    text = "찰칵! 자동 인식",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4B5563)
                )
            }
        }
    }
}

@Composable
private fun ExpirationManagementIllustration() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(270.dp)
                .clip(CircleShape)
                .background(Color(0x14191970))
        )

        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) { index ->
                Card(
                    modifier = Modifier.size(width = (210 + index * 14).dp, height = 28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {}
            }
        }

        Card(
            modifier = Modifier.size(width = 170.dp, height = 192.dp),
            shape = RoundedCornerShape(42.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Rounded.Schedule,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(62.dp)
                )
            }
        }

        Card(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 28.dp, bottom = 42.dp)
                .size(width = 100.dp, height = 126.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(66.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFFEDD5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Coffee,
                        contentDescription = null,
                        tint = Color(0xFFF97316),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "D-3",
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFEF4444))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun NotificationIllustration() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .clip(CircleShape)
                .background(Color(0x1A3B82F6))
        )

        Card(
            modifier = Modifier.size(width = 220.dp, height = 188.dp),
            shape = RoundedCornerShape(34.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0x66FFFFFF)),
            border = androidx.compose.foundation.BorderStroke(3.dp, Color(0xFFD1D5DB))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                repeat(3) { index ->
                    val rowColor = when (index) {
                        0 -> Color(0xFF22C55E)
                        1 -> Color(0xFFFACC15)
                        else -> Color(0xFF3B82F6)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(rowColor.copy(alpha = 0.20f))
                            .border(1.dp, rowColor.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(rowColor)
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(CircleShape)
                                .background(rowColor.copy(alpha = 0.35f))
                        )
                        Box(
                            modifier = Modifier
                                .size(width = 14.dp, height = 10.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(rowColor.copy(alpha = 0.5f))
                        )
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 42.dp)
                .size(width = 152.dp, height = 48.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFDE047))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AutoFixHigh,
                    contentDescription = null,
                    tint = Color(0xFF854D0E)
                )
                Text(
                    text = "똑똑하게 관리해요",
                    color = Color(0xFF854D0E),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 50.dp, end = 42.dp)
                .size(42.dp)
                .clip(CircleShape)
                .background(Color(0xFF22C55E)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.NotificationsActive,
                contentDescription = null,
                tint = Color.White
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CoinBadge(symbol = "₩")
            CoinBadge(symbol = "P")
            TicketBadge()
        }
    }
}

@Composable
private fun CoinBadge(symbol: String) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.linearGradient(listOf(Color(0xFFFBBF24), Color(0xFFF59E0B)))
            )
            .border(2.dp, Color(0xFFCA8A04), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            color = Color(0xFF854D0E),
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
private fun TicketBadge() {
    Box(
        modifier = Modifier
            .size(width = 54.dp, height = 38.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                brush = Brush.linearGradient(listOf(Color(0xFFEC4899), Color(0xFFD946EF)))
            )
            .border(2.dp, Color(0xFF9D174D), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Savings,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )
    }
}

private fun onboardingPages(): List<OnboardingPageUiModel> {
    return listOf(
        OnboardingPageUiModel(
            type = OnboardingType.AUTO_REGISTRATION,
            title = "OCR 자동 등록",
            description = "이미지를 불러오거나 촬영하면\n쿠폰 정보를 자동으로 입력해요."
        ),
        OnboardingPageUiModel(
            type = OnboardingType.EXPIRATION_MANAGEMENT,
            title = "기프티콘 만료 관리",
            description = "만료 임박한 쿠폰을\n놓치지 않게 관리해드려요."
        ),
        OnboardingPageUiModel(
            type = OnboardingType.NOTIFICATION,
            title = "알림 및 잔액 확인",
            description = "잔액권 관리부터 알림 설정까지\n한 번에 확인하세요."
        )
    )
}

private enum class OnboardingType {
    AUTO_REGISTRATION,
    EXPIRATION_MANAGEMENT,
    NOTIFICATION
}

private data class OnboardingPageUiModel(
    val type: OnboardingType,
    val title: String,
    val description: String
)
