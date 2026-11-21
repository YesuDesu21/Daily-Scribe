package com.example.dailyscribe.presentation.bible

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dailyscribe.data.local.BibleSeeder
import com.example.dailyscribe.domain.model.BibleVerse
import com.example.dailyscribe.domain.repository.BibleRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class BibleViewModel(
    private val bibleRepository: BibleRepository
) : ViewModel() {

    private val _selectedTranslation = MutableStateFlow("BSB")
    val selectedTranslation: StateFlow<String> = _selectedTranslation.asStateFlow()

    private val _selectedBook = MutableStateFlow("John")
    val selectedBook: StateFlow<String> = _selectedBook.asStateFlow()

    private val _selectedChapter = MutableStateFlow(1)
    val selectedChapter: StateFlow<Int> = _selectedChapter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val translations = listOf("BSB", "WEB", "KJV", "NIV")

    val books: StateFlow<List<String>> = _selectedTranslation
        .flatMapLatest { translation ->
            bibleRepository.getBooks(translation)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf("Genesis", "Psalms", "Matthew", "John", "Romans")
        )

    val chapters: StateFlow<List<Int>> = combine(
        _selectedTranslation,
        _selectedBook
    ) { translation, book ->
        Pair(translation, book)
    }.flatMapLatest { (translation, book) ->
        bibleRepository.getChapters(translation, book)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf(1)
    )

    val verses: StateFlow<List<BibleVerse>> = combine(
        _selectedTranslation,
        _selectedBook,
        _selectedChapter,
        _searchQuery
    ) { translation, book, chapter, query ->
        Quadruple(translation, book, chapter, query)
    }.flatMapLatest { (translation, book, chapter, query) ->
        if (query.isBlank()) {
            bibleRepository.getVerses(translation, book, chapter)
        } else {
            bibleRepository.searchVerses(translation, query)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            populateSampleVerses()
        }
    }

    fun selectTranslation(translation: String) {
        _selectedTranslation.value = translation
    }

    fun selectBook(book: String) {
        _selectedBook.value = book
        _selectedChapter.value = 1
    }

    fun selectChapter(chapter: Int) {
        _selectedChapter.value = chapter
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun populateSampleVerses() {
        viewModelScope.launch {
            val sampleEntities = BibleSeeder.getInitialVerses()
            val domainVerses = sampleEntities.map { entity ->
                BibleVerse(
                    id = entity.id,
                    translationId = entity.translationId,
                    book = entity.book,
                    chapter = entity.chapter,
                    verse = entity.verse,
                    text = entity.text
                )
            }
            bibleRepository.insertVerses(domainVerses)
        }
    }

    class Factory(private val repository: BibleRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BibleViewModel(repository) as T
        }
    }

    private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
}
