package com.example.dailyscribe.presentation.devotional

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.dailyscribe.domain.model.DevotionalLog
import com.example.dailyscribe.domain.model.DevotionalStats
import com.example.dailyscribe.presentation.devotional.components.DevotionalHeatmapGrid
import com.example.dailyscribe.ui.theme.DailyScribeTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DevotionalScreen(
    viewModel: DevotionalViewModel,
    modifier: Modifier = Modifier
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val stats by viewModel.devotionalStats.collectAsState()
    val selectedLog by viewModel.selectedDevotionalLog.collectAsState()
    val allLogs by viewModel.allLogs.collectAsState()

    DevotionalScreenContent(
        selectedDate = selectedDate,
        todayDate = viewModel.todayDateString,
        stats = stats,
        selectedLog = selectedLog,
        allLogs = allLogs,
        onDateSelected = viewModel::selectDate,
        onRecordDevotion = { viewModel.recordDevotionCompleted() },
        modifier = modifier
    )
}

@Composable
fun DevotionalScreenContent(
    selectedDate: String,
    todayDate: String,
    stats: DevotionalStats,
    selectedLog: DevotionalLog?,
    allLogs: List<DevotionalLog>,
    onDateSelected: (String) -> Unit,
    onRecordDevotion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val isExpanded = windowAdaptiveInfo.windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT

    Scaffold(modifier = modifier) { paddingValues ->
        if (isExpanded) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1.2f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HeaderSection(selectedDate = selectedDate, todayDate = todayDate)
                    DevotionalStatsRow(stats = stats)
                    DevotionalHeatmapGrid(
                        logs = allLogs,
                        selectedDate = selectedDate,
                        onDateSelected = onDateSelected
                    )
                }
                Column(
                    modifier = Modifier.weight(0.8f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SelectedDateProgressCard(
                        selectedDate = selectedDate,
                        todayDate = todayDate,
                        selectedLog = selectedLog,
                        onRecordDevotion = onRecordDevotion
                    )
                    TimestampsLogList(selectedLog = selectedLog, modifier = Modifier.weight(1f))
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HeaderSection(selectedDate = selectedDate, todayDate = todayDate)
                DevotionalStatsRow(stats = stats)
                DevotionalHeatmapGrid(
                    logs = allLogs,
                    selectedDate = selectedDate,
                    onDateSelected = onDateSelected
                )
                SelectedDateProgressCard(
                    selectedDate = selectedDate,
                    todayDate = todayDate,
                    selectedLog = selectedLog,
                    onRecordDevotion = onRecordDevotion
                )
                TimestampsLogList(selectedLog = selectedLog, modifier = Modifier.height(200.dp))
            }
        }
    }
}

@Composable
private fun HeaderSection(selectedDate: String, todayDate: String) {
    Column {
        Text(
            text = "Devotional Tracker",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = if (selectedDate == todayDate) "Today ($todayDate)" else "Selected Date: $selectedDate",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun DevotionalStatsRow(
    stats: DevotionalStats,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            title = "Current Streak",
            value = "${stats.currentStreak} Days",
            icon = Icons.Rounded.LocalFireDepartment,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "Longest Streak",
            value = "${stats.longestStreak} Days",
            icon = Icons.Rounded.EmojiEvents,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "Total Devotions",
            value = "${stats.totalDevotions}",
            icon = Icons.Rounded.TaskAlt,
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    tint: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = tint
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun SelectedDateProgressCard(
    selectedDate: String,
    todayDate: String,
    selectedLog: DevotionalLog?,
    onRecordDevotion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val completed = selectedLog?.devotionsCompleted ?: 0
    val target = selectedLog?.targetDevotions ?: 1
    val progress = (completed.toFloat() / target.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "ProgressAnim")

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (selectedDate == todayDate) "Today's Devotion Goal" else "Goal for $selectedDate",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "$completed / $target Completed",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onRecordDevotion,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Record Devotion Completed")
            }
        }
    }
}

@Composable
fun TimestampsLogList(
    selectedLog: DevotionalLog?,
    modifier: Modifier = Modifier
) {
    val timestamps = selectedLog?.completionTimestamps ?: emptyList()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Completion Logs",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (timestamps.isEmpty()) {
                Text(
                    text = "No devotions logged for this date.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(timestamps) { time ->
                        val formattedTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(time))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Schedule,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = "Logged at $formattedTime",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DevotionalScreenPreview() {
    DailyScribeTheme {
        DevotionalScreenContent(
            selectedDate = "2025-05-20",
            todayDate = "2025-05-20",
            stats = DevotionalStats(
                currentStreak = 7,
                longestStreak = 14,
                totalDevotions = 32
            ),
            selectedLog = DevotionalLog(
                id = "1",
                date = "2025-05-20",
                devotionsCompleted = 1,
                targetDevotions = 2,
                streakCount = 7,
                completionTimestamps = listOf(System.currentTimeMillis())
            ),
            allLogs = emptyList(),
            onDateSelected = {},
            onRecordDevotion = {}
        )
    }
}
