package com.example.dailyscribe

import com.example.dailyscribe.data.local.entity.BibleVerseEntity
import com.example.dailyscribe.data.local.entity.DevotionalLogEntity
import com.example.dailyscribe.data.local.entity.JournalEntryEntity
import com.example.dailyscribe.data.local.entity.ReminderSettingsEntity
import com.example.dailyscribe.data.mapper.toDomainModel
import com.example.dailyscribe.data.mapper.toEntity
import com.example.dailyscribe.domain.model.BibleVerse
import com.example.dailyscribe.domain.model.DevotionalLog
import com.example.dailyscribe.domain.model.JournalEntry
import com.example.dailyscribe.domain.model.ReminderSettings
import org.junit.Assert.assertEquals
import org.junit.Test

class MapperUnitTest {

    @Test
    fun journalEntryMapper_convertsEntityToDomainAndBack() {
        val entity = JournalEntryEntity(
            id = "test-id",
            title = "Morning Reflection",
            content = "God is good",
            isOcr = true,
            createdAt = 1000L,
            updatedAt = 2000L,
            tags = listOf("Prayer", "Peace"),
            scriptureRef = "John 1:1",
            mood = "Peaceful"
        )

        val domain = entity.toDomainModel()
        assertEquals(entity.id, domain.id)
        assertEquals(entity.title, domain.title)
        assertEquals(entity.content, domain.content)
        assertEquals(entity.isOcr, domain.isOcr)
        assertEquals(entity.createdAt, domain.createdAt)
        assertEquals(entity.tags, domain.tags)

        val backToEntity = domain.toEntity()
        assertEquals(entity, backToEntity)
    }

    @Test
    fun devotionalLogMapper_convertsEntityToDomainAndBack() {
        val entity = DevotionalLogEntity(
            id = "log-1",
            date = "2025-05-20",
            devotionsCompleted = 2,
            targetDevotions = 2,
            streakCount = 5,
            completionTimestamps = listOf(100L, 200L)
        )

        val domain = entity.toDomainModel()
        assertEquals(entity.id, domain.id)
        assertEquals(entity.streakCount, domain.streakCount)
        assertEquals(entity.completionTimestamps, domain.completionTimestamps)

        val backToEntity = domain.toEntity()
        assertEquals(entity, backToEntity)
    }

    @Test
    fun bibleVerseMapper_convertsEntityToDomainAndBack() {
        val entity = BibleVerseEntity(
            id = "WEB_John_1_1",
            translationId = "WEB",
            book = "John",
            chapter = 1,
            verse = 1,
            text = "In the beginning was the Word"
        )

        val domain = entity.toDomainModel()
        assertEquals(entity.id, domain.id)
        assertEquals(entity.text, domain.text)

        val backToEntity = domain.toEntity()
        assertEquals(entity, backToEntity)
    }

    @Test
    fun reminderSettingsMapper_convertsEntityToDomainAndBack() {
        val entity = ReminderSettingsEntity(
            id = 1,
            preferredTimes = listOf("08:00", "20:00"),
            followUpCount = 1,
            maxFollowUps = 3,
            targetDevotionsPerDay = 2
        )

        val domain = entity.toDomainModel()
        assertEquals(entity.id, domain.id)
        assertEquals(entity.preferredTimes, domain.preferredTimes)
        assertEquals(entity.targetDevotionsPerDay, domain.targetDevotionsPerDay)

        val backToEntity = domain.toEntity()
        assertEquals(entity, backToEntity)
    }
}
