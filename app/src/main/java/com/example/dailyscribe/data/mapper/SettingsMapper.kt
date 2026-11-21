package com.example.dailyscribe.data.mapper

import com.example.dailyscribe.data.local.entity.ReminderSettingsEntity
import com.example.dailyscribe.domain.model.ReminderSettings

fun ReminderSettingsEntity.toDomainModel(): ReminderSettings {
    return ReminderSettings(
        id = id,
        preferredTimes = preferredTimes,
        followUpCount = followUpCount,
        maxFollowUps = maxFollowUps,
        targetDevotionsPerDay = targetDevotionsPerDay
    )
}

fun ReminderSettings.toEntity(): ReminderSettingsEntity {
    return ReminderSettingsEntity(
        id = id,
        preferredTimes = preferredTimes,
        followUpCount = followUpCount,
        maxFollowUps = maxFollowUps,
        targetDevotionsPerDay = targetDevotionsPerDay
    )
}
