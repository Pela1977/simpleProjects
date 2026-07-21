package com.gaston.vibro.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalVivroSkin = staticCompositionLocalOf { SkinRegistry.default }

@Composable
fun VivroTheme(
    skin: VivroSkin = SkinRegistry.default,
    content: @Composable () -> Unit
) {
    val c = skin.colors
    val colorScheme = if (skin.isDark) {
        darkColorScheme(
            background = c.background,
            surface = c.surface,
            surfaceVariant = c.surfaceElevated,
            primary = c.primary,
            secondary = c.secondary,
            tertiary = c.accent,
            onBackground = c.textPrimary,
            onSurface = c.textPrimary,
            onSurfaceVariant = c.textSecondary,
            onPrimary = c.onPrimary
        )
    } else {
        lightColorScheme(
            background = c.background,
            surface = c.surface,
            surfaceVariant = c.surfaceElevated,
            primary = c.primary,
            secondary = c.secondary,
            tertiary = c.accent,
            onBackground = c.textPrimary,
            onSurface = c.textPrimary,
            onSurfaceVariant = c.textSecondary,
            onPrimary = c.onPrimary
        )
    }

    CompositionLocalProvider(LocalVivroSkin provides skin) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = VivroTypography,
            content = content
        )
    }
}
