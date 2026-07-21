package com.gaston.vibro.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Ícono play (triángulo) y stop (cuadrado) dibujados a mano —
// sin dependencia de material-icons-extended.
@Composable
fun PlayStopButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    color: Color = Color(0xFFE8185C),
    iconColor: Color = Color.White
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Box(
        modifier = modifier
            .size(size)
            .scale(if (isPlaying) pulseScale else 1f)
            .clip(CircleShape)
            .background(color)
            .clickable(onClick = onClick)
            .semantics { contentDescription = if (isPlaying) "Detener" else "Reproducir" },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.38f)) {
            if (isPlaying) {
                drawRoundRect(
                    color = iconColor,
                    cornerRadius = CornerRadius(this.size.width * 0.12f)
                )
            } else {
                val w = this.size.width
                val h = this.size.height
                val path = Path().apply {
                    moveTo(w * 0.18f, 0f)
                    lineTo(w * 0.18f, h)
                    lineTo(w, h / 2f)
                    close()
                }
                drawPath(path, iconColor, style = Fill)
            }
        }
    }
}
