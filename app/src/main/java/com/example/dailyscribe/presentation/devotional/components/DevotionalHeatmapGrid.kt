package com.example.dailyscribe.presentation.devotional.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailyscribe.domain.model.DevotionalLog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class HeatmapDay(
    val dateString: String,
    val devotionsCount: Int,
    val isToday: Boolean,
    val monthName: String,
    val dayOfWeek: Int
)

@Composable
fun DevotionalHeatmapGrid(
    logs: List<DevotionalLog>,
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    weeksCount: Int = 16
) {
    val logMap = remember(logs) {
        logs.associate { it.date to it.devotionsCompleted }
    }

    val dateFormat = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            timeZone = TimeZone.getDefault()
        }
    }

    val monthFormat = remember {
        SimpleDateFormat("MMM", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }
    }

    val todayStr = remember { dateFormat.format(Date()) }

    val heatmapData = remember(logMap, todayStr, weeksCount) {
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        val currentDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1
        cal.add(Calendar.DAY_OF_YEAR, -currentDayOfWeek)
        cal.add(Calendar.WEEK_OF_YEAR, -(weeksCount - 1))

        val weeks = mutableListOf<List<HeatmapDay>>()
        for (w in 0 until weeksCount) {
            val weekDays = mutableListOf<HeatmapDay>()
            for (d in 0 until 7) {
                val dStr = dateFormat.format(cal.time)
                val count = logMap[dStr] ?: 0
                val mName = if (d == 0 || cal.get(Calendar.DAY_OF_MONTH) == 1) {
                    monthFormat.format(cal.time)
                } else ""

                weekDays.add(
                    HeatmapDay(
                        dateString = dStr,
                        devotionsCount = count,
                        isToday = dStr == todayStr,
                        monthName = mName,
                        dayOfWeek = d
                    )
                )
                cal.add(Calendar.DAY_OF_YEAR, 1)
            }
            weeks.add(weekDays)
        }
        weeks
    }

    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Activity Heatmap",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.horizontalScroll(scrollState)) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(end = 8.dp, top = 20.dp)
                ) {
                    val dayLabels = listOf("S", "M", "T", "W", "T", "F", "S")
                    dayLabels.forEach { label ->
                        Box(
                            modifier = Modifier.size(18.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }

                Column {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.height(20.dp)
                    ) {
                        heatmapData.forEach { week ->
                            val firstDayMonth = week.firstOrNull { it.monthName.isNotEmpty() }?.monthName ?: ""
                            Box(
                                modifier = Modifier.width(18.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (firstDayMonth.isNotEmpty()) {
                                    Text(
                                        text = firstDayMonth,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        heatmapData.forEach { week ->
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                week.forEach { day ->
                                    val isSelected = day.dateString == selectedDate
                                    val color = getCellColor(day.devotionsCount)

                                    Box(
                                        modifier = Modifier
                                            .size(18.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(color)
                                            .then(
                                                if (isSelected) {
                                                    Modifier.border(
                                                        width = 2.dp,
                                                        color = MaterialTheme.colorScheme.primary,
                                                        shape = RoundedCornerShape(4.dp)
                                                    )
                                                } else if (day.isToday) {
                                                    Modifier.border(
                                                        width = 1.dp,
                                                        color = MaterialTheme.colorScheme.secondary,
                                                        shape = RoundedCornerShape(4.dp)
                                                    )
                                                } else {
                                                    Modifier
                                                }
                                            )
                                            .clickable { onDateSelected(day.dateString) }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Less",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.width(4.dp))
                listOf(0, 1, 2, 3).forEach { count ->
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(getCellColor(count))
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                }
                Spacer(modifier = Modifier.width(1.dp))
                Text(
                    text = "More",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
private fun getCellColor(count: Int): Color {
    return when {
        count == 0 -> MaterialTheme.colorScheme.surfaceVariant
        count == 1 -> MaterialTheme.colorScheme.primaryContainer
        count == 2 -> MaterialTheme.colorScheme.primary.copy(alpha = 0.65f)
        else -> MaterialTheme.colorScheme.primary
    }
}
