package com.myjournalapp.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.myjournalapp.data.preferences.UserPreferencesRepository


data class OnboardingUiState(
    val currentStep: Int = 0,
    val selectedInterests: Set<String> = emptySet(),
    val selectedGoals: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun onNextStep() {
        _uiState.update { it.copy(currentStep = it.currentStep + 1) }
    }

    fun onPreviousStep() {
        _uiState.update { it.copy(currentStep = it.currentStep - 1) }
    }

    fun toggleInterest(interest: String) {
        _uiState.update {
            val newInterests = if (it.selectedInterests.contains(interest)) {
                it.selectedInterests - interest
            } else {
                it.selectedInterests + interest
            }
            it.copy(selectedInterests = newInterests)
        }
    }

    fun toggleGoal(goal: String) {
        _uiState.update {
            val newGoals = if (it.selectedGoals.contains(goal)) {
                it.selectedGoals - goal
            } else {
                it.selectedGoals + goal
            }
            it.copy(selectedGoals = newGoals)
        }
    }

    fun completeOnboarding(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1000) // Simulate network call or data saving
            // In a real app, save selectedInterests and selectedGoals

            userPreferencesRepository.setOnboardingCompleted(true) // Set onboarding completed flag

            _uiState.update { it.copy(isLoading = false) }
            onSuccess()
        }
    }
}