package com.example.dailyscribe.data.repository

import com.example.dailyscribe.data.local.dao.DevotionalDao
import com.example.dailyscribe.data.local.entity.DevotionalLogEntity
import com.example.dailyscribe.data.mapper.toDomainModel
import com.example.dailyscribe.data.mapper.toEntity
import com.example.dailyscribe.domain.model.DevotionalLog
import com.example.dailyscribe.domain.repository.DevotionalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class DevotionalRepositoryImpl(
    private val devotionalDao: DevotionalDao
) : DevotionalRepository {

    override fun getDevotionalLogForDate(date: String): Flow<DevotionalLog?> {
        return devotionalDao.getLogForDate(date).map { entity ->
            entity?.toDomainModel()
        }
    }

    override fun getAllDevotionalLogs(): Flow<List<DevotionalLog>> {
        return devotionalDao.getAllLogs().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun saveDevotionalLog(log: DevotionalLog) {
        devotionalDao.insertOrUpdate(log.toEntity())
    }

    override suspend fun incrementDevotionalProgress(date: String, targetDevotions: Int) {
        val existing = devotionalDao.getLogForDateSync(date)
        val now = System.currentTimeMillis()
        if (existing != null) {
            val updatedCompleted = existing.devotionsCompleted + 1
            val updatedTimestamps = existing.completionTimestamps + now
            val updatedStreak = if (updatedCompleted >= targetDevotions) {
                existing.streakCount + 1
            } else {
                existing.streakCount
            }
            val updatedLog = existing.copy(
                devotionsCompleted = updatedCompleted,
                targetDevotions = targetDevotions,
                streakCount = updatedStreak,
                completionTimestamps = updatedTimestamps
            )
            devotionalDao.insertOrUpdate(updatedLog)
        } else {
            val newLog = DevotionalLogEntity(
                id = UUID.randomUUID().toString(),
                date = date,
                devotionsCompleted = 1,
                targetDevotions = targetDevotions,
                streakCount = 1,
                completionTimestamps = listOf(now)
            )
            devotionalDao.insertOrUpdate(newLog)
        }
    }
}
