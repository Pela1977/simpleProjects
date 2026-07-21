package com.gaston.vibro.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.gaston.vibro.haptics.HapticsConstants
import com.gaston.vibro.haptics.Segment
import com.gaston.vibro.haptics.VivroPattern

// Dibuja la forma de onda de un patrón como barras verticales:
// ancho de barra proporcional a la duración, alto a la amplitud.
@Composable
fun WaveformPreview(
    pattern: VivroPattern,
    color: Color,
    modifier: Modifier = Modifier,
    silenceColor: Color = color.copy(alpha = 0.15f)
) {
    SegmentsWaveform(
        segments = pattern.timings.indices.map { i ->
            Segment(pattern.timings[i], pattern.amplitudes[i])
        },
        color = color,
        modifier = modifier,
        silenceColor = silenceColor
    )
}

@Composable
fun SegmentsWaveform(
    segments: List<Segment>,
    color: Color,
    modifier: Modifier = Modifier,
    silenceColor: Color = color.copy(alpha = 0.15f)
) {
    Canvas(modifier = modifier) {
        if (segments.isEmpty()) {
            drawLine(
                color = silenceColor,
                start = androidx.compose.ui.geometry.Offset(0f, size.height / 2f),
                end = androidx.compose.ui.geometry.Offset(size.width, size.height / 2f),
                strokeWidth = 2f
            )
            return@Canvas
        }
        val totalMs = segments.sumOf { it.durationMs }.coerceAtLeast(1L).toFloat()
        val gap = 1.5f
        var x = 0f
        segments.forEach { s ->
            val w = (s.durationMs / totalMs) * size.width
            if (s.amplitude > 0) {
                val h = (s.amplitude.toFloat() / HapticsConstants.MAX_AMPLITUDE) * size.height
                drawRoundRect(
                    color = color,
                    topLeft = androidx.compose.ui.geometry.Offset(x + gap / 2f, size.height - h),
                    size = androidx.compose.ui.geometry.Size((w - gap).coerceAtLeast(1f), h),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f)
                )
            } else {
                drawLine(
                    color = silenceColor,
                    start = androidx.compose.ui.geometry.Offset(x, size.height - 2f),
                    end = androidx.compose.ui.geometry.Offset(x + w, size.height - 2f),
                    strokeWidth = 3f
                )
            }
            x += w
        }
    }
}
