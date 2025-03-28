package com.myjournalapp.ui.timeline.components

import androidx.compose.foundation.layout.Column
// import androidx.compose.foundation.layout.fillMaxWidth // <<--- Không cần nữa
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.myjournalapp.data.model.JournalEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalEntryCard(
    entry: JournalEntry,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier // Không có fillMaxWidth
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = entry.date,
                // ... style, color ...
            )
            Text(
                text = entry.snippet,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                // <<--- XÓA hoặc TĂNG maxLines để thẻ có chiều cao khác nhau ---
                // maxLines = 3,
                // overflow = TextOverflow.Ellipsis // Có thể giữ lại nếu vẫn muốn giới hạn chiều cao tối đa
            )
        }
    }
}