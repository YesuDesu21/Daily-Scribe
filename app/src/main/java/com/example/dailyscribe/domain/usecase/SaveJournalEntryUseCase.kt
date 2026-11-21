package com.example.dailyscribe.domain.usecase

import com.example.dailyscribe.domain.model.JournalEntry
import com.example.dailyscribe.domain.repository.JournalRepository

class SaveJournalEntryUseCase(
    private val journalRepository: JournalRepository
) {
    suspend operator fun invoke(entry: JournalEntry) {
        journalRepository.saveEntry(entry)
    }
}
