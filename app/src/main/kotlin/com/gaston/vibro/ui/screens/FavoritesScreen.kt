package com.gaston.vibro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.unit.dp
import com.gaston.vibro.data.FavoriteConfig
import com.gaston.vibro.haptics.HapticsViewModel
import com.gaston.vibro.ui.theme.LocalVivroSkin

@Composable
fun FavoritesScreen(viewModel: HapticsViewModel, modifier: Modifier = Modifier) {
    val skin = LocalVivroSkin.current
    val favorites by viewModel.favorites.collectAsState()
    val allPatterns by viewModel.allPatterns.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Favoritos",
            color = skin.colors.primary,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Configuraciones completas: patrón + intensidad + rampa + frecuencia. Tocá una para reproducirla.",
            color = skin.colors.textSecondary,
            style = MaterialTheme.typography.bodyMedium
        )

        if (favorites.isEmpty()) {
            Text(
                text = "Todavía no guardaste favoritos. En Inicio, ajustá todo a tu gusto y tocá “Guardar como favorito”.",
                color = skin.colors.textSecondary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 24.dp)
            )
        }

        favorites.forEach { fav ->
            FavoriteCard(
                favorite = fav,
                patternName = allPatterns.find { it.id == fav.patternId }?.name ?: "(patrón eliminado)",
                onPlay = { viewModel.applyFavorite(fav) },
                onDelete = { viewModel.deleteFavorite(fav.id) },
                skinColors = skin.colors
            )
        }
    }
}

@Composable
private fun FavoriteCard(
    favorite: FavoriteConfig,
    patternName: String,
    onPlay: () -> Unit,
    onDelete: () -> Unit,
    skinColors: com.gaston.vibro.ui.theme.VivroSkinColors
) {
    val shape = RoundedCornerShape(14.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(skinColors.surface)
            .border(1.dp, skinColors.textPrimary.copy(alpha = 0.08f), shape)
            .clickable(onClick = onPlay)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = favorite.name,
                color = skinColors.textPrimary,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "$patternName · Nivel ${favorite.intensityLevel + 1} · " +
                    "${favorite.rampType.displayName} · ${favorite.onTimeMs}/${favorite.offTimeMs} ms",
                color = skinColors.textSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            text = "▶",
            color = skinColors.primary,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "✕",
            color = skinColors.textSecondary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onDelete)
                .padding(6.dp)
        )
    }
}
