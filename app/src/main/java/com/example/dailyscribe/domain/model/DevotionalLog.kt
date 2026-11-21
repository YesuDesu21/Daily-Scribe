package com.example.dailyscribe.domain.model

data class DevotionalLog(
    val id: String,
    val date: String,
    val devotionsCompleted: Int,
    val targetDevotions: Int,
    val streakCount: Int,
    val completionTimestamps: List<Long> = emptyList()
)
