package com.gaston.vibro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gaston.vibro.haptics.HapticsViewModel
import com.gaston.vibro.haptics.PatternsRegistry
import com.gaston.vibro.ui.components.FrequencyControls
import com.gaston.vibro.ui.components.IntensitySlider
import com.gaston.vibro.ui.components.OrganicCanvas
import com.gaston.vibro.ui.components.PatternSelector
import com.gaston.vibro.ui.components.PlayStopButton
import com.gaston.vibro.ui.components.RampSelector
import com.gaston.vibro.ui.theme.LocalVivroSkin

@Composable
fun MainScreen(viewModel: HapticsViewModel, modifier: Modifier = Modifier) {
    val skin = LocalVivroSkin.current
    val state by viewModel.state.collectAsState()
    val customPatterns by viewModel.customPatterns.collectAsState()
    var showSaveDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "vibro",
            color = skin.colors.primary,
            style = MaterialTheme.typography.displayMedium
        )

        // Canvas orgánico con botón play superpuesto
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp),
            contentAlignment = Alignment.Center
        ) {
            OrganicCanvas(
                skin = skin,
                isPlaying = state.isPlaying,
                intensityLevel = state.intensityLevel,
                modifier = Modifier.fillMaxSize()
            )
            PlayStopButton(
                isPlaying = state.isPlaying,
                onClick = { viewModel.togglePlayback() },
                size = 76.dp,
                color = skin.colors.primary,
                iconColor = skin.colors.onPrimary
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = state.activePattern?.name ?: "Elegí un patrón",
                color = skin.colors.textPrimary,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            if (!state.activePattern?.description.isNullOrBlank()) {
                Text(
                    text = state.activePattern?.description ?: "",
                    color = skin.colors.textSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }

        IntensitySlider(
            level = state.intensityLevel,
            onLevelChange = { viewModel.setIntensityLevel(it) },
            activeColor = skin.colors.primary,
            inactiveColor = skin.colors.primary.copy(alpha = 0.2f),
            textColor = skin.colors.textPrimary,
            modifier = Modifier.fillMaxWidth()
        )

        FrequencyControls(
            onTimeMs = state.onTimeMs,
            offTimeMs = state.offTimeMs,
            onOnTimeChange = { viewModel.setOnTime(it) },
            onOffTimeChange = { viewModel.setOffTime(it) },
            accentColor = skin.colors.primary,
            textColor = skin.colors.textPrimary,
            modifier = Modifier.fillMaxWidth()
        )

        RampSelector(
            selected = state.activeRamp,
            onSelect = { viewModel.setRamp(it) },
            accentColor = skin.colors.primary,
            surfaceColor = skin.colors.surface,
            textColor = skin.colors.textPrimary,
            modifier = Modifier.fillMaxWidth()
        )

        PatternSelector(
            presets = PatternsRegistry.all,
            customs = customPatterns,
            selected = state.activePattern,
            onSelect = { viewModel.selectPattern(it) },
            onDeleteCustom = { viewModel.deleteCustomPattern(it.id) },
            accentColor = skin.colors.primary,
            surfaceColor = skin.colors.surface,
            textColor = skin.colors.textPrimary,
            modifier = Modifier.fillMaxWidth()
        )

        // Guardar la configuración actual como favorito
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(skin.colors.surface)
                .clickable(enabled = state.activePattern != null) { showSaveDialog = true }
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "♥ Guardar como favorito",
                color = skin.colors.secondary,
                style = MaterialTheme.typography.labelMedium
            )
        }

        if (!viewModel.hasAmplitudeControl) {
            Text(
                text = "⚠ Modo PWM — este dispositivo no soporta control de amplitud",
                color = skin.colors.secondary,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }

    if (showSaveDialog) {
        var name by remember { mutableStateOf(state.activePattern?.name ?: "") }
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            containerColor = skin.colors.surfaceElevated,
            title = {
                Text("Guardar favorito", color = skin.colors.textPrimary)
            },
            text = {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre", color = skin.colors.textSecondary) },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.saveFavorite(name)
                    showSaveDialog = false
                }) {
                    Text("Guardar", color = skin.colors.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Cancelar", color = skin.colors.textSecondary)
                }
            }
        )
    }
}
