package com.gaston.vibro.haptics

import kotlin.math.exp
import kotlin.math.ln

typealias RampFunction = (step: Int, totalSteps: Int, maxAmplitude: Int) -> Int

object RampFunctions {

    // Amplitude grows linearly 0 → max
    val LINEAR_UP: RampFunction = { step, totalSteps, maxAmplitude ->
        (maxAmplitude * step / totalSteps).coerceAtLeast(0)
    }

    // Amplitude shrinks linearly max → 0 (fade-out as attack shape)
    val LINEAR_DOWN: RampFunction = { step, totalSteps, maxAmplitude ->
        (maxAmplitude * (totalSteps - step) / totalSteps).coerceAtLeast(0)
    }

    // Slow start, accelerating end (x²)
    val PARABOLA: RampFunction = { step, totalSteps, maxAmplitude ->
        val t = step.toFloat() / totalSteps
        (maxAmplitude * t * t).toInt().coerceAtLeast(0)
    }

    // Fast start, decelerating plateau (1-(1-x)²)
    val HYPERBOLA: RampFunction = { step, totalSteps, maxAmplitude ->
        val t = step.toFloat() / totalSteps
        (maxAmplitude * (1f - (1f - t) * (1f - t))).toInt().coerceAtLeast(0)
    }

    // Quick rise then long plateau (ln)
    val LOGARITHMIC: RampFunction = { step, totalSteps, maxAmplitude ->
        val logMax = ln(totalSteps.toFloat() + 1f)
        val logStep = ln(step.toFloat() + 1f)
        (maxAmplitude * logStep / logMax).toInt().coerceAtLeast(0)
    }

    // Slow then dramatic surge at the end (e^x)
    val EXPONENTIAL: RampFunction = { step, totalSteps, maxAmplitude ->
        val t = step.toFloat() / totalSteps
        val k = 3.0
        val scaled = (exp(k * t) - 1.0) / (exp(k) - 1.0)
        (maxAmplitude * scaled).toInt().coerceIn(0, maxAmplitude)
    }

    fun forType(type: RampType): RampFunction = when (type) {
        RampType.LINEAR_UP   -> LINEAR_UP
        RampType.LINEAR_DOWN -> LINEAR_DOWN
        RampType.PARABOLA    -> PARABOLA
        RampType.HYPERBOLA   -> HYPERBOLA
        RampType.LOGARITHMIC -> LOGARITHMIC
        RampType.EXPONENTIAL -> EXPONENTIAL
    }
}
