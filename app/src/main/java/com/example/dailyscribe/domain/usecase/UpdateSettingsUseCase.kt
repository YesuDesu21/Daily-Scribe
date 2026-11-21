package com.example.dailyscribe.domain.usecase

import com.example.dailyscribe.domain.model.ReminderSettings
import com.example.dailyscribe.domain.repository.SettingsRepository

class UpdateSettingsUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(settings: ReminderSettings) {
        settingsRepository.updateSettings(settings)
    }
}
