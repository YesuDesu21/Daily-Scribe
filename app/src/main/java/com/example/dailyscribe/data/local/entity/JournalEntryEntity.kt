package com.example.dailyscribe.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntryEntity(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    val isOcr: Boolean,
    val isDraft: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long,
    val tags: List<String>,
    val scriptureRef: String?,
    val mood: String?
)
