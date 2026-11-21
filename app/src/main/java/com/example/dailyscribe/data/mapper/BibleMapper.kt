package com.example.dailyscribe.data.mapper

import com.example.dailyscribe.data.local.entity.BibleVerseEntity
import com.example.dailyscribe.domain.model.BibleVerse

fun BibleVerseEntity.toDomainModel(): BibleVerse {
    return BibleVerse(
        id = id,
        translationId = translationId,
        book = book,
        chapter = chapter,
        verse = verse,
        text = text
    )
}

fun BibleVerse.toEntity(): BibleVerseEntity {
    return BibleVerseEntity(
        id = id,
        translationId = translationId,
        book = book,
        chapter = chapter,
        verse = verse,
        text = text
    )
}
