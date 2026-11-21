package com.example.dailyscribe.data.mapper

import com.example.dailyscribe.data.local.entity.JournalEntryEntity
import com.example.dailyscribe.domain.model.JournalEntry

fun JournalEntryEntity.toDomainModel(): JournalEntry {
    return JournalEntry(
        id = id,
        title = title,
        content = content,
        isOcr = isOcr,
        isDraft = isDraft,
        createdAt = createdAt,
        updatedAt = updatedAt,
        tags = tags,
        scriptureRef = scriptureRef,
        mood = mood
    )
}

fun JournalEntry.toEntity(): JournalEntryEntity {
    return JournalEntryEntity(
        id = id,
        title = title,
        content = content,
        isOcr = isOcr,
        isDraft = isDraft,
        createdAt = createdAt,
        updatedAt = updatedAt,
        tags = tags,
        scriptureRef = scriptureRef,
        mood = mood
    )
}
