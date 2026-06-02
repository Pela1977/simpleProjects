package com.gaston.vibro.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gaston.vibro.haptics.HapticsConstants

@Composable
fun FrequencyControls(
    onTimeMs: Long,
    offTimeMs: Long,
    onOnTimeChange: (Long) -> Unit,
    onOffTimeChange: (Long) -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = Color(0xFFE8185C)
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FrequencySlider(
            label = "Pulso activo",
            value = onTimeMs,
            minValue = HapticsConstants.MIN_ON_TIME_MS,
            maxValue = HapticsConstants.MAX_ON_TIME_MS,
            onValueChange = onOnTimeChange,
            accentColor = accentColor
        )
        FrequencySlider(
            label = "Pausa",
            value = offTimeMs,
            minValue = HapticsConstants.MIN_OFF_TIME_MS,
            maxValue = HapticsConstants.MAX_OFF_TIME_MS,
            onValueChange = onOffTimeChange,
            accentColor = accentColor.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun FrequencySlider(
    label: String,
    value: Long,
    minValue: Long,
    maxValue: Long,
    onValueChange: (Long) -> Unit,
    accentColor: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "${value} ms",
                color = accentColor,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toLong()) },
            valueRange = minValue.toFloat()..maxValue.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = accentColor,
                activeTrackColor = accentColor,
                inactiveTrackColor = accentColor.copy(alpha = 0.2f)
            )
        )
    }
}
