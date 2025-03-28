package com.myjournalapp.ui.timeline.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TimelineFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = "Tạo mục mới",
//            modifier = Modifier.size(FloatingActionButtonDefaults.LargeIconSize)
        )
    }
}