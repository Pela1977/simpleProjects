package com.gaston.vibro.ui.creator

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.gaston.vibro.haptics.HapticsConstants
import com.gaston.vibro.haptics.Segment
import kotlin.math.abs

private enum class CanvasMode { DRAW, EDIT }

// Whiteboard: el dedo dibuja la envolvente de amplitud (X = tiempo, Y = intensidad).
// Al soltar, se detectan los puntos clave y quedan nodos arrastrables;
// entre nodos la curva se interpola con Catmull-Rom.
// EDIT: arrastrar mueve un nodo · tap agrega nodo · tap largo elimina.
@Composable
fun WaveformCanvas(
    accentColor: Color,
    surfaceColor: Color,
    textColor: Color,
    onSegmentsChange: (List<Segment>) -> Unit,
    modifier: Modifier = Modifier
) {
    var mode by remember { mutableStateOf(CanvasMode.DRAW) }
    val rawPoints = remember { mutableStateListOf<Offset>() }   // normalizados 0..1
    val nodes = remember { mutableStateListOf<Offset>() }       // normalizados 0..1
    var durationMs by remember { mutableStateOf(2000f) }

    fun emitSegments() {
        onSegmentsChange(nodesToSegments(nodes.toList(), durationMs.toLong()))
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = if (mode == CanvasMode.DRAW)
                "Dibujá la curva con el dedo — arriba es más intenso, abajo es silencio"
            else
                "Arrastrá los nodos · tocá para agregar · mantené presionado para borrar",
            color = textColor.copy(alpha = 0.65f),
            style = MaterialTheme.typography.bodySmall
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(surfaceColor)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(mode) {
                        if (mode == CanvasMode.DRAW) {
                            detectDragGestures(
                                onDragStart = { pos ->
                                    rawPoints.clear()
                                    rawPoints.add(normalize(pos, size.width, size.height))
                                },
                                onDrag = { change, _ ->
                                    val p = normalize(change.position, size.width, size.height)
                                    val last = rawPoints.lastOrNull()
                                    if (last == null || p.x > last.x + 0.002f) rawPoints.add(p)
                                },
                                onDragEnd = {
                                    if (rawPoints.size >= 2) {
                                        nodes.clear()
                                        nodes.addAll(extractKeyNodes(rawPoints.toList()))
                                        mode = CanvasMode.EDIT
                                        emitSegments()
                                    }
                                }
                            )
                        } else {
                            var dragIndex = -1
                            detectDragGestures(
                                onDragStart = { pos ->
                                    val p = normalize(pos, size.width, size.height)
                                    dragIndex = nearestNode(nodes, p, threshold = 0.08f)
                                },
                                onDrag = { change, _ ->
                                    if (dragIndex >= 0) {
                                        val p = normalize(change.position, size.width, size.height)
                                        val minX = if (dragIndex == 0) 0f
                                            else nodes[dragIndex - 1].x + 0.01f
                                        val maxX = if (dragIndex == nodes.size - 1) 1f
                                            else nodes[dragIndex + 1].x - 0.01f
                                        nodes[dragIndex] = Offset(
                                            p.x.coerceIn(minX, maxX),
                                            p.y.coerceIn(0f, 1f)
                                        )
                                    }
                                },
                                onDragEnd = {
                                    dragIndex = -1
                                    emitSegments()
                                }
                            )
                        }
                    }
                    .pointerInput(mode) {
                        if (mode == CanvasMode.EDIT) {
                            detectTapGestures(
                                onTap = { pos ->
                                    val p = normalize(pos, size.width, size.height)
                                    if (nearestNode(nodes, p, threshold = 0.06f) < 0) {
                                        val insertAt = nodes.indexOfFirst { it.x > p.x }
                                            .let { if (it < 0) nodes.size else it }
                                        nodes.add(insertAt, p)
                                        emitSegments()
                                    }
                                },
                                onLongPress = { pos ->
                                    val p = normalize(pos, size.width, size.height)
                                    val idx = nearestNode(nodes, p, threshold = 0.08f)
                                    if (idx >= 0 && nodes.size > 2) {
                                        nodes.removeAt(idx)
                                        emitSegments()
                                    }
                                }
                            )
                        }
                    }
            ) {
                // Grid de referencia
                for (i in 1..3) {
                    val y = size.height * i / 4f
                    drawLine(textColor.copy(alpha = 0.08f), Offset(0f, y), Offset(size.width, y), 2f)
                }
                for (i in 1..7) {
                    val x = size.width * i / 8f
                    drawLine(textColor.copy(alpha = 0.08f), Offset(x, 0f), Offset(x, size.height), 2f)
                }

                if (mode == CanvasMode.DRAW && rawPoints.size >= 2) {
                    val path = Path()
                    rawPoints.forEachIndexed { i, p ->
                        val px = p.x * size.width
                        val py = p.y * size.height
                        if (i == 0) path.moveTo(px, py) else path.lineTo(px, py)
                    }
                    drawPath(path, accentColor, style = Stroke(width = 5f))
                }

                if (mode == CanvasMode.EDIT && nodes.size >= 2) {
                    val samples = sampleCatmullRom(nodes.toList(), subSamples = 8)
                    val path = Path()
                    samples.forEachIndexed { i, p ->
                        val px = p.x.coerceIn(0f, 1f) * size.width
                        val py = p.y.coerceIn(0f, 1f) * size.height
                        if (i == 0) path.moveTo(px, py) else path.lineTo(px, py)
                    }
                    drawPath(path, accentColor, style = Stroke(width = 5f))

                    nodes.forEach { n ->
                        drawCircle(
                            color = accentColor,
                            radius = 14f,
                            center = Offset(n.x * size.width, n.y * size.height)
                        )
                        drawCircle(
                            color = surfaceColor,
                            radius = 7f,
                            center = Offset(n.x * size.width, n.y * size.height)
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Duración del ciclo: ${durationMs.toInt()} ms",
                color = textColor.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "Redibujar",
                color = accentColor,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        rawPoints.clear()
                        nodes.clear()
                        mode = CanvasMode.DRAW
                        onSegmentsChange(emptyList())
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            )
        }
        Slider(
            value = durationMs,
            onValueChange = { durationMs = it },
            onValueChangeFinished = { emitSegments() },
            valueRange = 500f..5000f,
            colors = SliderDefaults.colors(
                thumbColor = accentColor,
                activeTrackColor = accentColor,
                inactiveTrackColor = accentColor.copy(alpha = 0.2f)
            )
        )
    }
}

private fun normalize(pos: Offset, width: Int, height: Int): Offset = Offset(
    (pos.x / width.coerceAtLeast(1)).coerceIn(0f, 1f),
    (pos.y / height.coerceAtLeast(1)).coerceIn(0f, 1f)
)

private fun nearestNode(nodes: List<Offset>, p: Offset, threshold: Float): Int {
    var best = -1
    var bestDist = threshold
    nodes.forEachIndexed { i, n ->
        val d = (n - p).getDistance()
        if (d < bestDist) {
            best = i
            bestDist = d
        }
    }
    return best
}

// Detecta puntos clave del trazo libre: extremos, picos y valles.
private fun extractKeyNodes(raw: List<Offset>): List<Offset> {
    if (raw.size <= 3) return raw
    val keys = mutableListOf(raw.first())
    for (i in 1 until raw.size - 1) {
        val prev = raw[i - 1]
        val curr = raw[i]
        val next = raw[i + 1]
        val isPeak = curr.y < prev.y && curr.y <= next.y
        val isValley = curr.y > prev.y && curr.y >= next.y
        if ((isPeak || isValley) &&
            abs(curr.x - keys.last().x) > 0.035f &&
            abs(curr.y - keys.last().y) > 0.02f
        ) {
            keys.add(curr)
        }
    }
    if (abs(raw.last().x - keys.last().x) > 0.01f) keys.add(raw.last())
    // Cap: si quedaron demasiados, quedarse con una muestra pareja
    return if (keys.size <= 14) keys
    else keys.filterIndexed { i, _ -> i == 0 || i == keys.size - 1 || i % (keys.size / 12 + 1) == 0 }
}

// Interpola la polilínea de nodos con Catmull-Rom (pasa exacto por cada nodo).
private fun sampleCatmullRom(nodes: List<Offset>, subSamples: Int): List<Offset> {
    if (nodes.size < 2) return nodes
    val out = mutableListOf<Offset>()
    for (i in 0 until nodes.size - 1) {
        val p0 = nodes.getOrElse(i - 1) { nodes[i] }
        val p1 = nodes[i]
        val p2 = nodes[i + 1]
        val p3 = nodes.getOrElse(i + 2) { nodes[i + 1] }
        for (s in 0 until subSamples) {
            val t = s.toFloat() / subSamples
            out.add(catmullRom(p0, p1, p2, p3, t))
        }
    }
    out.add(nodes.last())
    return out
}

private fun catmullRom(p0: Offset, p1: Offset, p2: Offset, p3: Offset, t: Float): Offset {
    val t2 = t * t
    val t3 = t2 * t
    fun c(v0: Float, v1: Float, v2: Float, v3: Float): Float =
        0.5f * ((2f * v1) + (-v0 + v2) * t +
            (2f * v0 - 5f * v1 + 4f * v2 - v3) * t2 +
            (-v0 + 3f * v1 - 3f * v2 + v3) * t3)
    return Offset(c(p0.x, p1.x, p2.x, p3.x), c(p0.y, p1.y, p2.y, p3.y))
}

// Convierte la curva de nodos en segmentos: muestrea la amplitud a intervalos
// uniformes de tiempo. Y invertida: arriba (y=0) es amplitud máxima.
private fun nodesToSegments(nodes: List<Offset>, totalMs: Long): List<Segment> {
    if (nodes.size < 2) return emptyList()
    val samples = sampleCatmullRom(nodes, subSamples = 6)
    if (samples.isEmpty()) return emptyList()
    val stepMs = (totalMs / samples.size).coerceAtLeast(10L)
    return samples.map { p ->
        val amp = ((1f - p.y.coerceIn(0f, 1f)) * HapticsConstants.MAX_AMPLITUDE).toInt()
        Segment(stepMs, if (amp < 12) 0 else amp)
    }
}
