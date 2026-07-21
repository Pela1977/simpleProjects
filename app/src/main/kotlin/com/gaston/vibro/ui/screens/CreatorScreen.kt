package com.gaston.vibro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gaston.vibro.haptics.HapticsViewModel
import com.gaston.vibro.haptics.PatternCategory
import com.gaston.vibro.haptics.Segment
import com.gaston.vibro.haptics.SegmentTools
import com.gaston.vibro.ui.components.SegmentsWaveform
import com.gaston.vibro.ui.creator.MixerPanel
import com.gaston.vibro.ui.creator.SegmentEditor
import com.gaston.vibro.ui.creator.TapRecorder
import com.gaston.vibro.ui.creator.WaveformCanvas
import com.gaston.vibro.ui.theme.LocalVivroSkin

private enum class CreatorMode(val label: String) {
    WHITEBOARD("Dibujar"),
    TAP("Grabar"),
    SEGMENTS("Tramos"),
    MIXER("Mezclar")
}

// Pantalla de creación: cuatro modos que convergen en la misma lista de
// segmentos. Preview háptico en cualquier momento, guardado con nombre y categoría.
@Composable
fun CreatorScreen(viewModel: HapticsViewModel, modifier: Modifier = Modifier) {
    val skin = LocalVivroSkin.current
    val state by viewModel.state.collectAsState()
    val allPatterns by viewModel.allPatterns.collectAsState()

    var mode by remember { mutableStateOf(CreatorMode.WHITEBOARD) }
    val segments = remember { mutableStateListOf<Segment>() }
    var showSaveDialog by remember { mutableStateOf(false) }

    val accent = skin.colors.primary
    val surface = skin.colors.surface
    val text = skin.colors.textPrimary
    val hasContent = segments.any { it.amplitude > 0 }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Crear patrón",
            color = accent,
            style = MaterialTheme.typography.titleLarge
        )

        // Tabs de modo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CreatorMode.entries.forEach { m ->
                val isActive = m == mode
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isActive) accent else surface)
                        .clickable { mode = m }
                        .padding(vertical = 9.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = m.label,
                        color = if (isActive) Color.White else text.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }

        when (mode) {
            CreatorMode.WHITEBOARD -> WaveformCanvas(
                accentColor = accent,
                surfaceColor = surface,
                textColor = text,
                onSegmentsChange = { newSegments ->
                    segments.clear()
                    segments.addAll(newSegments)
                }
            )
            CreatorMode.TAP -> TapRecorder(
                accentColor = accent,
                surfaceColor = surface,
                textColor = text,
                segments = segments
            )
            CreatorMode.SEGMENTS -> SegmentEditor(
                accentColor = accent,
                surfaceColor = surface,
                textColor = text,
                segments = segments
            )
            CreatorMode.MIXER -> MixerPanel(
                accentColor = accent,
                surfaceColor = surface,
                textColor = text,
                availablePatterns = allPatterns,
                onUseMix = { mixed ->
                    segments.clear()
                    segments.addAll(mixed)
                    mode = CreatorMode.SEGMENTS
                },
                onFeelMix = { mixed -> viewModel.previewSegments(mixed) }
            )
        }

        // ── Estado actual del patrón en construcción ─────────────────────────
        if (segments.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Tu patrón — ${SegmentTools.totalDurationMs(segments)} ms por ciclo",
                    color = text.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.labelSmall
                )
                SegmentsWaveform(
                    segments = segments.toList(),
                    color = accent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (state.isPreviewing) accent else accent.copy(alpha = 0.15f))
                    .clickable(enabled = hasContent) {
                        if (state.isPreviewing) viewModel.stopPreview()
                        else viewModel.previewSegments(segments.toList())
                    }
                    .padding(vertical = 13.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (state.isPreviewing) "■ Detener" else "Sentir",
                    color = if (state.isPreviewing) Color.White
                        else if (hasContent) accent else accent.copy(alpha = 0.35f),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (hasContent) accent else surface)
                    .clickable(enabled = hasContent) { showSaveDialog = true }
                    .padding(vertical = 13.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Guardar",
                    color = if (hasContent) Color.White else text.copy(alpha = 0.35f),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }

    if (showSaveDialog) {
        var name by remember { mutableStateOf("") }
        var category by remember { mutableStateOf(PatternCategory.SUAVE) }
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            containerColor = skin.colors.surfaceElevated,
            title = { Text("Guardar patrón", color = text) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre", color = skin.colors.textSecondary) },
                        singleLine = true
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        PatternCategory.entries.forEach { cat ->
                            val isActive = cat == category
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isActive) accent else surface)
                                    .clickable { category = cat }
                                    .padding(horizontal = 12.dp, vertical = 7.dp)
                            ) {
                                Text(
                                    text = cat.name,
                                    color = if (isActive) Color.White else text.copy(alpha = 0.6f),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.saveCustomPattern(name, "Creado por mí", category, segments.toList())
                    showSaveDialog = false
                    segments.clear()
                }) {
                    Text("Guardar", color = accent)
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
