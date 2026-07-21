package com.gaston.vibro.ui.creator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gaston.vibro.haptics.PatternMixer
import com.gaston.vibro.haptics.Segment
import com.gaston.vibro.haptics.VivroPattern
import com.gaston.vibro.ui.components.SegmentsWaveform

private class MixLayerState(val pattern: VivroPattern, var weight: Float)

// Layer Mixer: hasta 4 patrones apilados, cada uno con su peso.
// Las amplitudes se suman y clampean a 255 — un híbrido único.
@Composable
fun MixerPanel(
    accentColor: Color,
    surfaceColor: Color,
    textColor: Color,
    availablePatterns: List<VivroPattern>,
    onUseMix: (List<Segment>) -> Unit,
    onFeelMix: (List<Segment>) -> Unit,
    modifier: Modifier = Modifier
) {
    val layers = remember { mutableStateListOf<MixLayerState>() }
    // Recalculado en cada recomposición — barato (≤ 400 muestras × 4 capas)
    val mixed = PatternMixer.mix(layers.map { PatternMixer.Layer(it.pattern, it.weight) })

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Sumá hasta ${PatternMixer.MAX_LAYERS} patrones como capas. Tocá un patrón para agregarlo.",
            color = textColor.copy(alpha = 0.65f),
            style = MaterialTheme.typography.bodySmall
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            availablePatterns.forEach { pattern ->
                val alreadyAdded = layers.any { it.pattern.id == pattern.id }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (alreadyAdded) accentColor.copy(alpha = 0.25f) else surfaceColor)
                        .clickable(enabled = !alreadyAdded && layers.size < PatternMixer.MAX_LAYERS) {
                            layers.add(MixLayerState(pattern, 0.6f))
                        }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = pattern.name,
                        color = if (alreadyAdded) accentColor else textColor.copy(alpha = 0.75f),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }

        layers.forEachIndexed { index, layer ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(surfaceColor)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${layer.pattern.name} · ${(layer.weight * 100).toInt()}%",
                        color = textColor,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = "✕",
                        color = textColor.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { layers.removeAt(index) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Slider(
                    value = layer.weight,
                    onValueChange = { newWeight ->
                        // Reemplazo del elemento para forzar recomposición del mix
                        layers[index] = MixLayerState(layer.pattern, newWeight)
                    },
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = accentColor,
                        activeTrackColor = accentColor,
                        inactiveTrackColor = accentColor.copy(alpha = 0.2f)
                    )
                )
            }
        }

        if (layers.isNotEmpty()) {
            Text(
                text = "Resultado:",
                color = textColor.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelSmall
            )
            SegmentsWaveform(
                segments = mixed,
                color = accentColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor.copy(alpha = 0.15f))
                        .clickable(enabled = mixed.isNotEmpty()) { onFeelMix(mixed) }
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text("Sentir mezcla", color = accentColor, style = MaterialTheme.typography.labelMedium)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor)
                        .clickable(enabled = mixed.isNotEmpty()) { onUseMix(mixed) }
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text("Usar como base", color = Color.White, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
