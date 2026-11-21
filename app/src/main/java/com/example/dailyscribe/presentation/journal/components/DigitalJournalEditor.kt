package com.example.dailyscribe.presentation.journal.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.FormatBold
import androidx.compose.material.icons.rounded.FormatItalic
import androidx.compose.material.icons.rounded.FormatListBulleted
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Title
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailyscribe.domain.model.JournalEntry

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DigitalJournalEditor(
    draftEntry: JournalEntry?,
    isAutoSaving: Boolean,
    onDraftChanged: (title: String, content: String, tags: List<String>, scriptureRef: String?, mood: String?) -> Unit,
    onSavePermanentEntry: (title: String, content: String, tags: List<String>, scriptureRef: String?, mood: String?) -> Unit,
    onDiscardDraft: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember(draftEntry) { mutableStateOf(draftEntry?.title ?: "") }
    var content by remember(draftEntry) { mutableStateOf(draftEntry?.content ?: "") }
    var scriptureRef by remember(draftEntry) { mutableStateOf(draftEntry?.scriptureRef ?: "") }
    var tagInput by remember(draftEntry) { mutableStateOf(draftEntry?.tags?.joinToString(", ") ?: "") }
    var selectedMood by remember(draftEntry) { mutableStateOf(draftEntry?.mood ?: "Reflective") }
    var selectedTab by remember { mutableIntStateOf(0) }

    val moodOptions = listOf("Reflective", "Grateful", "Inspired", "Peaceful", "Joyful", "Prayer")

    LaunchedEffect(title, content, scriptureRef, tagInput, selectedMood) {
        if (title.isNotBlank() || content.isNotBlank()) {
            val tags = tagInput.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            onDraftChanged(title, content, tags, scriptureRef.ifBlank { null }, selectedMood)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Digital Journal Editor",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(8.dp))
                if (isAutoSaving) {
                    Text(
                        text = "Saving...",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.height(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Draft saved",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }

            Row {
                TextButton(onClick = onDiscardDraft) {
                    Text("Clear")
                }
                TextButton(
                    onClick = {
                        if (title.isNotBlank() && content.isNotBlank()) {
                            val tags = tagInput.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                            onSavePermanentEntry(title, content, tags, scriptureRef.ifBlank { null }, selectedMood)
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Rounded.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Save")
                }
                TextButton(onClick = onClose) {
                    Text("Close")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        SecondaryTabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Write / Edit")
                    }
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Rounded.Visibility, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Markdown Preview")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (selectedTab == 0) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { content += " **Bold Text** " }) {
                        Icon(imageVector = Icons.Rounded.FormatBold, contentDescription = "Bold")
                    }
                    IconButton(onClick = { content += " *Italic Text* " }) {
                        Icon(imageVector = Icons.Rounded.FormatItalic, contentDescription = "Italic")
                    }
                    IconButton(onClick = { content += "\n# Heading\n" }) {
                        Icon(imageVector = Icons.Rounded.Title, contentDescription = "Heading")
                    }
                    IconButton(onClick = { content += "\n- Bullet item\n" }) {
                        Icon(imageVector = Icons.Rounded.FormatListBulleted, contentDescription = "List")
                    }
                    IconButton(onClick = { content += "\n> Quote\n" }) {
                        Icon(imageVector = Icons.Rounded.FormatQuote, contentDescription = "Quote")
                    }
                }

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Journal Content (Markdown Supported)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = scriptureRef,
                    onValueChange = { scriptureRef = it },
                    label = { Text("Scripture Reference (e.g. John 3:16)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = tagInput,
                    onValueChange = { tagInput = it },
                    label = { Text("Tags (comma separated)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Text(
                    text = "Mood / Feeling",
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
        } else {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = title.ifBlank { "Untitled Reflection" },
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    AnimatedVisibility(visible = scriptureRef.isNotBlank()) {
                        Text(
                            text = "Scripture: $scriptureRef",
                            style = MaterialTheme.typography.titleSmall,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    MarkdownText(markdown = content.ifBlank { "*No content written yet.*" })
                }
            }
        }
    }
}

@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier
) {
    val lines = markdown.lines()
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        lines.forEach { line ->
            when {
                line.startsWith("# ") -> {
                    Text(
                        text = line.removePrefix("# "),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                line.startsWith("## ") -> {
                    Text(
                        text = line.removePrefix("## "),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                line.startsWith("> ") -> {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = line.removePrefix("> "),
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
                line.startsWith("- ") -> {
                    Row(verticalAlignment = Alignment.Top) {
                        Text("• ", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(text = line.removePrefix("- "), style = MaterialTheme.typography.bodyMedium)
                    }
                }
                else -> {
                    Text(
                        text = line,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
