package com.example.dailyscribe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dailyscribe.data.local.DatabaseProvider
import com.example.dailyscribe.data.mapper.toDomainModel
import com.example.dailyscribe.data.repository.BibleRepositoryImpl
import com.example.dailyscribe.data.repository.DevotionalRepositoryImpl
import com.example.dailyscribe.data.repository.JournalRepositoryImpl
import com.example.dailyscribe.data.repository.SettingsRepositoryImpl
import com.example.dailyscribe.domain.model.ReminderSettings
import com.example.dailyscribe.presentation.bible.BibleViewModel
import com.example.dailyscribe.presentation.devotional.DevotionalViewModel
import com.example.dailyscribe.presentation.journal.JournalViewModel
import com.example.dailyscribe.presentation.main.MainScreen
import com.example.dailyscribe.presentation.settings.SettingsViewModel
import com.example.dailyscribe.ui.theme.DailyScribeTheme
import com.example.dailyscribe.util.ReminderScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = DatabaseProvider.getDatabase(applicationContext)
        val journalRepo = JournalRepositoryImpl(db.journalDao())
        val devotionalRepo = DevotionalRepositoryImpl(db.devotionalDao())
        val bibleRepo = BibleRepositoryImpl(db.bibleDao())
        val settingsRepo = SettingsRepositoryImpl(db.reminderSettingsDao())

        // Initialize and schedule initial reminders
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val settingsEntity = db.reminderSettingsDao().getSettingsSync()
                val settings = settingsEntity?.toDomainModel() ?: ReminderSettings()
                ReminderScheduler.scheduleAlarms(applicationContext, settings)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        setContent {
            DailyScribeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val journalViewModel: JournalViewModel = viewModel(
                        factory = JournalViewModel.Factory(journalRepo)
                    )
                    val devotionalViewModel: DevotionalViewModel = viewModel(
                        factory = DevotionalViewModel.Factory(devotionalRepo)
                    )
                    val bibleViewModel: BibleViewModel = viewModel(
                        factory = BibleViewModel.Factory(bibleRepo)
                    )
                    val settingsViewModel: SettingsViewModel = viewModel(
                        factory = SettingsViewModel.Factory(application, settingsRepo)
                    )

                    MainScreen(
                        journalViewModel = journalViewModel,
                        devotionalViewModel = devotionalViewModel,
                        bibleViewModel = bibleViewModel,
                        settingsViewModel = settingsViewModel
                    )
                }
            }
        }
    }
}
