package com.example.dailyscribe.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dailyscribe.data.local.converters.DatabaseConverters
import com.example.dailyscribe.data.local.dao.BibleDao
import com.example.dailyscribe.data.local.dao.DevotionalDao
import com.example.dailyscribe.data.local.dao.JournalDao
import com.example.dailyscribe.data.local.dao.ReminderSettingsDao
import com.example.dailyscribe.data.local.entity.BibleVerseEntity
import com.example.dailyscribe.data.local.entity.DevotionalLogEntity
import com.example.dailyscribe.data.local.entity.JournalEntryEntity
import com.example.dailyscribe.data.local.entity.ReminderSettingsEntity

@Database(
    entities = [
        JournalEntryEntity::class,
        DevotionalLogEntity::class,
        BibleVerseEntity::class,
        ReminderSettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun journalDao(): JournalDao
    abstract fun devotionalDao(): DevotionalDao
    abstract fun bibleDao(): BibleDao
    abstract fun reminderSettingsDao(): ReminderSettingsDao
}
