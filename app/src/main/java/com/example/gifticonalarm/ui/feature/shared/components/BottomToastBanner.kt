package com.example.gifticonalarm.ui.feature.shared.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 화면 하단 중앙에 토스트 배너를 렌더링한다.
 */
@Composable
fun BoxScope.BottomToastBanner(
    message: String?,
    modifier: Modifier = Modifier
) {
    message?.let { text ->
        ToastBanner(
            message = text,
            modifier = modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        )
    }
}
