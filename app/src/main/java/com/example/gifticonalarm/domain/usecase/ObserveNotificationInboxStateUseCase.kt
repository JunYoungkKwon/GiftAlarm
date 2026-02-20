package com.example.gifticonalarm.domain.usecase

import com.example.gifticonalarm.domain.model.NotificationInboxState
import com.example.gifticonalarm.domain.repository.NotificationInboxRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 알림함 읽음/삭제 상태를 관찰한다.
 */
class ObserveNotificationInboxStateUseCase @Inject constructor(
    private val repository: NotificationInboxRepository
) {
    operator fun invoke(): Flow<NotificationInboxState> {
        return repository.observeInboxState()
    }
}
