package com.gaston.vibro.haptics

object HapticsConstants {
    const val MAX_AMPLITUDE = 255
    const val MIN_AMPLITUDE = 20
    const val LEVEL_COUNT = 10

    const val BASE_ON_TIME_MS = 100L
    const val BASE_OFF_TIME_MS = 100L
    const val MIN_ON_TIME_MS = 30L
    const val MAX_ON_TIME_MS = 800L
    const val MIN_OFF_TIME_MS = 10L
    const val MAX_OFF_TIME_MS = 800L

    // Prepended ramp: N steps × STEP_DURATION_MS each
    const val RAMP_STEPS = 8
    const val RAMP_STEP_DURATION_MS = 25L

    // PWM period for devices without amplitude control
    const val PWM_PERIOD_MS = 16L

    // Amplitude at each intensity level (index 0 = level 1, index 9 = level 10)
    val AMPLITUDE_LEVELS: IntArray = IntArray(LEVEL_COUNT) { i ->
        MIN_AMPLITUDE + ((MAX_AMPLITUDE - MIN_AMPLITUDE) * i / (LEVEL_COUNT - 1))
    }
}
