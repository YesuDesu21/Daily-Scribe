package com.example.dailyscribe.presentation.bible

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dailyscribe.domain.model.BibleVerse
import com.example.dailyscribe.ui.theme.DailyScribeTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun BibleScreen(
    viewModel: BibleViewModel,
    modifier: Modifier = Modifier
) {
    val selectedTranslation by viewModel.selectedTranslation.collectAsState()
    val selectedBook by viewModel.selectedBook.collectAsState()
    val selectedChapter by viewModel.selectedChapter.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val verses by viewModel.verses.collectAsState()
    val books by viewModel.books.collectAsState()
    val chapters by viewModel.chapters.collectAsState()

    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val coroutineScope = rememberCoroutineScope()

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                BibleNavigationPane(
                    books = books,
                    chapters = chapters,
                    selectedBook = selectedBook,
                    selectedChapter = selectedChapter,
                    onSelectBook = { book ->
                        viewModel.selectBook(book)
                        coroutineScope.launch {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                        }
                    },
                    onSelectChapter = { chapter ->
                        viewModel.selectChapter(chapter)
                        coroutineScope.launch {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                        }
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                BibleDetailPane(
                    selectedTranslation = selectedTranslation,
                    selectedBook = selectedBook,
                    selectedChapter = selectedChapter,
                    searchQuery = searchQuery,
                    verses = verses,
                    translations = viewModel.translations,
                    onSelectTranslation = viewModel::selectTranslation,
                    onSearchQueryChange = viewModel::onSearchQueryChanged,
                    onPopulateSample = viewModel::populateSampleVerses,
                    onOpenNavigation = {
                        coroutineScope.launch {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.List)
                        }
                    },
                    showBackButton = navigator.canNavigateBack()
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun BibleNavigationPane(
    books: List<String>,
    chapters: List<Int>,
    selectedBook: String,
    selectedChapter: Int,
    onSelectBook: (String) -> Unit,
    onSelectChapter: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxHeight(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Book,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Scripture Navigation",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Select Book",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(books) { book ->
                    val isSelected = book == selectedBook
                    Card(
                        onClick = { onSelectBook(book) },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = book,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Icon(
                                imageVector = Icons.Rounded.ChevronRight,
                                contentDescription = null,
                                tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                                else MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Chapters in $selectedBook",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(chapters) { chapter ->
                    val isSelected = chapter == selectedChapter
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectChapter(chapter) },
                        label = { Text("Ch $chapter") }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibleDetailPane(
    selectedTranslation: String,
    selectedBook: String,
    selectedChapter: Int,
    searchQuery: String,
    verses: List<BibleVerse>,
    translations: List<String>,
    onSelectTranslation: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onPopulateSample: () -> Unit,
    onOpenNavigation: () -> Unit,
    showBackButton: Boolean,
    modifier: Modifier = Modifier
) {
    var translationExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onOpenNavigation() }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AutoStories,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$selectedBook $selectedChapter",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = onOpenNavigation) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Back to Navigation"
                            )
                        }
                    }
                },
                actions = {
                    Box {
                        OutlinedButton(
                            onClick = { translationExpanded = true },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(selectedTranslation)
                        }
                        DropdownMenu(
                            expanded = translationExpanded,
                            onDismissRequest = { translationExpanded = false }
                        ) {
                            translations.forEach { t ->
                                DropdownMenuItem(
                                    text = { Text(t) },
                                    onClick = {
                                        onSelectTranslation(t)
                                        translationExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search Scripture in $selectedTranslation...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Search"
                    )
                },
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (verses.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Rounded.AutoStories,
                            contentDescription = null,
                            modifier = Modifier.padding(16.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = "No Verses Found",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = onPopulateSample) {
                            Text("Load Sample Scriptures")
                        }
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(verses, key = { it.id }) { verse ->
                        BibleVerseItem(verse = verse)
                    }
                }
            }
        }
    }
}

@Composable
fun BibleVerseItem(
    verse: BibleVerse,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "${verse.verse}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.width(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = verse.text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BibleScreenPreview() {
    DailyScribeTheme {
        BibleDetailPane(
            selectedTranslation = "BSB",
            selectedBook = "John",
            selectedChapter = 1,
            searchQuery = "",
            verses = listOf(
                BibleVerse("1", "BSB", "John", 1, 1, "In the beginning was the Word, and the Word was with God, and the Word was God."),
                BibleVerse("2", "BSB", "John", 1, 2, "He was with God in the beginning.")
            ),
            translations = listOf("BSB", "WEB", "KJV", "NIV"),
            onSelectTranslation = {},
            onSearchQueryChange = {},
            onPopulateSample = {},
            onOpenNavigation = {},
            showBackButton = false
        )
    }
}
