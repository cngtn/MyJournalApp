package com.myjournalapp.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myjournalapp.data.model.Entry

@Composable
fun EntryCard(entry: Entry, onClick: (Entry) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(entry) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Mood: ${entry.mood}")
            Text(text = "Date: ${entry.date}")
            Text(text = entry.preview)
        }
    }
}
