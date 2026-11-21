package com.example.dailyscribe.domain.usecase

import com.example.dailyscribe.domain.model.DevotionalLog
import com.example.dailyscribe.domain.repository.DevotionalRepository
import kotlinx.coroutines.flow.Flow

class GetDevotionalLogUseCase(
    private val devotionalRepository: DevotionalRepository
) {
    operator fun invoke(date: String): Flow<DevotionalLog?> {
        return devotionalRepository.getDevotionalLogForDate(date)
    }
}
