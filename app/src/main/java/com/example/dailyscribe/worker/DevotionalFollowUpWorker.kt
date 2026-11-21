package com.example.dailyscribe.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.dailyscribe.MainActivity
import com.example.dailyscribe.data.local.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class DevotionalFollowUpWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val context = applicationContext
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        // Quiet hours: 12:00 AM onwards (suppressed from 00:00 to 06:00)
        if (hour >= 0 && hour < 6) {
            return@withContext Result.success()
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = dateFormat.format(Date())

        val db = DatabaseProvider.getDatabase(context)
        val devotionalDao = db.devotionalDao()
        val settingsDao = db.reminderSettingsDao()

        val log = devotionalDao.getLogForDateSync(today)
        val settings = settingsDao.getSettingsSync()

        val targetDevotions = settings?.targetDevotionsPerDay ?: 2
        val maxFollowUps = settings?.maxFollowUps ?: 2
        val currentFollowUpCount = settings?.followUpCount ?: 0

        val completed = log?.devotionsCompleted ?: 0

        if (completed >= targetDevotions) {
            if (currentFollowUpCount > 0 && settings != null) {
                settingsDao.insertOrUpdate(settings.copy(followUpCount = 0))
            }
            return@withContext Result.success()
        }

        if (currentFollowUpCount < maxFollowUps) {
            showFollowUpNotification(
                context,
                "Daily Scribe Follow-Up",
                "You've completed $completed of $targetDevotions devotions today. Take a few minutes to connect!"
            )

            if (settings != null) {
                val updatedSettings = settings.copy(followUpCount = currentFollowUpCount + 1)
                settingsDao.insertOrUpdate(updatedSettings)

                if (updatedSettings.followUpCount < maxFollowUps) {
                    val randomDelayHours = Random.nextLong(2, 5)
                    val nextFollowUpRequest = OneTimeWorkRequestBuilder<DevotionalFollowUpWorker>()
                        .setInitialDelay(randomDelayHours, TimeUnit.HOURS)
                        .build()
                    WorkManager.getInstance(context).enqueue(nextFollowUpRequest)
                }
            }
        }

        Result.success()
    }

    private fun showFollowUpNotification(context: Context, title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "daily_scribe_reminders"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Devotional Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders for daily devotions and follow-ups"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt() + 1, notification)
    }
}
