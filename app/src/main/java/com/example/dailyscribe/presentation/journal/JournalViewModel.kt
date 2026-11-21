package com.example.dailyscribe.presentation.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dailyscribe.domain.model.JournalEntry
import com.example.dailyscribe.domain.repository.JournalRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class JournalViewModel(
    private val journalRepository: JournalRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _isAutoSaving = MutableStateFlow(false)
    val isAutoSaving: StateFlow<Boolean> = _isAutoSaving.asStateFlow()

    val entries: StateFlow<List<JournalEntry>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                journalRepository.getAllEntries()
            } else {
                journalRepository.searchEntries(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val draftEntry: StateFlow<JournalEntry?> = journalRepository.getDraft()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private var autoSaveJob: Job? = null

    fun onQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onDraftChanged(
        title: String,
        content: String,
        tags: List<String>,
        scriptureRef: String?,
        mood: String?
    ) {
        autoSaveJob?.cancel()
        _isAutoSaving.value = true
        autoSaveJob = viewModelScope.launch {
            delay(800L)
            val currentDraft = draftEntry.value
            val updatedDraft = JournalEntry(
                id = currentDraft?.id ?: UUID.randomUUID().toString(),
                title = title,
                content = content,
                isOcr = false,
                isDraft = true,
                createdAt = currentDraft?.createdAt ?: System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                tags = tags,
                scriptureRef = scriptureRef,
                mood = mood
            )
            journalRepository.saveDraft(updatedDraft)
            _isAutoSaving.value = false
        }
    }

    fun savePermanentEntry(
        title: String,
        content: String,
        isOcr: Boolean = false,
        tags: List<String> = emptyList(),
        scriptureRef: String? = null,
        mood: String? = null
    ) {
        viewModelScope.launch {
            autoSaveJob?.cancel()
            val entry = JournalEntry(
                id = UUID.randomUUID().toString(),
                title = title,
                content = content,
                isOcr = isOcr,
                isDraft = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                tags = tags,
                scriptureRef = scriptureRef,
                mood = mood
            )
            journalRepository.saveEntry(entry)
        }
    }

    fun clearDraft() {
        viewModelScope.launch {
            autoSaveJob?.cancel()
            journalRepository.clearDraft()
            _isAutoSaving.value = false
        }
    }

    fun addSampleEntry(title: String, content: String, isOcr: Boolean = false, tags: List<String> = emptyList()) {
        savePermanentEntry(title = title, content = content, isOcr = isOcr, tags = tags)
    }

    fun deleteEntry(id: String) {
        viewModelScope.launch {
            journalRepository.deleteEntry(id)
        }
    }

    class Factory(private val repository: JournalRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return JournalViewModel(repository) as T
        }
    }
}
