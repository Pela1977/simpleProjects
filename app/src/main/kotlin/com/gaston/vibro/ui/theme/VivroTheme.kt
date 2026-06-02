package com.gaston.vibro.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Placeholder — compose-ui-designer expande esto en M6
private val BoudoirColorScheme = darkColorScheme(
    background = Color(0xFF0A0008),
    surface = Color(0xFF1A0018),
    primary = Color(0xFFE8185C),
    secondary = Color(0xFFC4A84A),
    onBackground = Color(0xFFF0E0E8),
    onSurface = Color(0xFFF0E0E8),
    onPrimary = Color.White,
)

@Composable
fun VivroTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BoudoirColorScheme,
        content = content
    )
}
