package com.gaston.vibro.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gaston.vibro.haptics.RampFunctions
import com.gaston.vibro.haptics.RampType

// Grid 2×3 de rampas, cada chip con la curva real dibujada
// (muestreada de la misma RampFunction que usa el engine).
@Composable
fun RampSelector(
    selected: RampType,
    onSelect: (RampType) -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = Color(0xFFE8185C),
    surfaceColor: Color = Color(0x22FFFFFF),
    textColor: Color = Color.White
) {
    val ramps = RampType.entries.toList()
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Rampa de transición",
            color = textColor.copy(alpha = 0.7f),
            style = MaterialTheme.typography.labelMedium
        )
        ramps.chunked(3).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { ramp ->
                    RampChip(
                        ramp = ramp,
                        isSelected = ramp == selected,
                        onClick = { onSelect(ramp) },
                        accentColor = accentColor,
                        surfaceColor = surfaceColor,
                        textColor = textColor,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun RampChip(
    ramp: RampType,
    isSelected: Boolean,
    onClick: () -> Unit,
    accentColor: Color,
    surfaceColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(14.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .background(if (isSelected) accentColor.copy(alpha = 0.18f) else surfaceColor)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) accentColor else textColor.copy(alpha = 0.12f),
                shape = shape
            )
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            val fn = RampFunctions.forType(ramp)
            val steps = 24
            val path = Path()
            for (i in 0..steps) {
                val amp = fn(i, steps, 255)
                val x = (i.toFloat() / steps) * size.width
                val y = size.height - (amp / 255f) * size.height
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            drawPath(
                path = path,
                color = if (isSelected) accentColor else textColor.copy(alpha = 0.55f),
                style = Stroke(width = 4f)
            )
            drawLine(
                color = textColor.copy(alpha = 0.1f),
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = 2f
            )
        }
        Text(
            text = ramp.displayName,
            color = if (isSelected) accentColor else textColor.copy(alpha = 0.75f),
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
