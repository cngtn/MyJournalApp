package com.myjournalapp.data.model // <<--- Package cho model

import androidx.compose.runtime.Immutable

@Immutable // Đảm bảo class ổn định, tốt cho hiệu năng Compose
data class JournalEntry(
    val id: String,
    val date: String,
    val snippet: String
)