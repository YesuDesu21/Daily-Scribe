package com.example.dailyscribe.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.dailyscribe.domain.model.ReminderSettings
import com.example.dailyscribe.receiver.ReminderReceiver
import java.util.Calendar

object ReminderScheduler {

    fun scheduleAlarms(context: Context, settings: ReminderSettings) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        cancelAlarms(context)

        settings.preferredTimes.forEachIndexed { index, timeStr ->
            val parts = timeStr.split(":")
            if (parts.size == 2) {
                val hour = parts[0].toIntOrNull() ?: 8
                val minute = parts[1].toIntOrNull() ?: 0

                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    if (timeInMillis <= System.currentTimeMillis()) {
                        add(Calendar.DAY_OF_YEAR, 1)
                    }
                }

                val intent = Intent(context, ReminderReceiver::class.java).apply {
                    action = "com.example.dailyscribe.ACTION_REMINDER"
                    putExtra("reminder_index", index)
                }

                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    100 + index,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                        alarmManager.setAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            pendingIntent
                        )
                    } else {
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            pendingIntent
                        )
                    }
                } catch (e: SecurityException) {
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            }
        }
    }

    fun cancelAlarms(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        for (index in 0 until 10) {
            val intent = Intent(context, ReminderReceiver::class.java).apply {
                action = "com.example.dailyscribe.ACTION_REMINDER"
                putExtra("reminder_index", index)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                100 + index,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        }
    }
}
