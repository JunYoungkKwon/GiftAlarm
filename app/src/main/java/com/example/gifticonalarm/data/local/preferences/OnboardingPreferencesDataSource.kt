package com.example.gifticonalarm.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 온보딩 관련 DataStore 접근을 담당하는 데이터소스.
 */
class OnboardingPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    /**
     * 온보딩 완료 여부를 관찰한다.
     */
    fun observeOnboardingCompleted(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[IS_ONBOARDING_COMPLETED_KEY] ?: false
        }
    }

    /**
     * 온보딩 완료 여부를 저장한다.
     */
    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_ONBOARDING_COMPLETED_KEY] = completed
        }
    }

    private companion object {
        val IS_ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("isOnboardingCompleted")
    }
}
