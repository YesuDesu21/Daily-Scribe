package com.example.dailyscribe.domain.usecase

import com.example.dailyscribe.domain.model.JournalEntry
import com.example.dailyscribe.domain.repository.JournalRepository
import kotlinx.coroutines.flow.Flow

class GetJournalEntriesUseCase(
    private val journalRepository: JournalRepository
) {
    operator fun invoke(query: String = ""): Flow<List<JournalEntry>> {
        return if (query.isBlank()) {
            journalRepository.getAllEntries()
        } else {
            journalRepository.searchEntries(query)
        }
    }
}
