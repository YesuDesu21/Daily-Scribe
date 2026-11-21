package com.example.dailyscribe.data.mapper

import com.example.dailyscribe.data.local.entity.DevotionalLogEntity
import com.example.dailyscribe.domain.model.DevotionalLog

fun DevotionalLogEntity.toDomainModel(): DevotionalLog {
    return DevotionalLog(
        id = id,
        date = date,
        devotionsCompleted = devotionsCompleted,
        targetDevotions = targetDevotions,
        streakCount = streakCount,
        completionTimestamps = completionTimestamps
    )
}

fun DevotionalLog.toEntity(): DevotionalLogEntity {
    return DevotionalLogEntity(
        id = id,
        date = date,
        devotionsCompleted = devotionsCompleted,
        targetDevotions = targetDevotions,
        streakCount = streakCount,
        completionTimestamps = completionTimestamps
    )
}
