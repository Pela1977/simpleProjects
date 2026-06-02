package com.gaston.vibro.haptics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class HapticsState(
    val isPlaying: Boolean = false,
    val activePattern: VivroPattern? = null,
    val intensityLevel: Int = 4,                          // 0-indexed (0 = level 1, 9 = level 10)
    val activeRamp: RampType = RampType.LINEAR_UP,
    val onTimeMs: Long = HapticsConstants.BASE_ON_TIME_MS,
    val offTimeMs: Long = HapticsConstants.BASE_OFF_TIME_MS
)

class HapticsViewModel(application: Application) : AndroidViewModel(application) {

    private val engine = HapticsEngine(application)

    private val _state = MutableStateFlow(HapticsState())
    val state: StateFlow<HapticsState> = _state.asStateFlow()

    val hasAmplitudeControl: Boolean = engine.hasAmplitudeControl

    fun selectPattern(pattern: VivroPattern) {
        _state.update { it.copy(activePattern = pattern) }
        if (_state.value.isPlaying) restartPlayback()
    }

    fun setIntensityLevel(level: Int) {
        _state.update { it.copy(intensityLevel = level.coerceIn(0, 9)) }
        if (_state.value.isPlaying) restartPlayback()
    }

    fun setRamp(ramp: RampType) {
        _state.update { it.copy(activeRamp = ramp) }
        if (_state.value.isPlaying) restartPlayback()
    }

    fun setOnTime(ms: Long) {
        _state.update {
            it.copy(onTimeMs = ms.coerceIn(
                HapticsConstants.MIN_ON_TIME_MS,
                HapticsConstants.MAX_ON_TIME_MS
            ))
        }
        if (_state.value.isPlaying) restartPlayback()
    }

    fun setOffTime(ms: Long) {
        _state.update {
            it.copy(offTimeMs = ms.coerceIn(
                HapticsConstants.MIN_OFF_TIME_MS,
                HapticsConstants.MAX_OFF_TIME_MS
            ))
        }
        if (_state.value.isPlaying) restartPlayback()
    }

    fun togglePlayback() {
        if (_state.value.isPlaying) stop() else play()
    }

    fun play() {
        val s = _state.value
        val pattern = s.activePattern ?: return
        engine.play(pattern, s.intensityLevel, s.activeRamp, s.onTimeMs, s.offTimeMs)
        _state.update { it.copy(isPlaying = true) }
    }

    fun stop() {
        engine.stop()
        _state.update { it.copy(isPlaying = false) }
    }

    private fun restartPlayback() {
        engine.stop()
        play()
    }

    override fun onCleared() {
        super.onCleared()
        engine.stop()
    }
}
