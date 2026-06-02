package com.gaston.vibro.haptics

enum class PatternCategory { SUAVE, ASCENSO, CIMA }

enum class RampType(val displayName: String) {
    LINEAR_UP("Lineal ↑"),
    LINEAR_DOWN("Lineal ↓"),
    PARABOLA("Parábola"),
    HYPERBOLA("Hipérbola"),
    LOGARITHMIC("Logarítmica"),
    EXPONENTIAL("Exponencial")
}

class VivroPattern(
    val id: String,
    val name: String,
    val category: PatternCategory,
    val description: String,
    val timings: LongArray,
    val amplitudes: IntArray,
    // repeat: -1 = play once, 0 = loop from start, N = loop from step N
    val repeat: Int = 0
) {
    init {
        require(timings.size == amplitudes.size) {
            "Pattern '$id': timings.size (${timings.size}) must equal amplitudes.size (${amplitudes.size})"
        }
        require(amplitudes.none { it != 0 && it < HapticsConstants.MIN_AMPLITUDE }) {
            "Pattern '$id': non-zero amplitudes must be >= ${HapticsConstants.MIN_AMPLITUDE}"
        }
    }

    override fun equals(other: Any?): Boolean = other is VivroPattern && id == other.id
    override fun hashCode(): Int = id.hashCode()
    override fun toString(): String = "VivroPattern(id='$id', name='$name', category=$category)"
}
