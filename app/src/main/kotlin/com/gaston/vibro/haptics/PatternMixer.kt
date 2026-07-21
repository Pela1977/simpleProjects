package com.gaston.vibro.haptics

// Layer Mixer: suma hasta 4 patrones como capas, cada una con su peso (0..1).
// Las amplitudes se muestrean a resolución fija, se suman y se clampean a 255.
object PatternMixer {

    const val MAX_LAYERS = 4
    private const val SAMPLE_MS = 20L

    data class Layer(val pattern: VivroPattern, val weight: Float)

    fun mix(layers: List<Layer>): List<Segment> {
        val active = layers.filter { it.weight > 0.01f }.take(MAX_LAYERS)
        if (active.isEmpty()) return emptyList()

        // Duración del mix = el ciclo más largo entre las capas (las cortas se repiten).
        val durations = active.map { layer -> layer.pattern.timings.sum().coerceAtLeast(SAMPLE_MS) }
        val totalMs = durations.max().coerceAtMost(8000L)
        val sampleCount = (totalMs / SAMPLE_MS).toInt().coerceAtLeast(1)

        val mixed = IntArray(sampleCount)
        active.forEachIndexed { layerIndex, layer ->
            val cycleMs = durations[layerIndex]
            for (i in 0 until sampleCount) {
                val t = (i * SAMPLE_MS) % cycleMs
                val amp = amplitudeAt(layer.pattern, t)
                mixed[i] = (mixed[i] + (amp * layer.weight).toInt())
                    .coerceAtMost(HapticsConstants.MAX_AMPLITUDE)
            }
        }

        return SegmentTools.sanitize(
            mixed.map { amp -> Segment(SAMPLE_MS, amp) }
        )
    }

    private fun amplitudeAt(pattern: VivroPattern, timeMs: Long): Int {
        var acc = 0L
        for (i in pattern.timings.indices) {
            acc += pattern.timings[i]
            if (timeMs < acc) return pattern.amplitudes[i]
        }
        return 0
    }
}
