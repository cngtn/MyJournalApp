package com.myjournalapp.ui.screens.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myjournalapp.ui.components.buttons.PrimaryButton
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

import androidx.compose.foundation.layout.PaddingValues // Add this import
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.nestedscroll.nestedScroll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ...

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Lấy padding an toàn cho thanh điều hướng dưới cùng.
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 100.dp

    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            delay(1500) // Simulate a network call
            isRefreshing = false
        }
    }

    val lazyGridState = rememberLazyGridState()
    // Sử dụng enterAlwaysScrollBehavior để ẩn/hiện TopAppBar khi cuộn
    // Nếu bạn muốn TopAppBar biến mất hoàn toàn và chỉ hiển thị khi cuộn xuống hết,
    // hãy thử TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Chào buổi sáng!") },
                scrollBehavior = scrollBehavior,
                // Để TopAppBar cuộn qua khỏi status bar, bạn cần đảm bảo
                // background của TopAppBar là trong suốt hoặc đủ mờ để thấy nội dung bên dưới,
                // và Scaffold phải được cấu hình edge-to-edge.
                // Màu nền mặc định của TopAppBar sẽ che status bar nếu không có inset,
                // nhưng scrollBehavior sẽ giúp nó di chuyển.
                // Nếu muốn hiệu ứng trong suốt/mờ thì tùy chỉnh ở đây:
                 colors = TopAppBarDefaults.topAppBarColors(
                     containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0f) // Ví dụ màu mờ
                 )
            )
        },
    ) { innerPadding ->
        PullToRefreshBox(
            state = pullRefreshState,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            indicator = {
                PullToRefreshDefaults.LoadingIndicator(
                    state = pullRefreshState,
                    isRefreshing = isRefreshing,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        ) {
            LazyVerticalGrid(
                state = lazyGridState,
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = innerPadding,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { MoodTodayWidget(mood = uiState.todayMood) }
                item { WeatherWidget(weather = uiState.weather) }
                item(span = { GridItemSpan(2) }) {
                    PromptCard(prompt = uiState.dailyPrompt, onAnswer = { /* TODO */ })
                }
                item { QuickNoteCard(note = uiState.quickNote, onEdit = { /* TODO */ }) }
                item { ChallengeStatusCard(challenge = uiState.currentChallenge) }
                item(span = { GridItemSpan(2) }) { CalendarMoodHeatmap() }
                item(span = { GridItemSpan(2) }) { RecentPhotosGrid() }
                item(span = { GridItemSpan(2) }) { Calendar() }
                item(span = { GridItemSpan(2) }) {
                    Spacer(modifier = Modifier.height(bottomPadding))
                }
            }
        }
    }
}

@Composable
private fun getRandomCardColors(): Pair<Color, Color> {
    val baseColors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.error,
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.secondaryContainer,
        MaterialTheme.colorScheme.tertiaryContainer,
        MaterialTheme.colorScheme.errorContainer
    )
    val randomBaseColor = baseColors[Random.nextInt(baseColors.size)]

    // Function to lighten a color (adjust factor as needed)
    fun Color.lighten(factor: Float): Color {
        val red = (red + (1f - red) * factor).coerceIn(0f, 1f)
        val green = (green + (1f - green) * factor).coerceIn(0f, 1f)
        val blue = (blue + (1f - blue) * factor).coerceIn(0f, 1f)
        return Color(red, green, blue, alpha)
    }

    val lightenedColor = randomBaseColor.lighten(0.5f) // Adjust lighten factor for desired pastel effect

    val onLightenedColor = when (randomBaseColor) {
        MaterialTheme.colorScheme.primary -> MaterialTheme.colorScheme.onPrimary
        MaterialTheme.colorScheme.secondary -> MaterialTheme.colorScheme.onSecondary
        MaterialTheme.colorScheme.tertiary -> MaterialTheme.colorScheme.onTertiary
        MaterialTheme.colorScheme.error -> MaterialTheme.colorScheme.onError
        MaterialTheme.colorScheme.primaryContainer -> MaterialTheme.colorScheme.onPrimaryContainer
        MaterialTheme.colorScheme.secondaryContainer -> MaterialTheme.colorScheme.onSecondaryContainer
        MaterialTheme.colorScheme.tertiaryContainer -> MaterialTheme.colorScheme.onTertiaryContainer
        MaterialTheme.colorScheme.errorContainer -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.onSurface
    }
    // If the lightened color is very light, ensure onLightenedColor is dark enough
    // This is a simplified check, more robust luminance check might be needed
    val finalOnColor = if (lightenedColor.red > 0.8f && lightenedColor.green > 0.8f && lightenedColor.blue > 0.8f) {
        Color.Black // Or a very dark color from your palette
    } else {
        onLightenedColor
    }

    return Pair(lightenedColor, finalOnColor)
}

@Composable
fun MoodTodayWidget(mood: String) {
    val (cardColor, onCardColor) = getRandomCardColors()
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = cardColor)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Tâm trạng hôm nay:", style = MaterialTheme.typography.titleMedium, color = onCardColor)
            Text(mood, style = MaterialTheme.typography.bodyLarge, color = onCardColor)
        }
    }
}

@Composable
fun WeatherWidget(weather: String) {
    val (cardColor, onCardColor) = getRandomCardColors()
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = cardColor)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.LocationOn, contentDescription = "Location", modifier = Modifier.size(24.dp), tint = onCardColor)
            Spacer(modifier = Modifier.padding(8.dp))
            Column {
                Text("Thời tiết:", style = MaterialTheme.typography.titleMedium, color = onCardColor)
                Text(weather, style = MaterialTheme.typography.bodyLarge, color = onCardColor)
            }
        }
    }
}

@Composable
fun PromptCard(prompt: String, onAnswer: () -> Unit) {
    val (cardColor, onCardColor) = getRandomCardColors()
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = cardColor)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Câu hỏi gợi ý:", style = MaterialTheme.typography.titleMedium, color = onCardColor)
            Text(prompt, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(bottom = 16.dp), color = onCardColor)
            PrimaryButton(text = "Trả lời", onClick = onAnswer)
        }
    }
}

@Composable
fun QuickNoteCard(note: String, onEdit: () -> Unit) {
    val (cardColor, onCardColor) = getRandomCardColors()
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = cardColor)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Ghi chú nhanh:", style = MaterialTheme.typography.titleMedium, color = onCardColor)
                Icon(Icons.Default.Edit, contentDescription = "Edit Note", modifier = Modifier.size(20.dp).clickable { onEdit() }, tint = onCardColor)
            }
            Text(note, style = MaterialTheme.typography.bodyLarge, color = onCardColor)
        }
    }
}

@Composable
fun ChallengeStatusCard(challenge: String) {
    val (cardColor, onCardColor) = getRandomCardColors()
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = cardColor)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Thử thách hiện tại:", style = MaterialTheme.typography.titleMedium, color = onCardColor)
            Text(challenge, style = MaterialTheme.typography.bodyLarge, color = onCardColor)
        }
    }
}

@Composable
fun CalendarMoodHeatmap() {
    val (cardColor, onCardColor) = getRandomCardColors()
    Card(modifier = Modifier.fillMaxWidth().height(200.dp), colors = CardDefaults.cardColors(containerColor = cardColor)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Lịch tâm trạng (Heatmap)", style = MaterialTheme.typography.titleLarge, color = onCardColor)
            Text("// Placeholder for Calendar Mood Heatmap", style = MaterialTheme.typography.bodyMedium, color = onCardColor)
        }
    }
}

@Composable
fun RecentPhotosGrid() {
    val (cardColor, onCardColor) = getRandomCardColors()
    Card(modifier = Modifier.fillMaxWidth().height(200.dp), colors = CardDefaults.cardColors(containerColor = cardColor)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Ảnh gần đây", style = MaterialTheme.typography.titleLarge, color = onCardColor)
            Text("// Placeholder for Recent Photos Grid", style = MaterialTheme.typography.bodyMedium, color = onCardColor)
        }
    }
}

@Composable
fun Calendar() {
    val (cardColor, onCardColor) = getRandomCardColors()
    Card(modifier = Modifier.fillMaxWidth().height(200.dp), colors = CardDefaults.cardColors(containerColor = cardColor)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Ảnh gần đây", style = MaterialTheme.typography.titleLarge, color = onCardColor)
            Text("// Placeholder for Recent Photos Grid", style = MaterialTheme.typography.bodyMedium, color = onCardColor)
        }
    }
}
