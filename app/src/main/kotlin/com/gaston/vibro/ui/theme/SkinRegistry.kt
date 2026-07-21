package com.gaston.vibro.ui.theme

import androidx.compose.ui.graphics.Color

object SkinRegistry {

    val boudoir = VivroSkin(
        id = "boudoir",
        name = "Boudoir",
        description = "Orquídea nocturna — fucsia y dorado sobre negro",
        colors = VivroSkinColors(
            background = Color(0xFF0A0008),
            surface = Color(0xFF1A0018),
            surfaceElevated = Color(0xFF2A0526),
            primary = Color(0xFFE8185C),
            secondary = Color(0xFFC4A84A),
            accent = Color(0xFFB05ACF),
            textPrimary = Color(0xFFF0E0E8),
            textSecondary = Color(0xFFA88898),
            onPrimary = Color.White
        ),
        isDark = true,
        canvasStyle = CanvasStyle.ORCHID
    )

    val sedaPiel = VivroSkin(
        id = "seda_piel",
        name = "Seda & Piel",
        description = "Carmesí y rosa piel — curvas cálidas",
        colors = VivroSkinColors(
            background = Color(0xFF150C0C),
            surface = Color(0xFF241414),
            surfaceElevated = Color(0xFF341C1C),
            primary = Color(0xFFC0143C),
            secondary = Color(0xFFF2A59D),
            accent = Color(0xFFE07050),
            textPrimary = Color(0xFFF5E5E0),
            textSecondary = Color(0xFFB09088),
            onPrimary = Color.White
        ),
        isDark = true,
        canvasStyle = CanvasStyle.SILK
    )

    val alba = VivroSkin(
        id = "alba",
        name = "Alba",
        description = "Amanecer — crema suave y coral, para uso diurno",
        colors = VivroSkinColors(
            background = Color(0xFFFDF6F0),
            surface = Color(0xFFF6E8E0),
            surfaceElevated = Color(0xFFEFDCD2),
            primary = Color(0xFFE0566A),
            secondary = Color(0xFFD9A05B),
            accent = Color(0xFFC97B94),
            textPrimary = Color(0xFF3A2830),
            textSecondary = Color(0xFF8A6E78),
            onPrimary = Color.White
        ),
        isDark = false,
        canvasStyle = CanvasStyle.DAWN
    )

    val nacar = VivroSkin(
        id = "nacar",
        name = "Nácar",
        description = "Perla y lila — luminoso y sereno",
        colors = VivroSkinColors(
            background = Color(0xFFF8F5FA),
            surface = Color(0xFFEEE8F4),
            surfaceElevated = Color(0xFFE4DAEE),
            primary = Color(0xFF9B59B6),
            secondary = Color(0xFF7FB3D5),
            accent = Color(0xFFC39BD3),
            textPrimary = Color(0xFF2E2438),
            textSecondary = Color(0xFF7A6C88),
            onPrimary = Color.White
        ),
        isDark = false,
        canvasStyle = CanvasStyle.PEARL
    )

    val rubi = VivroSkin(
        id = "rubi",
        name = "Rubí",
        description = "Rojo profundo, formas directas — sin sutilezas",
        colors = VivroSkinColors(
            background = Color(0xFF120204),
            surface = Color(0xFF2A060C),
            surfaceElevated = Color(0xFF3D0A14),
            primary = Color(0xFFFF1744),
            secondary = Color(0xFFFF8A65),
            accent = Color(0xFFFF5252),
            textPrimary = Color(0xFFFFE5E8),
            textSecondary = Color(0xFFC08890),
            onPrimary = Color.White
        ),
        isDark = true,
        isExplicit = true,
        canvasStyle = CanvasStyle.EMBER
    )

    val obsidiana = VivroSkin(
        id = "obsidiana",
        name = "Obsidiana",
        description = "Negro absoluto y violeta eléctrico — intensidad pura",
        colors = VivroSkinColors(
            background = Color(0xFF000000),
            surface = Color(0xFF14001F),
            surfaceElevated = Color(0xFF22003A),
            primary = Color(0xFFAA00FF),
            secondary = Color(0xFF00E5FF),
            accent = Color(0xFFE040FB),
            textPrimary = Color(0xFFF0E5FF),
            textSecondary = Color(0xFF9080A8),
            onPrimary = Color.White
        ),
        isDark = true,
        isExplicit = true,
        canvasStyle = CanvasStyle.NOIR
    )

    val all: List<VivroSkin> = listOf(boudoir, sedaPiel, alba, nacar, rubi, obsidiana)

    val default: VivroSkin = boudoir

    fun byId(id: String?): VivroSkin = all.find { it.id == id } ?: default
}
