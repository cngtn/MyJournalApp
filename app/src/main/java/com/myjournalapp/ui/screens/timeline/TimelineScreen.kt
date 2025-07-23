package com.myjournalapp.ui.screens.timeline

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.myjournalapp.ui.components.cards.EntryCard
import com.myjournalapp.ui.components.dividers.DateDivider
import com.myjournalapp.viewmodel.TimelineViewModel
import com.myjournalapp.viewmodel.TimelineUiState
import com.myjournalapp.data.model.Entry
import java.time.LocalDate
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.myjournalapp.ui.components.bars.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreen(
    viewModel: TimelineViewModel = hiltViewModel(),
    onEntryClick: (Entry) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Lấy padding an toàn cho thanh điều hướng dưới cùng.
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val collapsedFraction by remember {
        derivedStateOf { scrollBehavior.state.collapsedFraction }
    }
    val dynamicAlpha = 1f - collapsedFraction
    val dynamicColor = MaterialTheme.colorScheme.surface.copy(alpha = dynamicAlpha)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    SearchBar(
                        query = uiState.searchQuery,
                        onQueryChange = { query -> viewModel.onSearchQueryChanged(query) },
                        onSearch = { query ->
                            viewModel.onSearchQueryChanged(query)
                            // active = false // You might want to set active to false here if search closes the bar
                        },
                        placeholder = { Text("Tìm kiếm...") },
                        modifier = Modifier.fillMaxWidth(),
                        active = uiState.isSearching, // Assuming isSearching in ViewModel controls active state
                        onActiveChange = { active ->
                            // You might want to update a state in ViewModel to control active state
                            // For now, we'll just update the search query if active changes
                            if (!active) {
                                viewModel.onSearchQueryChanged("") // Clear search when inactive
                            }
                        }
                    )
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = dynamicColor,
                    scrolledContainerColor = dynamicColor
                )
            )

        },
    ) { paddingValues ->

        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            Column {
                LazyColumn {
                    val groupedEntries = uiState.filteredEntries.groupBy { it.date }
                    groupedEntries.forEach { (date, entries) ->
                        item {
                            DateDivider(date = date)
                        }
                        items(entries) { entry ->
                            EntryCard(entry = entry, onClick = onEntryClick)
                        }
                    }
                }
            }
        }
    }
}