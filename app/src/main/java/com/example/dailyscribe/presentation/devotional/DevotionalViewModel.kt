package com.example.dailyscribe.presentation.devotional

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dailyscribe.domain.model.DevotionalLog
import com.example.dailyscribe.domain.model.DevotionalStats
import com.example.dailyscribe.domain.model.DevotionalStatsCalculator
import com.example.dailyscribe.domain.repository.DevotionalRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class DevotionalViewModel(
    private val devotionalRepository: DevotionalRepository
) : ViewModel() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val todayDateString: String = dateFormat.format(Date())

    private val _selectedDate = MutableStateFlow(todayDateString)
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    val allLogs: StateFlow<List<DevotionalLog>> = devotionalRepository.getAllDevotionalLogs()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val devotionalStats: StateFlow<DevotionalStats> = allLogs
        .map { logs ->
            DevotionalStatsCalculator.calculateStats(logs, todayDateString)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DevotionalStats()
        )

    val selectedDevotionalLog: StateFlow<DevotionalLog?> = _selectedDate
        .flatMapLatest { date ->
            devotionalRepository.getDevotionalLogForDate(date)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val todayDevotionalLog: StateFlow<DevotionalLog?> = devotionalRepository
        .getDevotionalLogForDate(todayDateString)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun selectDate(date: String) {
        _selectedDate.value = date
    }

    fun recordDevotionCompleted(targetDevotions: Int = 1) {
        viewModelScope.launch {
            devotionalRepository.incrementDevotionalProgress(_selectedDate.value, targetDevotions)
        }
    }

    class Factory(private val repository: DevotionalRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DevotionalViewModel(repository) as T
        }
    }
}
