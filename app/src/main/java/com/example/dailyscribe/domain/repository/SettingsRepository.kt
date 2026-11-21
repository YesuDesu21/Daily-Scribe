package com.example.dailyscribe.domain.repository

import com.example.dailyscribe.domain.model.ReminderSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<ReminderSettings>
    suspend fun updateSettings(settings: ReminderSettings)
}
