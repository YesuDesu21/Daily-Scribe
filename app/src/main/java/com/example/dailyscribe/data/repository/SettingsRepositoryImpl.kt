package com.example.dailyscribe.data.repository

import com.example.dailyscribe.data.local.dao.ReminderSettingsDao
import com.example.dailyscribe.data.mapper.toDomainModel
import com.example.dailyscribe.data.mapper.toEntity
import com.example.dailyscribe.domain.model.ReminderSettings
import com.example.dailyscribe.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val reminderSettingsDao: ReminderSettingsDao
) : SettingsRepository {

    override fun getSettings(): Flow<ReminderSettings> {
        return reminderSettingsDao.getSettings().map { entity ->
            entity?.toDomainModel() ?: ReminderSettings()
        }
    }

    override suspend fun updateSettings(settings: ReminderSettings) {
        reminderSettingsDao.insertOrUpdate(settings.toEntity())
    }
}
