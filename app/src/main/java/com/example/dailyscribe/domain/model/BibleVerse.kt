package com.example.dailyscribe.domain.model

data class BibleVerse(
    val id: String,
    val translationId: String,
    val book: String,
    val chapter: Int,
    val verse: Int,
    val text: String
)
