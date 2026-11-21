package com.example.dailyscribe.domain.repository

import com.example.dailyscribe.domain.model.BibleVerse
import kotlinx.coroutines.flow.Flow

interface BibleRepository {
    fun getVerses(translationId: String, book: String, chapter: Int): Flow<List<BibleVerse>>
    fun getVerse(translationId: String, book: String, chapter: Int, verse: Int): Flow<BibleVerse?>
    fun searchVerses(translationId: String, query: String): Flow<List<BibleVerse>>
    fun getBooks(translationId: String): Flow<List<String>>
    fun getChapters(translationId: String, book: String): Flow<List<Int>>
    suspend fun insertVerses(verses: List<BibleVerse>)
}
