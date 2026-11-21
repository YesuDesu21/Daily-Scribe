package com.example.dailyscribe.presentation.journal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Notes
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.dailyscribe.domain.model.JournalEntry
import com.example.dailyscribe.presentation.journal.components.DigitalJournalEditor
import com.example.dailyscribe.presentation.journal.components.MarkdownText
import com.example.dailyscribe.presentation.journal.components.OcrCameraScreen
import com.example.dailyscribe.presentation.journal.components.OcrCorrectionDialog
import com.example.dailyscribe.ui.theme.DailyScribeTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class JournalScreenMode {
    LIST,
    DIGITAL_EDITOR,
    OCR_CAMERA
}

@Composable
fun JournalScreen(
    viewModel: JournalViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val entries by viewModel.entries.collectAsState()
    val draftEntry by viewModel.draftEntry.collectAsState()
    val isAutoSaving by viewModel.isAutoSaving.collectAsState()

    JournalScreenContent(
        searchQuery = searchQuery,
        onSearchQueryChange = viewModel::onQueryChanged,
        entries = entries,
        draftEntry = draftEntry,
        isAutoSaving = isAutoSaving,
        onDraftChanged = viewModel::onDraftChanged,
        onSavePermanentEntry = viewModel::savePermanentEntry,
        onDiscardDraft = viewModel::clearDraft,
        onDeleteEntry = viewModel::deleteEntry,
        modifier = modifier
    )
}

@Composable
fun JournalScreenContent(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    entries: List<JournalEntry>,
    draftEntry: JournalEntry?,
    isAutoSaving: Boolean,
    onDraftChanged: (title: String, content: String, tags: List<String>, scriptureRef: String?, mood: String?) -> Unit,
    onSavePermanentEntry: (title: String, content: String, isOcr: Boolean, tags: List<String>, scriptureRef: String?, mood: String?) -> Unit,
    onDiscardDraft: () -> Unit,
    onDeleteEntry: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var screenMode by remember { mutableStateOf(JournalScreenMode.LIST) }
    var ocrExtractedText by remember { mutableStateOf<String?>(null) }
    var selectedEntryForDetail by remember { mutableStateOf<JournalEntry?>(null) }

    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val isExpanded = windowAdaptiveInfo.windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT

    when (screenMode) {
        JournalScreenMode.OCR_CAMERA -> {
            OcrCameraScreen(
                onTextExtracted = { text ->
                    ocrExtractedText = text
                    screenMode = JournalScreenMode.LIST
                },
                onClose = {
                    screenMode = JournalScreenMode.LIST
                },
                modifier = modifier
            )
            return
        }
        JournalScreenMode.DIGITAL_EDITOR -> {
            DigitalJournalEditor(
                draftEntry = draftEntry,
                isAutoSaving = isAutoSaving,
                onDraftChanged = onDraftChanged,
                onSavePermanentEntry = { title, content, tags, scriptureRef, mood ->
                    onSavePermanentEntry(title, content, false, tags, scriptureRef, mood)
                    screenMode = JournalScreenMode.LIST
                },
                onDiscardDraft = onDiscardDraft,
                onClose = { screenMode = JournalScreenMode.LIST },
                modifier = modifier
            )
            return
        }
        JournalScreenMode.LIST -> {
            Scaffold(
                floatingActionButton = {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ExtendedFloatingActionButton(
                            onClick = { screenMode = JournalScreenMode.OCR_CAMERA },
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            icon = { Icon(Icons.Rounded.CameraAlt, contentDescription = null) },
                            text = { Text("Scan Paper Note") }
                        )

                        FloatingActionButton(
                            onClick = { screenMode = JournalScreenMode.DIGITAL_EDITOR },
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ) {
                            Icon(Icons.Rounded.Add, contentDescription = "New Entry")
                        }
                    }
                },
                modifier = modifier
            ) { paddingValues ->
                if (isExpanded) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            JournalSearchAndDraftHeader(
                                searchQuery = searchQuery,
                                onSearchQueryChange = onSearchQueryChange,
                                draftEntry = draftEntry,
                                onOpenEditor = { screenMode = JournalScreenMode.DIGITAL_EDITOR }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            JournalEntriesList(
                                entries = entries,
                                selectedEntryId = selectedEntryForDetail?.id,
                                onSelectEntry = { selectedEntryForDetail = it },
                                onDeleteEntry = onDeleteEntry,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                        ) {
                            val activeEntry = selectedEntryForDetail ?: entries.firstOrNull()
                            if (activeEntry != null) {
                                JournalDetailView(
                                    entry = activeEntry,
                                    onDelete = {
                                        onDeleteEntry(activeEntry.id)
                                        if (selectedEntryForDetail?.id == activeEntry.id) {
                                            selectedEntryForDetail = null
                                        }
                                    }
                                )
                            } else {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Select an entry to view details",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        JournalSearchAndDraftHeader(
                            searchQuery = searchQuery,
                            onSearchQueryChange = onSearchQueryChange,
                            draftEntry = draftEntry,
                            onOpenEditor = { screenMode = JournalScreenMode.DIGITAL_EDITOR }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        JournalEntriesList(
                            entries = entries,
                            selectedEntryId = selectedEntryForDetail?.id,
                            onSelectEntry = { selectedEntryForDetail = it },
                            onDeleteEntry = onDeleteEntry,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }

    ocrExtractedText?.let { text ->
        OcrCorrectionDialog(
            initialText = text,
            onDismiss = { ocrExtractedText = null },
            onRescan = {
                ocrExtractedText = null
                screenMode = JournalScreenMode.OCR_CAMERA
            },
            onSaveEntry = { title, content, tags, scriptureRef, mood ->
                onSavePermanentEntry(title, content, true, tags, scriptureRef, mood)
                ocrExtractedText = null
            }
        )
    }
}

@Composable
fun JournalSearchAndDraftHeader(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    draftEntry: JournalEntry?,
    onOpenEditor: () -> Unit
) {
    Column {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search journal entries...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search"
                )
            },
            shape = RoundedCornerShape(24.dp),
            singleLine = true
        )

        AnimatedVisibility(visible = draftEntry != null && (draftEntry.title.isNotBlank() || draftEntry.content.isNotBlank())) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .clickable { onOpenEditor() },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.EditNote,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Unsaved Draft: ${draftEntry?.title?.ifBlank { "Untitled" }}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Text(
                                text = "Tap to resume editing",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JournalEntriesList(
    entries: List<JournalEntry>,
    selectedEntryId: String?,
    onSelectEntry: (JournalEntry) -> Unit,
    onDeleteEntry: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (entries.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Notes,
                    contentDescription = null,
                    modifier = Modifier.padding(16.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = "No Journal Entries Yet",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = "Tap + to write a digital entry or scan paper notes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = modifier
        ) {
            items(entries, key = { it.id }) { entry ->
                JournalEntryCard(
                    entry = entry,
                    isSelected = entry.id == selectedEntryId,
                    onSelect = { onSelectEntry(entry) },
                    onDelete = { onDeleteEntry(entry.id) }
                )
            }
        }
    }
}

@Composable
fun JournalEntryCard(
    entry: JournalEntry,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showShareMenu by remember { mutableStateOf(false) }

    val formattedDate = remember(entry.createdAt) {
        SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault()).format(Date(entry.createdAt))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = entry.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )

                if (entry.isOcr) {
                    AssistChip(
                        onClick = { },
                        label = { Text("OCR Paper") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.CameraAlt,
                                contentDescription = "Scanned entry"
                            )
                        }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }

                Box {
                    IconButton(onClick = { showShareMenu = true }) {
                        Icon(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    DropdownMenu(
                        expanded = showShareMenu,
                        onDismissRequest = { showShareMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Share Verbatim Text") },
                            leadingIcon = { Icon(Icons.Rounded.Share, contentDescription = null) },
                            onClick = {
                                showShareMenu = false
                                SocialShareHelper.shareEntry(context, entry, ShareMode.VERBATIM)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Share AI Summary Quote") },
                            leadingIcon = { Icon(Icons.Rounded.AutoAwesome, contentDescription = null) },
                            onClick = {
                                showShareMenu = false
                                SocialShareHelper.shareEntry(context, entry, ShareMode.AI_SUMMARY)
                            }
                        )
                    }
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Delete entry",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Text(
                text = formattedDate,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = entry.content.take(180) + if (entry.content.length > 180) "..." else "",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
            )

            AnimatedVisibility(visible = entry.tags.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    entry.tags.forEach { tag ->
                        AssistChip(
                            onClick = { },
                            label = { Text(tag) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Tag,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JournalDetailView(
    entry: JournalEntry,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showShareMenu by remember { mutableStateOf(false) }

    val formattedDate = remember(entry.createdAt) {
        SimpleDateFormat("EEEE, MMMM dd, yyyy • hh:mm a", Locale.getDefault()).format(Date(entry.createdAt))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = entry.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )

            Box {
                IconButton(onClick = { showShareMenu = true }) {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                DropdownMenu(
                    expanded = showShareMenu,
                    onDismissRequest = { showShareMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Share Verbatim Text") },
                        leadingIcon = { Icon(Icons.Rounded.Share, contentDescription = null) },
                        onClick = {
                            showShareMenu = false
                            SocialShareHelper.shareEntry(context, entry, ShareMode.VERBATIM)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Share AI Summary Quote") },
                        leadingIcon = { Icon(Icons.Rounded.AutoAwesome, contentDescription = null) },
                        onClick = {
                            showShareMenu = false
                            SocialShareHelper.shareEntry(context, entry, ShareMode.AI_SUMMARY)
                        }
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = "Delete entry",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        Text(
            text = formattedDate,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline
        )

        entry.scriptureRef?.let { script ->
            Text(
                text = "Scripture: $script",
                style = MaterialTheme.typography.titleSmall,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        MarkdownText(markdown = entry.content)

        Spacer(modifier = Modifier.height(16.dp))

        if (entry.tags.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                entry.tags.forEach { tag ->
                    AssistChip(
                        onClick = { },
                        label = { Text(tag) },
                        leadingIcon = { Icon(Icons.Rounded.Tag, contentDescription = null) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JournalScreenPreview() {
    DailyScribeTheme {
        JournalScreenContent(
            searchQuery = "",
            onSearchQueryChange = {},
            entries = listOf(
                JournalEntry(
                    id = "1",
                    title = "Morning Gratitude",
                    content = "# Peace in Prayer\n> \"The Lord is my shepherd.\"\n- Count your blessings daily.",
                    isOcr = false,
                    tags = listOf("Prayer", "Gratitude")
                ),
                JournalEntry(
                    id = "2",
                    title = "Scanned Sermon Notes",
                    content = "Walking in faith and trust during quiet times.",
                    isOcr = true,
                    tags = listOf("Sermon", "OCR")
                )
            ),
            draftEntry = null,
            isAutoSaving = false,
            onDraftChanged = { _, _, _, _, _ -> },
            onSavePermanentEntry = { _, _, _, _, _, _ -> },
            onDiscardDraft = {},
            onDeleteEntry = {}
        )
    }
}
