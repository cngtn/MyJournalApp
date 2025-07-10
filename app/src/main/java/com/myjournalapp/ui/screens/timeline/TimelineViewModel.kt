package com.myjournalapp.ui.screens.timeline

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class TimelineUiState(
    val entries: List<String> = emptyList(),
    val searchQuery: String = "",
    val filteredEntries: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class TimelineViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(TimelineUiState())
    val uiState: StateFlow<TimelineUiState> = _uiState.asStateFlow()

    init {
        // Simulate loading entries
        _uiState.value = _uiState.value.copy(
            entries = listOf("Entry 1 (2025-07-01)", "Entry 2 (2025-07-01)", "Entry 3 (2025-07-02)"),
            filteredEntries = listOf("Entry 1 (2025-07-01)", "Entry 2 (2025-07-01)", "Entry 3 (2025-07-02)")
        )
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            filteredEntries = _uiState.value.entries.filter { it.contains(query, ignoreCase = true) }
        )
    }
}
