package com.example.dailyscribe.presentation.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dailyscribe.domain.model.ReminderSettings
import com.example.dailyscribe.domain.repository.SettingsRepository
import com.example.dailyscribe.util.ReminderScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    application: Application,
    private val settingsRepository: SettingsRepository
) : AndroidViewModel(application) {

    val settings: StateFlow<ReminderSettings> = settingsRepository.getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ReminderSettings()
        )

    fun updateTargetDevotions(target: Int) {
        viewModelScope.launch {
            val current = settings.value
            val updated = current.copy(targetDevotionsPerDay = target)
            settingsRepository.updateSettings(updated)
            ReminderScheduler.scheduleAlarms(getApplication(), updated)
        }
    }

    fun updateMaxFollowUps(max: Int) {
        viewModelScope.launch {
            val current = settings.value
            val updated = current.copy(maxFollowUps = max)
            settingsRepository.updateSettings(updated)
            ReminderScheduler.scheduleAlarms(getApplication(), updated)
        }
    }

    fun addPreferredTime(time: String) {
        viewModelScope.launch {
            val current = settings.value
            if (!current.preferredTimes.contains(time)) {
                val updatedTimes = current.preferredTimes + time
                val updated = current.copy(preferredTimes = updatedTimes)
                settingsRepository.updateSettings(updated)
                ReminderScheduler.scheduleAlarms(getApplication(), updated)
            }
        }
    }

    fun removePreferredTime(time: String) {
        viewModelScope.launch {
            val current = settings.value
            val updatedTimes = current.preferredTimes - time
            val updated = current.copy(preferredTimes = updatedTimes)
            settingsRepository.updateSettings(updated)
            ReminderScheduler.scheduleAlarms(getApplication(), updated)
        }
    }

    class Factory(
        private val application: Application,
        private val repository: SettingsRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(application, repository) as T
        }
    }
}
