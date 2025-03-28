package com.myjournalapp.data.sample // <<--- Package cho dữ liệu mẫu

import com.myjournalapp.data.model.JournalEntry // Import data class

// Danh sách các mục nhật ký mẫu
val sampleEntries: List<JournalEntry> = List(20) { index ->
    JournalEntry(
        id = "entry_$index",
        date = "Ngày ${28 - index}, Tháng 3, 2025",
        snippet = "Hôm nay là một ngày ${if (index % 2 == 0) "tuyệt vời" else "bình thường"}. ${"Viết nhật ký giúp tôi cảm thấy tốt hơn. ".repeat(index % 3 + 1)}"
    )
}

// Danh sách các gợi ý mẫu
val sampleSuggestions: List<String> = listOf(
    "Gợi ý: Suy ngẫm về một thành tựu gần đây.",
    "Gợi ý: Viết về địa điểm bạn đã ghé thăm cuối tuần.",
    "Gợi ý: Điều gì làm bạn biết ơn hôm nay?"
)