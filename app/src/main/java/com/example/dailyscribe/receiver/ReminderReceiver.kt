package com.example.dailyscribe.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.dailyscribe.MainActivity
import com.example.dailyscribe.data.local.DatabaseProvider
import com.example.dailyscribe.data.mapper.toDomainModel
import com.example.dailyscribe.util.ReminderScheduler
import com.example.dailyscribe.worker.DevotionalFollowUpWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.dailyscribe.ACTION_REMINDER") {
            showReminderNotification(
                context,
                "Time for Daily Scribe",
                "Take a moment for your daily devotion and reflection."
            )

            // Enqueue WorkManager follow-up check (e.g. check 2 hours later)
            val followUpRequest = OneTimeWorkRequestBuilder<DevotionalFollowUpWorker>()
                .setInitialDelay(2, TimeUnit.HOURS)
                .setInputData(workDataOf("follow_up_index" to 1))
                .build()

            WorkManager.getInstance(context).enqueue(followUpRequest)
        }
    }

    private fun showReminderNotification(context: Context, title: String, message: String) {
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
            0,
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

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
