package com.gaston.vibro.haptics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gaston.vibro.data.CustomPatternRepository
import com.gaston.vibro.data.FavoriteConfig
import com.gaston.vibro.data.FavoritesRepository
import com.gaston.vibro.data.SkinRepository
import com.gaston.vibro.ui.theme.SkinRegistry
import com.gaston.vibro.ui.theme.VivroSkin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HapticsState(
    val isPlaying: Boolean = false,
    val isPreviewing: Boolean = false,
    val activePattern: VivroPattern? = null,
    val intensityLevel: Int = 4,                          // 0-indexed (0 = nivel 1, 9 = nivel 10)
    val activeRamp: RampType = RampType.LINEAR_UP,
    val onTimeMs: Long = HapticsConstants.BASE_ON_TIME_MS,
    val offTimeMs: Long = HapticsConstants.BASE_OFF_TIME_MS
)

class HapticsViewModel(application: Application) : AndroidViewModel(application) {

    private val engine = HapticsEngine(application)
    private val skinRepository = SkinRepository(application)
    private val favoritesRepository = FavoritesRepository(application)
    private val customPatternRepository = CustomPatternRepository(application)

    private val _state = MutableStateFlow(HapticsState())
    val state: StateFlow<HapticsState> = _state.asStateFlow()

    val hasAmplitudeControl: Boolean = engine.hasAmplitudeControl

    val skin: StateFlow<VivroSkin> = skinRepository.activeSkinId
        .map { SkinRegistry.byId(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, SkinRegistry.default)

    val favorites: StateFlow<List<FavoriteConfig>> = favoritesRepository.favorites
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val customPatterns: StateFlow<List<VivroPattern>> = customPatternRepository.patterns
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val allPatterns: StateFlow<List<VivroPattern>> =
        customPatterns.map { customs -> PatternsRegistry.all + customs }
            .stateIn(viewModelScope, SharingStarted.Eagerly, PatternsRegistry.all)

    // ── Reproducción ─────────────────────────────────────────────────────────

    fun selectPattern(pattern: VivroPattern) {
        _state.update { it.copy(activePattern = pattern) }
        if (_state.value.isPlaying) restartPlayback()
    }

    fun setIntensityLevel(level: Int) {
        _state.update { it.copy(intensityLevel = level.coerceIn(0, HapticsConstants.LEVEL_COUNT - 1)) }
        if (_state.value.isPlaying) restartPlayback()
    }

    fun setRamp(ramp: RampType) {
        _state.update { it.copy(activeRamp = ramp) }
        if (_state.value.isPlaying) restartPlayback()
    }

    fun setOnTime(ms: Long) {
        _state.update {
            it.copy(onTimeMs = ms.coerceIn(HapticsConstants.MIN_ON_TIME_MS, HapticsConstants.MAX_ON_TIME_MS))
        }
        if (_state.value.isPlaying) restartPlayback()
    }

    fun setOffTime(ms: Long) {
        _state.update {
            it.copy(offTimeMs = ms.coerceIn(HapticsConstants.MIN_OFF_TIME_MS, HapticsConstants.MAX_OFF_TIME_MS))
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
        _state.update { it.copy(isPlaying = true, isPreviewing = false) }
    }

    fun stop() {
        engine.stop()
        _state.update { it.copy(isPlaying = false, isPreviewing = false) }
    }

    private fun restartPlayback() {
        engine.stop()
        play()
    }

    // ── Preview del creador ──────────────────────────────────────────────────

    fun validateSegments(segments: List<Segment>): Boolean =
        SegmentTools.sanitize(segments).any { it.amplitude > 0 }

    fun previewSegments(segments: List<Segment>) {
        val pattern = SegmentTools.toPattern(
            segments, id = "preview", name = "Preview",
            description = "", category = PatternCategory.SUAVE
        ) ?: return
        engine.stop()
        engine.play(pattern, _state.value.intensityLevel, _state.value.activeRamp)
        _state.update { it.copy(isPlaying = false, isPreviewing = true) }
    }

    fun previewPattern(pattern: VivroPattern) {
        engine.stop()
        engine.play(pattern, _state.value.intensityLevel, _state.value.activeRamp)
        _state.update { it.copy(isPlaying = false, isPreviewing = true) }
    }

    fun stopPreview() {
        engine.stop()
        _state.update { it.copy(isPreviewing = false) }
    }

    // ── Skins ────────────────────────────────────────────────────────────────

    fun setSkin(skinId: String) {
        viewModelScope.launch { skinRepository.setActiveSkin(skinId) }
    }

    // ── Favoritos ────────────────────────────────────────────────────────────

    fun saveFavorite(name: String) {
        val s = _state.value
        val pattern = s.activePattern ?: return
        viewModelScope.launch {
            favoritesRepository.add(
                name = name.ifBlank { pattern.name },
                patternId = pattern.id,
                intensityLevel = s.intensityLevel,
                rampType = s.activeRamp,
                onTimeMs = s.onTimeMs,
                offTimeMs = s.offTimeMs
            )
        }
    }

    fun renameFavorite(id: String, newName: String) {
        viewModelScope.launch { favoritesRepository.rename(id, newName) }
    }

    fun deleteFavorite(id: String) {
        viewModelScope.launch { favoritesRepository.delete(id) }
    }

    fun applyFavorite(favorite: FavoriteConfig) {
        val pattern = allPatterns.value.find { it.id == favorite.patternId } ?: return
        engine.stop()
        _state.update {
            it.copy(
                activePattern = pattern,
                intensityLevel = favorite.intensityLevel,
                activeRamp = favorite.rampType,
                onTimeMs = favorite.onTimeMs,
                offTimeMs = favorite.offTimeMs,
                isPlaying = false,
                isPreviewing = false
            )
        }
        play()
    }

    // ── Patrones custom ──────────────────────────────────────────────────────

    fun saveCustomPattern(
        name: String,
        description: String,
        category: PatternCategory,
        segments: List<Segment>
    ) {
        val clean = SegmentTools.sanitize(segments)
        if (clean.none { it.amplitude > 0 }) return
        viewModelScope.launch {
            customPatternRepository.add(
                name = name.ifBlank { "Mi patrón" },
                description = description,
                category = category,
                timings = clean.map { it.durationMs }.toLongArray(),
                amplitudes = clean.map { it.amplitude }.toIntArray()
            )
        }
    }

    fun deleteCustomPattern(id: String) {
        if (_state.value.activePattern?.id == id) {
            stop()
            _state.update { it.copy(activePattern = PatternsRegistry.all.first()) }
        }
        viewModelScope.launch { customPatternRepository.delete(id) }
    }

    override fun onCleared() {
        super.onCleared()
        engine.stop()
    }
}
