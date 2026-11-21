package com.example.dailyscribe.presentation.main

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Notes
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.dailyscribe.presentation.bible.BibleScreen
import com.example.dailyscribe.presentation.bible.BibleViewModel
import com.example.dailyscribe.presentation.devotional.DevotionalScreen
import com.example.dailyscribe.presentation.devotional.DevotionalViewModel
import com.example.dailyscribe.presentation.journal.JournalScreen
import com.example.dailyscribe.presentation.journal.JournalViewModel
import com.example.dailyscribe.presentation.settings.SettingsScreen
import com.example.dailyscribe.presentation.settings.SettingsViewModel

enum class NavigationDestination(
    val title: String,
    val icon: ImageVector
) {
    JOURNAL("Journal", Icons.AutoMirrored.Rounded.Notes),
    DEVOTIONAL("Devotional", Icons.Rounded.LocalFireDepartment),
    BIBLE("Bible", Icons.Rounded.AutoStories),
    SETTINGS("Settings", Icons.Rounded.Settings)
}

@Composable
fun MainScreen(
    journalViewModel: JournalViewModel,
    devotionalViewModel: DevotionalViewModel,
    bibleViewModel: BibleViewModel,
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    var selectedDestination by remember { mutableStateOf(NavigationDestination.JOURNAL) }
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val isExpanded = windowAdaptiveInfo.windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT

    if (isExpanded) {
        Row(modifier = modifier.fillMaxSize()) {
            NavigationRail {
                NavigationDestination.entries.forEach { destination ->
                    NavigationRailItem(
                        selected = selectedDestination == destination,
                        onClick = { selectedDestination = destination },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.title
                            )
                        },
                        label = { Text(destination.title) }
                    )
                }
            }
            MainScreenContent(
                selectedDestination = selectedDestination,
                journalViewModel = journalViewModel,
                devotionalViewModel = devotionalViewModel,
                bibleViewModel = bibleViewModel,
                settingsViewModel = settingsViewModel,
                modifier = Modifier.weight(1f)
            )
        }
    } else {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    NavigationDestination.entries.forEach { destination ->
                        NavigationBarItem(
                            selected = selectedDestination == destination,
                            onClick = { selectedDestination = destination },
                            icon = {
                                Icon(
                                    imageVector = destination.icon,
                                    contentDescription = destination.title
                                )
                            },
                            label = { Text(destination.title) }
                        )
                    }
                }
            },
            modifier = modifier
        ) { innerPadding ->
            MainScreenContent(
                selectedDestination = selectedDestination,
                journalViewModel = journalViewModel,
                devotionalViewModel = devotionalViewModel,
                bibleViewModel = bibleViewModel,
                settingsViewModel = settingsViewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun MainScreenContent(
    selectedDestination: NavigationDestination,
    journalViewModel: JournalViewModel,
    devotionalViewModel: DevotionalViewModel,
    bibleViewModel: BibleViewModel,
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    when (selectedDestination) {
        NavigationDestination.JOURNAL -> JournalScreen(viewModel = journalViewModel, modifier = modifier)
        NavigationDestination.DEVOTIONAL -> DevotionalScreen(viewModel = devotionalViewModel, modifier = modifier)
        NavigationDestination.BIBLE -> BibleScreen(viewModel = bibleViewModel, modifier = modifier)
        NavigationDestination.SETTINGS -> SettingsScreen(viewModel = settingsViewModel, modifier = modifier)
    }
}
