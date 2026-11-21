package com.example.dailyscribe.domain.repository

import com.example.dailyscribe.domain.model.DevotionalLog
import kotlinx.coroutines.flow.Flow

interface DevotionalRepository {
    fun getDevotionalLogForDate(date: String): Flow<DevotionalLog?>
    fun getAllDevotionalLogs(): Flow<List<DevotionalLog>>
    suspend fun saveDevotionalLog(log: DevotionalLog)
    suspend fun incrementDevotionalProgress(date: String, targetDevotions: Int)
}
