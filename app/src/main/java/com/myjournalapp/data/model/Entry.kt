package com.myjournalapp.data.model

import java.time.LocalDate

data class Entry(
    val id: String,
    val date: LocalDate,
    val mood: String,
    val preview: String
)
