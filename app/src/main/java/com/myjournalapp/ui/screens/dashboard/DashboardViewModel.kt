package com.myjournalapp.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class DashboardUiState(
    val todayMood: String = "",
    val weather: String = "",
    val dailyPrompt: String = "",
    val quickNote: String = "",
    val recentPhotos: List<String> = emptyList(),
    val currentChallenge: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        // Simulate loading dashboard data
        _uiState.update { it.copy(isLoading = true) }
        // In a real app, fetch data from repository
        _uiState.update {
            it.copy(
                todayMood = "Happy",
                weather = "Sunny, 28°C",
                dailyPrompt = "What made you smile today?",
                quickNote = "Remember to buy groceries.",
                recentPhotos = listOf("photo1.jpg", "photo2.jpg"),
                currentChallenge = "7-Day Gratitude Challenge",
                isLoading = false
            )
        }
    }
}
