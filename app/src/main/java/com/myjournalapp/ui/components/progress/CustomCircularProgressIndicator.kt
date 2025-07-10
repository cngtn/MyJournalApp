package com.myjournalapp.ui.components.progress

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
@Composable
fun CustomCircularProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float? = null // null for indeterminate, 0.0f to 1.0f for determinate
) {
    if (progress != null) {
        CircularProgressIndicator(modifier = modifier, progress = progress)
    } else {
        CircularProgressIndicator(modifier = modifier)
    }
}