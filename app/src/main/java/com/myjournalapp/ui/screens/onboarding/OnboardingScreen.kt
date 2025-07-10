package com.myjournalapp.ui.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myjournalapp.R
import com.myjournalapp.ui.components.LogoHeader
import com.myjournalapp.ui.components.buttons.PrimaryButton
import com.myjournalapp.ui.components.buttons.SecondaryButton
import com.myjournalapp.ui.components.cards.OnboardingStepCard
import com.myjournalapp.ui.components.chips.SelectableInterestChip

@OptIn(ExperimentalAnimationApi::class, ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    onCompleted: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LogoHeader()
            Spacer(modifier = Modifier.height(48.dp))

            AnimatedContent(targetState = uiState.currentStep, transitionSpec = {
                slideInHorizontally { width -> width } + fadeIn() with
                        slideOutHorizontally { width -> -width } + fadeOut()
            }) {
                when (it) {
                    0 -> WelcomeStep(onNext = { viewModel.onNextStep() })
                    1 -> InterestsStep(
                        selectedInterests = uiState.selectedInterests,
                        onToggleInterest = { interest -> viewModel.toggleInterest(interest) },
                        onNext = { viewModel.onNextStep() },
                        onPrevious = { viewModel.onPreviousStep() }
                    )
                    2 -> GoalsStep(
                        selectedGoals = uiState.selectedGoals,
                        onToggleGoal = { goal -> viewModel.toggleGoal(goal) },
                        onComplete = { viewModel.completeOnboarding(onCompleted) },
                        onPrevious = { viewModel.onPreviousStep() },
                        isLoading = uiState.isLoading
                    )
                    else -> Text("Onboarding Completed!") // Placeholder
                }
            }
        }
    }
}

@Composable
fun WelcomeStep(onNext: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OnboardingStepCard(
            title = "Chào mừng đến với MyJournalApp!",
            description = "Hành trình ghi lại cảm xúc và suy nghĩ của bạn bắt đầu từ đây.",
            image = painterResource(id = R.drawable.ic_launcher_foreground)
        )
        Spacer(modifier = Modifier.height(32.dp))
        PrimaryButton(text = "Bắt đầu", onClick = onNext)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InterestsStep(
    selectedInterests: Set<String>,
    onToggleInterest: (String) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit
) {
    val interests = listOf(
        "Sức khỏe", "Hạnh phúc", "Công việc", "Học tập", "Mối quan hệ",
        "Sáng tạo", "Tài chính", "Du lịch", "Phát triển bản thân", "Gia đình"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Bạn quan tâm đến điều gì?",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            interests.forEach { interest ->
                SelectableInterestChip(
                    label = interest,
                    isSelected = selectedInterests.contains(interest),
                    onToggle = { onToggleInterest(interest) }
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        if (selectedInterests.isNotEmpty()) {
            PrimaryButton(
                text = "Tiếp tục",
                onClick = onNext,
                enabled = true // Hoặc có thể bỏ luôn enabled vì nút chỉ hiển thị khi đã chọn
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        SecondaryButton(text = "Quay lại", onClick = onPrevious)
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GoalsStep(
    selectedGoals: Set<String>,
    onToggleGoal: (String) -> Unit,
    onComplete: () -> Unit,
    onPrevious: () -> Unit,
    isLoading: Boolean
) {
    val goals = listOf(
        "Xả stress", "Viết tri ân", "Viết phản tư", "Theo dõi tâm trạng",
        "Ghi lại kỷ niệm", "Phát triển thói quen", "Sáng tạo nội dung"
    )
    var progress by remember { mutableFloatStateOf(0.1f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = "Mục tiêu của bạn là gì?",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            goals.forEach { goal ->
                SelectableInterestChip(
                    label = goal,
                    isSelected = selectedGoals.contains(goal),
                    onToggle = { onToggleGoal(goal) }
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        if (isLoading) {
            CircularWavyProgressIndicator()
        } else {
            if (selectedGoals.isNotEmpty()) {
                PrimaryButton(
                    text = "Tiếp tục",
                    onClick = onComplete,
                    enabled = true // Hoặc có thể bỏ luôn enabled vì nút chỉ hiển thị khi đã chọn
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        SecondaryButton(text = "Quay lại", onClick = onPrevious)
    }
}
