package com.example.dailyscribe.domain.usecase

import com.example.dailyscribe.domain.repository.JournalRepository

class DeleteJournalEntryUseCase(
    private val journalRepository: JournalRepository
) {
    suspend operator fun invoke(id: String) {
        journalRepository.deleteEntry(id)
    }
}
