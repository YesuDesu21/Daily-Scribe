package com.example.dailyscribe.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devotional_logs")
data class DevotionalLogEntity(
    @PrimaryKey val id: String,
    val date: String,
    val devotionsCompleted: Int,
    val targetDevotions: Int,
    val streakCount: Int,
    val completionTimestamps: List<Long>
)
