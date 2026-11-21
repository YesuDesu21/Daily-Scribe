package com.example.dailyscribe.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dailyscribe.data.local.entity.BibleVerseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BibleDao {
    @Query("SELECT * FROM bible_verses WHERE translationId = :translationId AND book = :book AND chapter = :chapter ORDER BY verse ASC")
    fun getVerses(translationId: String, book: String, chapter: Int): Flow<List<BibleVerseEntity>>

    @Query("SELECT * FROM bible_verses WHERE translationId = :translationId AND book = :book AND chapter = :chapter AND verse = :verse LIMIT 1")
    fun getVerse(translationId: String, book: String, chapter: Int, verse: Int): Flow<BibleVerseEntity?>

    @Query("SELECT * FROM bible_verses WHERE translationId = :translationId AND text LIKE '%' || :query || '%' ORDER BY book ASC, chapter ASC, verse ASC")
    fun searchVerses(translationId: String, query: String): Flow<List<BibleVerseEntity>>

    @Query("SELECT DISTINCT book FROM bible_verses WHERE translationId = :translationId")
    fun getBooks(translationId: String): Flow<List<String>>

    @Query("SELECT DISTINCT chapter FROM bible_verses WHERE translationId = :translationId AND book = :book ORDER BY chapter ASC")
    fun getChapters(translationId: String, book: String): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerses(verses: List<BibleVerseEntity>)
}
