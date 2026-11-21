package com.example.dailyscribe.domain.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class DevotionalStats(
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalDevotions: Int = 0
)

object DevotionalStatsCalculator {

    private fun getDateFormat(): SimpleDateFormat {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        sdf.timeZone = TimeZone.getDefault()
        return sdf
    }

    fun calculateStats(
        logs: List<DevotionalLog>,
        todayDateString: String
    ): DevotionalStats {
        val total = logs.sumOf { it.devotionsCompleted }
        val completedDates = logs
            .filter { it.devotionsCompleted > 0 }
            .map { it.date }
            .toSet()

        if (completedDates.isEmpty()) {
            return DevotionalStats(currentStreak = 0, longestStreak = 0, totalDevotions = total)
        }

        val dateFormat = getDateFormat()

        val sortedDates = completedDates.mapNotNull { dateStr ->
            try {
                dateFormat.parse(dateStr)
            } catch (e: Exception) {
                null
            }
        }.sorted()

        if (sortedDates.isEmpty()) {
            return DevotionalStats(currentStreak = 0, longestStreak = 0, totalDevotions = total)
        }

        var maxStreak = 0
        var tempStreak = 0
        var prevCal: Calendar? = null

        for (date in sortedDates) {
            val currCal = Calendar.getInstance().apply {
                time = date
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            if (prevCal == null) {
                tempStreak = 1
            } else {
                val diffDays = ((currCal.timeInMillis - prevCal.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()
                if (diffDays == 1) {
                    tempStreak++
                } else if (diffDays > 1) {
                    tempStreak = 1
                }
            }
            if (tempStreak > maxStreak) {
                maxStreak = tempStreak
            }
            prevCal = currCal
        }

        val todayDate = try {
            dateFormat.parse(todayDateString)
        } catch (e: Exception) {
            Date()
        }

        val todayCal = Calendar.getInstance().apply {
            time = todayDate
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val todayStr = dateFormat.format(todayCal.time)

        val yesterdayCal = (todayCal.clone() as Calendar).apply {
            add(Calendar.DAY_OF_YEAR, -1)
        }
        val yesterdayStr = dateFormat.format(yesterdayCal.time)

        var currStreak = 0
        val checkCal = Calendar.getInstance()

        if (completedDates.contains(todayStr)) {
            checkCal.time = todayCal.time
        } else if (completedDates.contains(yesterdayStr)) {
            checkCal.time = yesterdayCal.time
        } else {
            checkCal.time = todayCal.time
        }

        while (true) {
            val checkStr = dateFormat.format(checkCal.time)
            if (completedDates.contains(checkStr)) {
                currStreak++
                checkCal.add(Calendar.DAY_OF_YEAR, -1)
            } else {
                break
            }
        }

        return DevotionalStats(
            currentStreak = currStreak,
            longestStreak = maxStreak,
            totalDevotions = total
        )
    }
}
