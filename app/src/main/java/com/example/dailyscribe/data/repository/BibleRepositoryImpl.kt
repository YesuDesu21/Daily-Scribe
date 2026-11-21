package com.example.dailyscribe.data.repository

import com.example.dailyscribe.data.local.dao.BibleDao
import com.example.dailyscribe.data.mapper.toDomainModel
import com.example.dailyscribe.data.mapper.toEntity
import com.example.dailyscribe.domain.model.BibleVerse
import com.example.dailyscribe.domain.repository.BibleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BibleRepositoryImpl(
    private val bibleDao: BibleDao
) : BibleRepository {

    override fun getVerses(translationId: String, book: String, chapter: Int): Flow<List<BibleVerse>> {
        return bibleDao.getVerses(translationId, book, chapter).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getVerse(translationId: String, book: String, chapter: Int, verse: Int): Flow<BibleVerse?> {
        return bibleDao.getVerse(translationId, book, chapter, verse).map { entity ->
            entity?.toDomainModel()
        }
    }

    override fun searchVerses(translationId: String, query: String): Flow<List<BibleVerse>> {
        return bibleDao.searchVerses(translationId, query).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getBooks(translationId: String): Flow<List<String>> {
        return bibleDao.getBooks(translationId)
    }

    override fun getChapters(translationId: String, book: String): Flow<List<Int>> {
        return bibleDao.getChapters(translationId, book)
    }

    override suspend fun insertVerses(verses: List<BibleVerse>) {
        bibleDao.insertVerses(verses.map { it.toEntity() })
    }
}
