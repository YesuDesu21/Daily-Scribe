package com.example.dailyscribe.presentation.journal.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OcrCorrectionDialog(
    initialText: String,
    onDismiss: () -> Unit,
    onRescan: () -> Unit,
    onSaveEntry: (title: String, content: String, tags: List<String>, scriptureRef: String?, mood: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val defaultTitle = remember {
        "OCR Note - " + SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
    }

    var title by remember { mutableStateOf(defaultTitle) }
    var content by remember { mutableStateOf(initialText) }
    var scriptureRef by remember { mutableStateOf("") }
    var tagInput by remember { mutableStateOf("Paper Notes, OCR") }
    var selectedMood by remember { mutableStateOf("Reflective") }

    val moodOptions = listOf("Reflective", "Grateful", "Inspired", "Peaceful", "Joyful", "Prayer")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Review & Edit OCR Text",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "ML Kit extracted text from your paper note. Please review and make any needed corrections.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Extracted Content (Editable)") },
                    minLines = 6,
                    maxLines = 10,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {
                            val cleaned = content.lines()
                                .map { it.trim() }
                                .filter { it.isNotEmpty() }
                                .joinToString(" ")
                            content = cleaned
                        }
                    ) {
                        Text("Clean Line Breaks", style = MaterialTheme.typography.labelMedium)
                    }
                }

                OutlinedTextField(
                    value = scriptureRef,
                    onValueChange = { scriptureRef = it },
                    label = { Text("Scripture Reference (Optional)") },
                    placeholder = { Text("e.g., Psalm 23:1") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = tagInput,
                    onValueChange = { tagInput = it },
                    label = { Text("Tags (comma separated)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Select Mood",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    moodOptions.forEach { mood ->
                        FilterChip(
                            selected = selectedMood == mood,
                            onClick = { selectedMood = mood },
                            label = { Text(mood) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        val tags = tagInput.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        onSaveEntry(
                            title,
                            content,
                            tags,
                            scriptureRef.ifBlank { null },
                            selectedMood.ifBlank { null }
                        )
                    }
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Save to Journal")
            }
        },
        dismissButton = {
            Row {
                OutlinedButton(onClick = onRescan) {
                    Icon(imageVector = Icons.Rounded.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Rescan")
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        },
        modifier = modifier
    )
}
