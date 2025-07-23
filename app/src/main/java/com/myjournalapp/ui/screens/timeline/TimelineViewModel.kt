package com.myjournalapp.ui.screens.timeline

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject
import com.myjournalapp.data.model.Entry

@HiltViewModel
class TimelineViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(TimelineUiState())
    val uiState: StateFlow<TimelineUiState> = _uiState.asStateFlow()

    init {
        // Load dummy data for now
        _uiState.update { currentState ->
            currentState.copy(
                entries = generateDummyEntries(),
                filteredEntries = generateDummyEntries()
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { currentState ->
            val filtered = if (query.isBlank()) {
                currentState.entries
            } else {
                currentState.entries.filter {
                    it.preview.contains(query, ignoreCase = true) ||
                    it.mood.contains(query, ignoreCase = true)
                }
            }
            currentState.copy(
                searchQuery = query,
                filteredEntries = filtered,
                isSearching = query.isNotBlank()
            )
        }
    }

    private fun generateDummyEntries(): List<Entry> {
        return listOf(
            Entry("1", LocalDate.of(2024, 7, 10), "Happy", "Today was a great day!"),
            Entry("2", LocalDate.of(2024, 7, 10), "Calm", "Meditated for 30 minutes."),
            Entry("3", LocalDate.of(2024, 7, 9), "Sad", "Feeling a bit down today."),
            Entry("4", LocalDate.of(2024, 7, 8), "Excited", "Started a new project!"),
            Entry("5", LocalDate.of(2024, 7, 8), "Neutral", "Just a regular day."),
            Entry("6", LocalDate.of(2024, 7, 7), "Happy", "Met old friends.")
        ).sortedByDescending { it.date }
    }
}

data class TimelineUiState(
    val entries: List<Entry> = emptyList(),
    val searchQuery: String = "",
    val filteredEntries: List<Entry> = emptyList(),
    val isSearching: Boolean = false
)
