package com.myjournalapp.ui.timeline.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onSearchClick: () -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LargeTopAppBar(
        title = { Text("Nhật ký") },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(Icons.Filled.Search, contentDescription = "Tìm kiếm")
            }
            IconButton(onClick = onFilterClick) {
                Icon(Icons.Filled.Star, contentDescription = "Lọc")
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}