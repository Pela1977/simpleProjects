package com.gaston.vibro.ui.creator

import android.os.SystemClock
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.gaston.vibro.haptics.HapticsConstants
import com.gaston.vibro.haptics.Segment
import com.gaston.vibro.haptics.SegmentTools

private val SPEED_OPTIONS = listOf(0.25f, 0.5f, 1f, 2f)

// Tap-to-record: grabá el ritmo tocando el pad. La duración del toque es el
// pulso; el silencio entre toques es la pausa. Con velocidad < 1× grabás en
// cámara lenta y los tiempos se normalizan al soltar (precisión sin apuro).
@Composable
fun TapRecorder(
    accentColor: Color,
    surfaceColor: Color,
    textColor: Color,
    segments: SnapshotStateList<Segment>,
    modifier: Modifier = Modifier
) {
    var speed by remember { mutableStateOf(1f) }
    var amplitude by remember { mutableStateOf(180f) }
    var lastUpUptime by remember { mutableStateOf<Long?>(null) }
    var isPressed by remember { mutableStateOf(false) }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Velocidad de captura — con 0.5× grabás al doble de tiempo y el patrón queda a velocidad real",
            color = textColor.copy(alpha = 0.65f),
            style = MaterialTheme.typography.bodySmall
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SPEED_OPTIONS.forEach { option ->
                val isActive = option == speed
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(18.dp))
                        .background(if (isActive) accentColor else surfaceColor)
                        .clickable { speed = option }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${option}×",
                        color = if (isActive) Color.White else textColor.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (isPressed) accentColor.copy(alpha = 0.35f) else surfaceColor)
                .pointerInput(speed, amplitude) {
                    awaitEachGesture {
                        awaitFirstDown()
                        val downAt = SystemClock.uptimeMillis()
                        isPressed = true
                        // El silencio previo se registra desde el toque anterior
                        val lastUp = lastUpUptime
                        if (lastUp != null) {
                            val gapMs = ((downAt - lastUp) * speed).toLong()
                            // Un gap enorme es una distracción, no una pausa del ritmo
                            if (gapMs in SegmentTools.MIN_SEGMENT_MS..SegmentTools.MAX_SEGMENT_MS) {
                                segments.add(Segment(gapMs, 0))
                            }
                        }
                        waitForUpOrCancellation()
                        val upAt = SystemClock.uptimeMillis()
                        isPressed = false
                        val pressMs = ((upAt - downAt) * speed).toLong()
                            .coerceIn(SegmentTools.MIN_SEGMENT_MS, SegmentTools.MAX_SEGMENT_MS)
                        segments.add(Segment(pressMs, amplitude.toInt()))
                        lastUpUptime = upAt
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (segments.isEmpty()) "Tocá acá para grabar tu ritmo"
                    else "${segments.count { it.amplitude > 0 }} pulsos · ${SegmentTools.totalDurationMs(segments)} ms",
                color = textColor.copy(alpha = 0.75f),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Text(
            text = "Intensidad del pulso: ${amplitude.toInt()}",
            color = textColor.copy(alpha = 0.7f),
            style = MaterialTheme.typography.labelSmall
        )
        Slider(
            value = amplitude,
            onValueChange = { amplitude = it },
            valueRange = HapticsConstants.MIN_AMPLITUDE.toFloat()..HapticsConstants.MAX_AMPLITUDE.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = accentColor,
                activeTrackColor = accentColor,
                inactiveTrackColor = accentColor.copy(alpha = 0.2f)
            )
        )

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Deshacer toque",
                color = accentColor,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        // Quita el último pulso y la pausa que lo precedía
                        if (segments.isNotEmpty()) segments.removeAt(segments.size - 1)
                        if (segments.isNotEmpty() && segments.last().amplitude == 0) {
                            segments.removeAt(segments.size - 1)
                        }
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            )
            Text(
                text = "Limpiar",
                color = textColor.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        segments.clear()
                        lastUpUptime = null
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            )
        }
    }
}
