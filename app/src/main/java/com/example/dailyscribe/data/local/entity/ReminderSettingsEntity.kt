package com.example.dailyscribe.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder_settings")
data class ReminderSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val preferredTimes: List<String>,
    val followUpCount: Int,
    val maxFollowUps: Int,
    val targetDevotionsPerDay: Int
)
