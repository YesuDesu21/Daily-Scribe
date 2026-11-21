package com.example.dailyscribe.domain.model

data class ReminderSettings(
    val id: Int = 1,
    val preferredTimes: List<String> = listOf("08:00", "20:00"),
    val followUpCount: Int = 0,
    val maxFollowUps: Int = 2,
    val targetDevotionsPerDay: Int = 2
)
