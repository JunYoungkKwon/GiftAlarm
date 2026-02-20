package com.example.gifticonalarm.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.gifticonalarm.domain.model.NotificationSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 알림 설정 DataStore 접근을 담당한다.
 */
class NotificationSettingsPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    fun observeNotificationSettings(): Flow<NotificationSettings> {
        return dataStore.data.map { preferences ->
            NotificationSettings(
                pushEnabled = preferences[PUSH_ENABLED_KEY] ?: true,
                selectedDays = preferences[SELECTED_DAYS_KEY]
                    ?.mapNotNull { it.toIntOrNull() }
                    ?.toSet()
                    ?: DEFAULT_SELECTED_DAYS,
                notifyHour = preferences[NOTIFY_HOUR_KEY] ?: 9,
                notifyMinute = preferences[NOTIFY_MINUTE_KEY] ?: 0
            )
        }
    }

    suspend fun getNotificationSettings(): NotificationSettings {
        return observeNotificationSettings().first()
    }

    suspend fun updateNotificationSettings(settings: NotificationSettings) {
        dataStore.edit { preferences ->
            preferences[PUSH_ENABLED_KEY] = settings.pushEnabled
            preferences[SELECTED_DAYS_KEY] = settings.selectedDays.map { it.toString() }.toSet()
            preferences[NOTIFY_HOUR_KEY] = settings.notifyHour
            preferences[NOTIFY_MINUTE_KEY] = settings.notifyMinute
        }
    }

    private companion object {
        val PUSH_ENABLED_KEY = booleanPreferencesKey("push_enabled")
        val SELECTED_DAYS_KEY = stringSetPreferencesKey("selected_notify_days")
        val NOTIFY_HOUR_KEY = intPreferencesKey("notify_hour")
        val NOTIFY_MINUTE_KEY = intPreferencesKey("notify_minute")
        val DEFAULT_SELECTED_DAYS = setOf(1, 3, 7)
    }
}
