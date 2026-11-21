package com.example.dailyscribe.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dailyscribe.data.local.entity.ReminderSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderSettingsDao {
    @Query("SELECT * FROM reminder_settings WHERE id = 1 LIMIT 1")
    fun getSettings(): Flow<ReminderSettingsEntity?>

    @Query("SELECT * FROM reminder_settings WHERE id = 1 LIMIT 1")
    suspend fun getSettingsSync(): ReminderSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(settings: ReminderSettingsEntity)
}
