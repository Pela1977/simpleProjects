package com.gaston.vibro.haptics

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class HapticsEngine(context: Context) {

    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        context.getSystemService(VibratorManager::class.java).defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Vibrator::class.java)!!
    }

    val hasAmplitudeControl: Boolean = vibrator.hasAmplitudeControl()

    fun play(
        pattern: VivroPattern,
        intensityLevel: Int,
        rampType: RampType,
        onTimeMs: Long = HapticsConstants.BASE_ON_TIME_MS,
        offTimeMs: Long = HapticsConstants.BASE_OFF_TIME_MS
    ) {
        val scaledTimings = scaleTimings(pattern, onTimeMs, offTimeMs)
        val effect = if (hasAmplitudeControl) {
            buildAmplitudeEffect(pattern, scaledTimings, intensityLevel, rampType)
        } else {
            buildPwmEffect(scaledTimings, pattern.amplitudes, intensityLevel, pattern.repeat)
        }
        vibrator.vibrate(effect)
    }

    fun stop() {
        vibrator.cancel()
    }

    private fun scaleTimings(
        pattern: VivroPattern,
        onTimeMs: Long,
        offTimeMs: Long
    ): LongArray {
        val onScale = onTimeMs.toFloat() / HapticsConstants.BASE_ON_TIME_MS
        val offScale = offTimeMs.toFloat() / HapticsConstants.BASE_OFF_TIME_MS
        return LongArray(pattern.timings.size) { i ->
            if (pattern.amplitudes[i] == 0) {
                (pattern.timings[i] * offScale).toLong()
                    .coerceIn(HapticsConstants.MIN_OFF_TIME_MS, HapticsConstants.MAX_OFF_TIME_MS)
            } else {
                (pattern.timings[i] * onScale).toLong()
                    .coerceIn(HapticsConstants.MIN_ON_TIME_MS, HapticsConstants.MAX_ON_TIME_MS)
            }
        }
    }

    private fun buildAmplitudeEffect(
        pattern: VivroPattern,
        scaledTimings: LongArray,
        intensityLevel: Int,
        rampType: RampType
    ): VibrationEffect {
        val maxAmplitude = HapticsConstants.AMPLITUDE_LEVELS[intensityLevel.coerceIn(0, 9)]
        val rampFn = RampFunctions.forType(rampType)
        val rampSteps = HapticsConstants.RAMP_STEPS
        val stepDuration = HapticsConstants.RAMP_STEP_DURATION_MS

        val rampTimings = LongArray(rampSteps) { stepDuration }
        val rampAmplitudes = IntArray(rampSteps) { step ->
            rampFn(step, rampSteps, maxAmplitude).coerceAtLeast(0)
        }

        val scaledAmplitudes = IntArray(pattern.amplitudes.size) { i ->
            val orig = pattern.amplitudes[i]
            if (orig == 0) 0
            else (orig.toFloat() / HapticsConstants.MAX_AMPLITUDE * maxAmplitude)
                .toInt().coerceAtLeast(HapticsConstants.MIN_AMPLITUDE)
        }

        val allTimings = LongArray(rampSteps + scaledTimings.size).also { dest ->
            rampTimings.copyInto(dest)
            scaledTimings.copyInto(dest, rampSteps)
        }
        val allAmplitudes = IntArray(rampSteps + scaledAmplitudes.size).also { dest ->
            rampAmplitudes.copyInto(dest)
            scaledAmplitudes.copyInto(dest, rampSteps)
        }
        // Offset repeat index past the ramp prefix so loops skip the fade-in
        val repeatIndex = if (pattern.repeat < 0) -1 else rampSteps + pattern.repeat

        return VibrationEffect.createWaveform(allTimings, allAmplitudes, repeatIndex)
    }

    private fun buildPwmEffect(
        scaledTimings: LongArray,
        amplitudes: IntArray,
        intensityLevel: Int,
        loopIndex: Int
    ): VibrationEffect {
        val dutyCycle = (intensityLevel + 1).toFloat() / HapticsConstants.LEVEL_COUNT
        val period = HapticsConstants.PWM_PERIOD_MS
        val onTime = (period * dutyCycle).toLong().coerceAtLeast(1L)
        val offTime = (period - onTime).coerceAtLeast(0L)

        val timingsList = mutableListOf<Long>()
        val amplitudesList = mutableListOf<Int>()

        scaledTimings.forEachIndexed { i, duration ->
            if (amplitudes[i] == 0) {
                timingsList.add(duration)
                amplitudesList.add(0)
            } else {
                // Expand on-period into rapid PWM cycles to simulate amplitude
                val cycles = (duration / period).coerceIn(1L, 30L).toInt()
                repeat(cycles) {
                    if (onTime > 0) {
                        timingsList.add(onTime)
                        amplitudesList.add(HapticsConstants.MAX_AMPLITUDE)
                    }
                    if (offTime > 0) {
                        timingsList.add(offTime)
                        amplitudesList.add(0)
                    }
                }
            }
        }

        val repeatIndex = if (loopIndex < 0) -1 else 0
        return VibrationEffect.createWaveform(
            timingsList.toLongArray(),
            amplitudesList.toIntArray(),
            repeatIndex
        )
    }
}
