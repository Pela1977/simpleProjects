package com.gaston.vibro.haptics

// Fractal / Pattern DNA: replica una secuencia base a múltiples escalas de
// tiempo y las intercala, generando complejidad autosimilar desde una forma simple.
object FractalExpander {

    const val MAX_DEPTH = 3

    // depth 1 = solo la base; 2 = base + eco rápido (×0.5);
    // 3 = eco rápido + base + versión expandida (×2, amplitud atenuada).
    fun expand(base: List<Segment>, depth: Int): List<Segment> {
        if (base.isEmpty()) return base
        val d = depth.coerceIn(1, MAX_DEPTH)
        val result = mutableListOf<Segment>()
        if (d >= 2) result.addAll(scale(base, 0.5f, 1.0f))
        result.addAll(base)
        if (d >= 3) result.addAll(scale(base, 2.0f, 0.7f))
        return SegmentTools.sanitize(result)
    }

    private fun scale(segments: List<Segment>, timeFactor: Float, ampFactor: Float): List<Segment> =
        segments.map { s ->
            Segment(
                durationMs = (s.durationMs * timeFactor).toLong()
                    .coerceIn(SegmentTools.MIN_SEGMENT_MS, SegmentTools.MAX_SEGMENT_MS),
                amplitude = if (s.amplitude == 0) 0
                else (s.amplitude * ampFactor).toInt()
                    .coerceIn(HapticsConstants.MIN_AMPLITUDE, HapticsConstants.MAX_AMPLITUDE)
            )
        }
}
