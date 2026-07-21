package com.gaston.vibro.haptics

// Unidad editable del creador de patrones: un tramo de vibración (o silencio).
data class Segment(
    val durationMs: Long,
    val amplitude: Int // 0 = silencio, 20..255 = vibración
)

object SegmentTools {

    const val MIN_SEGMENT_MS = HapticsConstants.MIN_TIMING_MS
    const val MAX_SEGMENT_MS = HapticsConstants.MAX_TIMING_MS

    // Sanea y convierte una lista de segmentos en un VivroPattern válido.
    // Devuelve null si no queda ningún tramo audible.
    fun toPattern(
        segments: List<Segment>,
        id: String,
        name: String,
        description: String,
        category: PatternCategory
    ): VivroPattern? {
        val clean = sanitize(segments)
        if (clean.none { it.amplitude > 0 }) return null
        return VivroPattern(
            id = id,
            name = name,
            category = category,
            description = description,
            timings = clean.map { it.durationMs }.toLongArray(),
            amplitudes = clean.map { it.amplitude }.toIntArray(),
            repeat = 0
        )
    }

    // Clampea duraciones/amplitudes y fusiona tramos consecutivos equivalentes.
    fun sanitize(segments: List<Segment>): List<Segment> {
        val clamped = segments
            .filter { it.durationMs >= MIN_SEGMENT_MS / 2 }
            .map { s ->
                val amp = when {
                    s.amplitude < HapticsConstants.MIN_AMPLITUDE / 2 -> 0
                    s.amplitude < HapticsConstants.MIN_AMPLITUDE -> HapticsConstants.MIN_AMPLITUDE
                    else -> s.amplitude.coerceAtMost(HapticsConstants.MAX_AMPLITUDE)
                }
                Segment(s.durationMs.coerceIn(MIN_SEGMENT_MS, MAX_SEGMENT_MS), amp)
            }
        val merged = mutableListOf<Segment>()
        clamped.forEach { s ->
            val last = merged.lastOrNull()
            if (last != null &&
                ((last.amplitude == 0 && s.amplitude == 0) ||
                 (last.amplitude > 0 && s.amplitude > 0 && kotlin.math.abs(last.amplitude - s.amplitude) <= 6))
            ) {
                merged[merged.size - 1] = Segment(
                    (last.durationMs + s.durationMs).coerceAtMost(MAX_SEGMENT_MS),
                    if (s.amplitude == 0) 0 else (last.amplitude + s.amplitude) / 2
                )
            } else {
                merged.add(s)
            }
        }
        return merged
    }

    fun fromPattern(pattern: VivroPattern): List<Segment> =
        pattern.timings.indices.map { i -> Segment(pattern.timings[i], pattern.amplitudes[i]) }

    fun totalDurationMs(segments: List<Segment>): Long = segments.sumOf { it.durationMs }
}
