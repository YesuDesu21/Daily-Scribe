package com.example.dailyscribe.domain.usecase

import com.example.dailyscribe.domain.repository.DevotionalRepository

class UpdateDevotionalProgressUseCase(
    private val devotionalRepository: DevotionalRepository
) {
    suspend operator fun invoke(date: String, targetDevotions: Int) {
        devotionalRepository.incrementDevotionalProgress(date, targetDevotions)
    }
}
