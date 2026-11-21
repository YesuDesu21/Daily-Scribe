package com.example.dailyscribe.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bible_verses")
data class BibleVerseEntity(
    @PrimaryKey val id: String,
    val translationId: String,
    val book: String,
    val chapter: Int,
    val verse: Int,
    val text: String
)
