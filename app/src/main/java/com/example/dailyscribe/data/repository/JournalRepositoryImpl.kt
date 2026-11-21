package com.example.dailyscribe.data.repository

import com.example.dailyscribe.data.local.dao.JournalDao
import com.example.dailyscribe.data.mapper.toDomainModel
import com.example.dailyscribe.data.mapper.toEntity
import com.example.dailyscribe.domain.model.JournalEntry
import com.example.dailyscribe.domain.repository.JournalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class JournalRepositoryImpl(
    private val journalDao: JournalDao
) : JournalRepository {

    override fun getAllEntries(): Flow<List<JournalEntry>> {
        return journalDao.getAllEntries().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getEntryById(id: String): Flow<JournalEntry?> {
        return journalDao.getEntryById(id).map { entity ->
            entity?.toDomainModel()
        }
    }

    override fun searchEntries(query: String): Flow<List<JournalEntry>> {
        return journalDao.searchEntries(query).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getDraft(): Flow<JournalEntry?> {
        return journalDao.getDraft().map { entity ->
            entity?.toDomainModel()
        }
    }

    override suspend fun saveDraft(entry: JournalEntry) {
        val draftEntity = entry.copy(isDraft = true).toEntity()
        journalDao.insertOrUpdate(draftEntity)
    }

    override suspend fun clearDraft() {
        journalDao.deleteDraft()
    }

    override suspend fun saveEntry(entry: JournalEntry) {
        val finalEntity = entry.copy(isDraft = false).toEntity()
        journalDao.insertOrUpdate(finalEntity)
        journalDao.deleteDraft()
    }

    override suspend fun deleteEntry(id: String) {
        journalDao.deleteById(id)
    }
}
