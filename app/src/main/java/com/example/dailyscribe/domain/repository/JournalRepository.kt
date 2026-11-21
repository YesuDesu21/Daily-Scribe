package com.example.dailyscribe.domain.repository

import com.example.dailyscribe.domain.model.JournalEntry
import kotlinx.coroutines.flow.Flow

interface JournalRepository {
    fun getAllEntries(): Flow<List<JournalEntry>>
    fun getEntryById(id: String): Flow<JournalEntry?>
    fun searchEntries(query: String): Flow<List<JournalEntry>>
    fun getDraft(): Flow<JournalEntry?>
    suspend fun saveDraft(entry: JournalEntry)
    suspend fun clearDraft()
    suspend fun saveEntry(entry: JournalEntry)
    suspend fun deleteEntry(id: String)
}
