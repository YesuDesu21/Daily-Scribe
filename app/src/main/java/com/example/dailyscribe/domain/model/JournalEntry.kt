package com.example.dailyscribe.domain.model

data class JournalEntry(
    val id: String,
    val title: String,
    val content: String,
    val isOcr: Boolean = false,
    val isDraft: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val tags: List<String> = emptyList(),
    val scriptureRef: String? = null,
    val mood: String? = null
)
