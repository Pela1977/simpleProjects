package com.gaston.vibro.ui.creator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gaston.vibro.haptics.FractalExpander
import com.gaston.vibro.haptics.HapticsConstants
import com.gaston.vibro.haptics.Segment
import com.gaston.vibro.haptics.SegmentTools

// Editor por tramos: control fino de cada segmento (duración + amplitud),
// reordenar con ▲▼, y el expansor fractal para generar complejidad autosimilar.
@Composable
fun SegmentEditor(
    accentColor: Color,
    surfaceColor: Color,
    textColor: Color,
    segments: SnapshotStateList<Segment>,
    modifier: Modifier = Modifier
) {
    var fractalDepth by remember { mutableStateOf(2) }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ActionChip("+ Pulso", accentColor, surfaceColor) {
                segments.add(Segment(150, 150))
            }
            ActionChip("+ Pausa", textColor.copy(alpha = 0.7f), surfaceColor) {
                segments.add(Segment(100, 0))
            }
        }

        if (segments.isEmpty()) {
            Text(
                text = "Agregá tramos, o creá una base en Dibujar / Grabar y refinala acá.",
                color = textColor.copy(alpha = 0.5f),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        segments.forEachIndexed { index, segment ->
            SegmentRow(
                index = index,
                segment = segment,
                isLast = index == segments.size - 1,
                accentColor = accentColor,
                surfaceColor = surfaceColor,
                textColor = textColor,
                onChange = { segments[index] = it },
                onMoveUp = {
                    if (index > 0) {
                        val tmp = segments[index - 1]
                        segments[index - 1] = segments[index]
                        segments[index] = tmp
                    }
                },
                onMoveDown = {
                    if (index < segments.size - 1) {
                        val tmp = segments[index + 1]
                        segments[index + 1] = segments[index]
                        segments[index] = tmp
                    }
                },
                onDelete = { segments.removeAt(index) }
            )
        }

        // ── Fractal / Pattern DNA ────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(surfaceColor)
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Fractal DNA",
                color = accentColor,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Replica tu secuencia a distintas escalas de tiempo: un eco rápido antes, y una versión expandida después. Complejidad orgánica desde una forma simple.",
                color = textColor.copy(alpha = 0.6f),
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Profundidad:",
                    color = textColor.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.labelSmall
                )
                (1..FractalExpander.MAX_DEPTH).forEach { d ->
                    val isActive = d == fractalDepth
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isActive) accentColor else Color.Transparent)
                            .clickable { fractalDepth = d }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "$d",
                            color = if (isActive) Color.White else textColor.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
            ActionChip(
                label = "Aplicar fractal",
                color = accentColor,
                background = accentColor.copy(alpha = 0.15f),
                enabled = segments.isNotEmpty()
            ) {
                val expanded = FractalExpander.expand(segments.toList(), fractalDepth)
                segments.clear()
                segments.addAll(expanded)
            }
        }

        if (segments.isNotEmpty()) {
            Text(
                text = "Total: ${segments.size} tramos · ${SegmentTools.totalDurationMs(segments)} ms por ciclo",
                color = textColor.copy(alpha = 0.55f),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun ActionChip(
    label: String,
    color: Color,
    background: Color,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = label,
            color = if (enabled) color else color.copy(alpha = 0.35f),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun SegmentRow(
    index: Int,
    segment: Segment,
    isLast: Boolean,
    accentColor: Color,
    surfaceColor: Color,
    textColor: Color,
    onChange: (Segment) -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onDelete: () -> Unit
) {
    val isPause = segment.amplitude == 0
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(surfaceColor)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isPause) "${index + 1} · Pausa — ${segment.durationMs} ms"
                    else "${index + 1} · Pulso — ${segment.durationMs} ms · amp ${segment.amplitude}",
                color = if (isPause) textColor.copy(alpha = 0.55f) else textColor,
                style = MaterialTheme.typography.labelSmall
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                RowButton("▲", enabled = index > 0, textColor, onMoveUp)
                RowButton("▼", enabled = !isLast, textColor, onMoveDown)
                RowButton("✕", enabled = true, textColor, onDelete)
            }
        }
        Slider(
            value = segment.durationMs.toFloat(),
            onValueChange = { onChange(segment.copy(durationMs = it.toLong())) },
            valueRange = SegmentTools.MIN_SEGMENT_MS.toFloat()..1000f,
            colors = SliderDefaults.colors(
                thumbColor = accentColor,
                activeTrackColor = accentColor,
                inactiveTrackColor = accentColor.copy(alpha = 0.2f)
            )
        )
        if (!isPause) {
            Slider(
                value = segment.amplitude.toFloat(),
                onValueChange = {
                    onChange(segment.copy(amplitude = it.toInt().coerceAtLeast(HapticsConstants.MIN_AMPLITUDE)))
                },
                valueRange = HapticsConstants.MIN_AMPLITUDE.toFloat()..HapticsConstants.MAX_AMPLITUDE.toFloat(),
                colors = SliderDefaults.colors(
                    thumbColor = accentColor.copy(alpha = 0.7f),
                    activeTrackColor = accentColor.copy(alpha = 0.7f),
                    inactiveTrackColor = accentColor.copy(alpha = 0.15f)
                )
            )
        }
    }
}

@Composable
private fun RowButton(label: String, enabled: Boolean, textColor: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = textColor.copy(alpha = if (enabled) 0.7f else 0.25f),
            style = MaterialTheme.typography.labelMedium
        )
    }
}
