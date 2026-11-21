package com.example.dailyscribe.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.dailyscribe.data.local.DatabaseProvider
import com.example.dailyscribe.data.mapper.toDomainModel
import com.example.dailyscribe.domain.model.ReminderSettings
import com.example.dailyscribe.util.ReminderScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == "android.intent.action.QUICKBOOT_POWERON") {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = DatabaseProvider.getDatabase(context)
                    val settingsEntity = db.reminderSettingsDao().getSettingsSync()
                    val settings = settingsEntity?.toDomainModel() ?: ReminderSettings()
                    ReminderScheduler.scheduleAlarms(context, settings)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
