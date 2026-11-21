package com.example.dailyscribe.domain.usecase

import com.example.dailyscribe.domain.model.ReminderSettings
import com.example.dailyscribe.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetSettingsUseCase(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<ReminderSettings> {
        return settingsRepository.getSettings()
    }
}
