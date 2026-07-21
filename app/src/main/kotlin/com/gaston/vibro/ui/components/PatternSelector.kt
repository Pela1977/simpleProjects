package com.gaston.vibro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gaston.vibro.haptics.PatternCategory
import com.gaston.vibro.haptics.VivroPattern

private const val TAB_CUSTOM = "MÍOS"

// Selector de patrones: tabs por categoría + grid de cards con mini-waveform.
// Los patrones custom viven en la tab MÍOS con opción de borrar.
@Composable
fun PatternSelector(
    presets: List<VivroPattern>,
    customs: List<VivroPattern>,
    selected: VivroPattern?,
    onSelect: (VivroPattern) -> Unit,
    onDeleteCustom: (VivroPattern) -> Unit,
    accentColor: Color,
    surfaceColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf(PatternCategory.SUAVE.name) }

    val tabs = PatternCategory.entries.map { it.name } + TAB_CUSTOM
    val visible: List<VivroPattern> = if (activeTab == TAB_CUSTOM) {
        customs
    } else {
        presets.filter { it.category.name == activeTab }
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tabs.forEach { tab ->
                val isActive = tab == activeTab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isActive) accentColor else surfaceColor)
                        .clickable { activeTab = tab }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab,
                        color = if (isActive) Color.White else textColor.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1
                    )
                }
            }
        }

        if (visible.isEmpty()) {
            Text(
                text = if (activeTab == TAB_CUSTOM)
                    "Todavía no creaste patrones. Andá a la pestaña Crear."
                else "Sin patrones en esta categoría.",
                color = textColor.copy(alpha = 0.5f),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        } else {
            visible.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row.forEach { pattern ->
                        PatternCard(
                            pattern = pattern,
                            isSelected = pattern.id == selected?.id,
                            isCustom = activeTab == TAB_CUSTOM,
                            onClick = { onSelect(pattern) },
                            onDelete = { onDeleteCustom(pattern) },
                            accentColor = accentColor,
                            surfaceColor = surfaceColor,
                            textColor = textColor,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun PatternCard(
    pattern: VivroPattern,
    isSelected: Boolean,
    isCustom: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    accentColor: Color,
    surfaceColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(14.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .background(if (isSelected) accentColor.copy(alpha = 0.16f) else surfaceColor)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) accentColor else textColor.copy(alpha = 0.1f),
                shape = shape
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = pattern.name,
                color = if (isSelected) accentColor else textColor,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )
            if (isCustom) {
                Text(
                    text = "✕",
                    color = textColor.copy(alpha = 0.45f),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onDelete)
                        .padding(horizontal = 6.dp)
                )
            }
        }
        WaveformPreview(
            pattern = pattern,
            color = if (isSelected) accentColor else textColor.copy(alpha = 0.6f),
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
        )
    }
}
