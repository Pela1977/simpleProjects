package com.gaston.vibro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun IntensitySlider(
    level: Int,             // 0-indexed (0 = nivel 1, 9 = nivel 10)
    onLevelChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    activeColor: Color = Color(0xFFE8185C),
    inactiveColor: Color = Color(0x33E8185C)
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Intensidad — Nivel ${level + 1}",
            color = Color.White.copy(alpha = 0.7f),
            style = MaterialTheme.typography.labelMedium
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0..9) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(if (i <= level) activeColor else inactiveColor)
                        .clickable { onLevelChange(i) }
                        .semantics { contentDescription = "Nivel ${i + 1}" }
                )
            }
        }
    }
}
