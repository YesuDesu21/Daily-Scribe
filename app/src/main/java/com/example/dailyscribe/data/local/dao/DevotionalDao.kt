package com.example.dailyscribe.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dailyscribe.data.local.entity.DevotionalLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DevotionalDao {
    @Query("SELECT * FROM devotional_logs WHERE date = :date LIMIT 1")
    fun getLogForDate(date: String): Flow<DevotionalLogEntity?>

    @Query("SELECT * FROM devotional_logs WHERE date = :date LIMIT 1")
    suspend fun getLogForDateSync(date: String): DevotionalLogEntity?

    @Query("SELECT * FROM devotional_logs ORDER BY date DESC")
    fun getAllLogs(): Flow<List<DevotionalLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(log: DevotionalLogEntity)
}
