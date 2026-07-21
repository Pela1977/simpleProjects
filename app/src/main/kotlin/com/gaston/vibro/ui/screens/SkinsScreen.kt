package com.gaston.vibro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gaston.vibro.haptics.HapticsViewModel
import com.gaston.vibro.ui.theme.LocalVivroSkin
import com.gaston.vibro.ui.theme.SkinRegistry
import com.gaston.vibro.ui.theme.VivroSkin

@Composable
fun SkinsScreen(viewModel: HapticsViewModel, modifier: Modifier = Modifier) {
    val skin = LocalVivroSkin.current
    val activeSkin by viewModel.skin.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Skins",
            color = skin.colors.primary,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "El skin cambia colores, tipografía y la forma del canvas central.",
            color = skin.colors.textSecondary,
            style = MaterialTheme.typography.bodyMedium
        )

        SkinRegistry.all.forEach { s ->
            SkinCard(
                skin = s,
                isActive = s.id == activeSkin.id,
                currentTextColor = skin.colors.textPrimary,
                accentColor = skin.colors.primary,
                onClick = { viewModel.setSkin(s.id) }
            )
        }
    }
}

@Composable
private fun SkinCard(
    skin: VivroSkin,
    isActive: Boolean,
    currentTextColor: Color,
    accentColor: Color,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(skin.colors.background)
            .border(
                width = if (isActive) 2.dp else 1.dp,
                color = if (isActive) accentColor else currentTextColor.copy(alpha = 0.15f),
                shape = shape
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Paleta del skin en tres círculos
        Row(horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
            listOf(skin.colors.primary, skin.colors.secondary, skin.colors.accent).forEach { c ->
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(c)
                        .border(1.dp, skin.colors.background, CircleShape)
                )
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = skin.name,
                    color = skin.colors.textPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
                if (skin.isExplicit) {
                    Text(
                        text = "18+",
                        color = skin.colors.primary,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(skin.colors.primary.copy(alpha = 0.18f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Text(
                text = skin.description,
                color = skin.colors.textSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
        if (isActive) {
            Text(
                text = "✓",
                color = accentColor,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
