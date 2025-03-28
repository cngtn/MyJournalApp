package com.myjournalapp.ui.timeline

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import com.myjournalapp.data.sample.sampleEntries // Import dữ liệu mẫu
import com.myjournalapp.data.sample.sampleSuggestions // Import gợi ý mẫu
import com.myjournalapp.ui.theme.MyJournalAppTheme // Import Theme
// Import các components đã tách ra
import com.myjournalapp.ui.timeline.components.TimelineContent
import com.myjournalapp.ui.timeline.components.TimelineFAB
import com.myjournalapp.ui.timeline.components.TimelineTopAppBar
import android.content.res.Configuration // Cho Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetail: (entryId: String) -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToSearch: () -> Unit = { Log.d("Timeline", "Search Clicked") },
    onNavigateToFilter: () -> Unit = { Log.d("Timeline", "Filter Clicked") },
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TimelineTopAppBar(
                scrollBehavior = scrollBehavior,
                onSearchClick = onNavigateToSearch,
                onFilterClick = onNavigateToFilter
            )
        },
        floatingActionButton = {
            TimelineFAB(onClick = onNavigateToCreate)
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding -> // Padding này từ Scaffold (TopAppBar)
        TimelineContent(
            entries = sampleEntries, // Dùng dữ liệu mẫu
            suggestions = sampleSuggestions, // Dùng gợi ý mẫu
            onNavigateToDetail = onNavigateToDetail,
            onSuggestionClick = { suggestion ->
                Log.d("Timeline", "Suggestion clicked: $suggestion")
                onNavigateToCreate() // Ví dụ: mở màn hình tạo mới
            },
            scaffoldPadding = innerPadding // Truyền padding của Scaffold xuống
        )
    }
}

// --- Preview ---
@Preview(showBackground = true, name = "Timeline Light")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Timeline Dark")
@Composable
fun TimelineScreenPreview() {
    MyJournalAppTheme {
        TimelineScreen(
            onNavigateToDetail = {},
            onNavigateToCreate = {}
        )
    }
}