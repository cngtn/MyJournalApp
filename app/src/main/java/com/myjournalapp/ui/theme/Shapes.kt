package com.myjournalapp.ui.theme // <<--- THAY PACKAGE NAME

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Định nghĩa Shapes theo Material 3 Shape Scale
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp), // Thường dùng cho Card, Button,...
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp)
)