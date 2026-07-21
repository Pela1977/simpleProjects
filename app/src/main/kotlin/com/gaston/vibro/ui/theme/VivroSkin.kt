package com.gaston.vibro.ui.theme

import androidx.compose.ui.graphics.Color

enum class CanvasStyle { ORCHID, SILK, DAWN, PEARL, EMBER, NOIR }

data class VivroSkinColors(
    val background: Color,
    val surface: Color,
    val surfaceElevated: Color,
    val primary: Color,
    val secondary: Color,
    val accent: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val onPrimary: Color
)

data class VivroSkin(
    val id: String,
    val name: String,
    val description: String,
    val colors: VivroSkinColors,
    val isDark: Boolean,
    val isExplicit: Boolean = false,
    val canvasStyle: CanvasStyle
)
