package com.example.dailyscribe.domain.usecase

import com.example.dailyscribe.domain.model.BibleVerse
import com.example.dailyscribe.domain.repository.BibleRepository
import kotlinx.coroutines.flow.Flow

class GetBibleVerseUseCase(
    private val bibleRepository: BibleRepository
) {
    operator fun invoke(translationId: String, book: String, chapter: Int): Flow<List<BibleVerse>> {
        return bibleRepository.getVerses(translationId, book, chapter)
    }
}
