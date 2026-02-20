package com.example.gifticonalarm.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.gifticonalarm.domain.model.NotificationInboxState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 알림함 읽음/삭제 상태 DataStore 접근을 담당한다.
 */
class NotificationInboxPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    fun observeInboxState(): Flow<NotificationInboxState> {
        return dataStore.data.map { preferences ->
            NotificationInboxState(
                readNotificationIds = preferences[READ_IDS_KEY]
                    .orEmpty()
                    .mapNotNull { it.toLongOrNull() }
                    .toSet(),
                deletedNotificationIds = preferences[DELETED_IDS_KEY]
                    .orEmpty()
                    .mapNotNull { it.toLongOrNull() }
                    .toSet()
            )
        }
    }

    suspend fun getInboxState(): NotificationInboxState {
        return observeInboxState().first()
    }

    suspend fun markAsRead(notificationIds: Set<Long>) {
        if (notificationIds.isEmpty()) return
        val current = getInboxState()
        dataStore.edit { preferences ->
            preferences[READ_IDS_KEY] = (current.readNotificationIds + notificationIds)
                .map { it.toString() }
                .toSet()
        }
    }

    suspend fun deleteNotifications(notificationIds: Set<Long>) {
        if (notificationIds.isEmpty()) return
        val current = getInboxState()
        dataStore.edit { preferences ->
            preferences[DELETED_IDS_KEY] = (current.deletedNotificationIds + notificationIds)
                .map { it.toString() }
                .toSet()
            preferences[READ_IDS_KEY] = (current.readNotificationIds + notificationIds)
                .map { it.toString() }
                .toSet()
        }
    }

    private companion object {
        val READ_IDS_KEY = stringSetPreferencesKey("notification_read_ids")
        val DELETED_IDS_KEY = stringSetPreferencesKey("notification_deleted_ids")
    }
}
