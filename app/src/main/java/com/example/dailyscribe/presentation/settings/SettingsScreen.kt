package com.example.dailyscribe.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Alarm
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dailyscribe.domain.model.ReminderSettings
import com.example.dailyscribe.ui.theme.DailyScribeTheme

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settings.collectAsState()

    SettingsScreenContent(
        settings = settings,
        onUpdateTargetDevotions = viewModel::updateTargetDevotions,
        onUpdateMaxFollowUps = viewModel::updateMaxFollowUps,
        onAddPreferredTime = viewModel::addPreferredTime,
        onRemovePreferredTime = viewModel::removePreferredTime,
        modifier = modifier
    )
}

@Composable
fun SettingsScreenContent(
    settings: ReminderSettings,
    onUpdateTargetDevotions: (Int) -> Unit,
    onUpdateMaxFollowUps: (Int) -> Unit,
    onAddPreferredTime: (String) -> Unit,
    onRemovePreferredTime: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showTimeDialog by remember { mutableStateOf(false) }

    Scaffold(modifier = modifier) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Settings & Security",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Security Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Security,
                        contentDescription = "Encrypted Storage",
                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Column {
                        Text(
                            text = "SQLCipher Database Encryption",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "All journal reflections and logs are encrypted at rest with AES-256 master key.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Devotional Goals Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Devotional Goals & Reminders",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Target Devotions / Day",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { if (settings.targetDevotionsPerDay > 1) onUpdateTargetDevotions(settings.targetDevotionsPerDay - 1) }) {
                                Text("-", style = MaterialTheme.typography.titleLarge)
                            }
                            Text(
                                text = "${settings.targetDevotionsPerDay}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = { onUpdateTargetDevotions(settings.targetDevotionsPerDay + 1) }) {
                                Text("+", style = MaterialTheme.typography.titleLarge)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Max Follow-Up Reminders",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { if (settings.maxFollowUps > 0) onUpdateMaxFollowUps(settings.maxFollowUps - 1) }) {
                                Text("-", style = MaterialTheme.typography.titleLarge)
                            }
                            Text(
                                text = "${settings.maxFollowUps}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = { onUpdateMaxFollowUps(settings.maxFollowUps + 1) }) {
                                Text("+", style = MaterialTheme.typography.titleLarge)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Preferred Reminder Times",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(settings.preferredTimes) { time ->
                            AssistChip(
                                onClick = { },
                                label = { Text(time) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Alarm,
                                        contentDescription = null
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = { onRemovePreferredTime(time) }) {
                                        Icon(
                                            imageVector = Icons.Rounded.Close,
                                            contentDescription = "Remove time"
                                        )
                                    }
                                }
                            )
                        }
                        item {
                            IconButton(onClick = { showTimeDialog = true }) {
                                Icon(
                                    imageVector = Icons.Rounded.Add,
                                    contentDescription = "Add Preferred Time"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showTimeDialog) {
        AddTimeDialog(
            onDismiss = { showTimeDialog = false },
            onConfirm = { time ->
                onAddPreferredTime(time)
                showTimeDialog = false
            }
        )
    }
}

@Composable
fun AddTimeDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var timeInput by remember { mutableStateOf("08:00") }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Preferred Time") },
        text = {
            OutlinedTextField(
                value = timeInput,
                onValueChange = { timeInput = it },
                label = { Text("Time (e.g., 08:00 or 20:00)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (timeInput.isNotBlank()) {
                        onConfirm(timeInput)
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    DailyScribeTheme {
        SettingsScreenContent(
            settings = ReminderSettings(
                preferredTimes = listOf("08:00", "20:00"),
                followUpCount = 1,
                maxFollowUps = 3,
                targetDevotionsPerDay = 1
            ),
            onUpdateTargetDevotions = {},
            onUpdateMaxFollowUps = {},
            onAddPreferredTime = {},
            onRemovePreferredTime = {}
        )
    }
}
