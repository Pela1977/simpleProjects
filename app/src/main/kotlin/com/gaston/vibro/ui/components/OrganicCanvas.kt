package com.gaston.vibro.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import com.gaston.vibro.ui.theme.CanvasStyle
import com.gaston.vibro.ui.theme.VivroSkin
import kotlin.math.cos
import kotlin.math.sin

// Canvas central animado. La forma depende del skin activo; el pulso
// se sincroniza con el estado de reproducción y la intensidad.
@Composable
fun OrganicCanvas(
    skin: VivroSkin,
    isPlaying: Boolean,
    intensityLevel: Int, // 0..9
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "organic")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(14000, easing = LinearEasing)),
        label = "phase"
    )
    val pulse by transition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            tween(if (isPlaying) 450 else 2200),
            RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val intensityFactor = 0.75f + (intensityLevel / 9f) * 0.35f
    val scale = if (isPlaying) pulse * intensityFactor else 0.9f

    Canvas(modifier = modifier) {
        val radius = size.minDimension / 2f * 0.82f * scale
        when (skin.canvasStyle) {
            CanvasStyle.ORCHID -> drawOrchid(skin, radius, phase)
            CanvasStyle.SILK -> drawSilk(skin, radius, phase)
            CanvasStyle.DAWN -> drawDawn(skin, radius, phase)
            CanvasStyle.PEARL -> drawDawn(skin, radius, phase)
            CanvasStyle.EMBER -> drawEmber(skin, radius, phase, isPlaying)
            CanvasStyle.NOIR -> drawNoir(skin, radius, phase, isPlaying)
        }
    }
}

// Cinco pétalos rotados con gradiente radial fucsia + centro dorado.
private fun DrawScope.drawOrchid(skin: VivroSkin, radius: Float, phase: Float) {
    val c = center
    val petals = 5
    for (i in 0 until petals) {
        rotate(degrees = i * (360f / petals) + phase * 0.4f, pivot = c) {
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(
                        skin.colors.primary.copy(alpha = 0.45f),
                        skin.colors.accent.copy(alpha = 0.12f),
                        skin.colors.primary.copy(alpha = 0f)
                    ),
                    center = c,
                    radius = radius
                ),
                topLeft = Offset(c.x - radius * 0.32f, c.y - radius),
                size = Size(radius * 0.64f, radius * 1.15f)
            )
        }
    }
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                skin.colors.secondary.copy(alpha = 0.85f),
                skin.colors.secondary.copy(alpha = 0f)
            ),
            center = c,
            radius = radius * 0.24f
        ),
        radius = radius * 0.24f,
        center = c
    )
}

// Óvalos concéntricos cálidos, apenas rotados — seda superpuesta.
private fun DrawScope.drawSilk(skin: VivroSkin, radius: Float, phase: Float) {
    val c = center
    val layers = 6
    for (i in layers downTo 1) {
        val f = i.toFloat() / layers
        rotate(degrees = phase * 0.25f * i, pivot = c) {
            drawOval(
                color = skin.colors.primary.copy(alpha = 0.10f + 0.06f * (layers - i)),
                topLeft = Offset(c.x - radius * f, c.y - radius * f * 0.72f),
                size = Size(radius * 2f * f, radius * 1.44f * f)
            )
        }
    }
    drawCircle(
        color = skin.colors.secondary.copy(alpha = 0.5f),
        radius = radius * 0.14f,
        center = c
    )
}

// Halos suaves y luminosos para skins claros.
private fun DrawScope.drawDawn(skin: VivroSkin, radius: Float, phase: Float) {
    val c = center
    val rings = 4
    for (i in rings downTo 1) {
        val f = i.toFloat() / rings
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    skin.colors.primary.copy(alpha = 0.22f * (1.1f - f)),
                    skin.colors.accent.copy(alpha = 0.05f)
                ),
                center = c,
                radius = radius * f
            ),
            radius = radius * f,
            center = c
        )
    }
    // Tres satélites orbitando lento
    for (i in 0 until 3) {
        val angle = Math.toRadians((phase + i * 120f).toDouble())
        val orbit = radius * 0.68f
        drawCircle(
            color = skin.colors.secondary.copy(alpha = 0.55f),
            radius = radius * 0.05f,
            center = Offset(
                c.x + (orbit * cos(angle)).toFloat(),
                c.y + (orbit * sin(angle)).toFloat()
            )
        )
    }
}

// Explícito: anillos de choque que se expanden desde el centro.
private fun DrawScope.drawEmber(skin: VivroSkin, radius: Float, phase: Float, isPlaying: Boolean) {
    val c = center
    val rings = 5
    val speed = if (isPlaying) 3f else 1f
    for (i in 0 until rings) {
        val progress = (((phase * speed) / 360f + i.toFloat() / rings) % 1f)
        drawCircle(
            color = skin.colors.primary.copy(alpha = (1f - progress) * 0.5f),
            radius = radius * progress,
            center = c,
            style = Stroke(width = 6f + 10f * (1f - progress))
        )
    }
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(skin.colors.accent, skin.colors.primary.copy(alpha = 0f)),
            center = c,
            radius = radius * 0.3f
        ),
        radius = radius * 0.3f,
        center = c
    )
}

// Explícito: rayos radiales pulsantes sobre negro absoluto.
private fun DrawScope.drawNoir(skin: VivroSkin, radius: Float, phase: Float, isPlaying: Boolean) {
    val c = center
    val rays = 12
    for (i in 0 until rays) {
        val angle = Math.toRadians((i * (360.0 / rays) + phase).toDouble())
        val wobble = if (isPlaying) 0.75f + 0.25f * sin(Math.toRadians(phase * 4.0 + i)).toFloat() else 0.85f
        val inner = radius * 0.3f
        val outer = radius * wobble
        drawLine(
            brush = Brush.linearGradient(
                colors = listOf(
                    skin.colors.primary.copy(alpha = 0.8f),
                    skin.colors.secondary.copy(alpha = 0f)
                ),
                start = Offset(
                    c.x + (inner * cos(angle)).toFloat(),
                    c.y + (inner * sin(angle)).toFloat()
                ),
                end = Offset(
                    c.x + (outer * cos(angle)).toFloat(),
                    c.y + (outer * sin(angle)).toFloat()
                )
            ),
            start = Offset(
                c.x + (inner * cos(angle)).toFloat(),
                c.y + (inner * sin(angle)).toFloat()
            ),
            end = Offset(
                c.x + (outer * cos(angle)).toFloat(),
                c.y + (outer * sin(angle)).toFloat()
            ),
            strokeWidth = 5f
        )
    }
    drawCircle(
        color = skin.colors.primary.copy(alpha = 0.9f),
        radius = radius * 0.12f,
        center = c
    )
}
