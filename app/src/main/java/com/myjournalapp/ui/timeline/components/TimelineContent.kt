package com.myjournalapp.ui.timeline.components

// --- Import thêm cho Staggered Grid ---
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid // <<--- Thay đổi
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells       // <<--- Thay đổi
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan  // <<--- Thay đổi
import androidx.compose.foundation.lazy.staggeredgrid.items                 // <<--- Thay đổi items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState // <<--- Thay đổi state
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState      // <<--- Thay đổi type state
// --- Các import khác giữ nguyên ---
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myjournalapp.data.model.JournalEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineContent(
    entries: List<JournalEntry>,
    suggestions: List<String>,
    onNavigateToDetail: (String) -> Unit,
    onSuggestionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    lazyStaggeredGridState: LazyStaggeredGridState = rememberLazyStaggeredGridState(), // <<--- Sử dụng State của Staggered Grid
    scaffoldPadding: PaddingValues = PaddingValues()
) {
    val gridSpacing = 16.dp // Khoảng cách chung

    // --- Sử dụng LazyVerticalStaggeredGrid ---
    LazyVerticalStaggeredGrid(
        // Định nghĩa 2 cột cho lưới so le
        columns = StaggeredGridCells.Fixed(2), // <<--- Dùng StaggeredGridCells
        state = lazyStaggeredGridState,
        modifier = modifier
            .fillMaxSize()
            .padding(scaffoldPadding)
//            .navigationBarsPadding()
            .padding(horizontal = gridSpacing), // Padding lề ngang
        // Khoảng cách dọc TỐI THIỂU giữa các item
        verticalItemSpacing = gridSpacing, // <<--- Đổi tên từ verticalArrangement
        // Khoảng cách ngang giữa các cột
        horizontalArrangement = Arrangement.spacedBy(gridSpacing),
        contentPadding = PaddingValues(bottom = 96.dp) // Padding dưới cùng
    ) {
        // --- Phần Gợi ý (Chiếm toàn bộ chiều rộng) ---
        items(
            items = suggestions,
//            span = { GridItemSpan(maxLineSpan) } // <<--- Chiếm hết chiều rộng lưới (2 cột)
        ) { suggestionText ->
            SuggestionCard(
                suggestionText = suggestionText,
                onClick = { onSuggestionClick(suggestionText) }
                // Lưu ý: Modifier.fillMaxWidth() đã được xóa bên trong SuggestionCard
            )
        }

        // --- Phân cách (Chiếm toàn bộ chiều rộng) ---
        item(
            span = StaggeredGridItemSpan.FullLine // <<--- Dùng StaggeredGridItemSpan
        ) {
            Spacer(modifier = Modifier.height(8.dp))
        }

        // --- Phần Danh sách Nhật ký (Sẽ tự động xếp so le) ---
        items(
            items = entries,
            key = { entry -> entry.id }
            // Không cần span, tự động xếp so le vào 2 cột
        ) { entry ->
            // Để hiệu ứng rõ ràng, JournalEntryCard nên có chiều cao thay đổi
            JournalEntryCard(
                entry = entry,
                onClick = { onNavigateToDetail(entry.id) }
            )
        }
    } // --- Kết thúc LazyVerticalStaggeredGrid ---
}