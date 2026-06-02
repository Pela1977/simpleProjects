# PROGRESS.md — simpleProjects / Vibro

> Actualizar al inicio Y fin de cada tarea. Sin este registro la tarea no está terminada.

---

## M0 — Fundaciones PM

[X] CLAUDE.md creado con contexto completo
[X] CONVENTIONS.md v1 creado
[X] PROGRESS.md inicializado
[X] ROADMAP.md creado
[X] `.claude/agents/haptics-engineer.md` creado
[X] `.claude/agents/compose-ui-designer.md` creado
[X] `.claude/agents/android-architect.md` creado
[X] `.claude/skills/pattern-composer/SKILL.md` creado
[X] `.claude/skills/progress-log/SKILL.md` creado
[X] Commit inicial pusheado a rama `claude/vibration-motor-app-plan-V69Qq`

> Completado por: claude (setup) | 2026-06-02

---

## M1 — Android project scaffold

[X] `settings.gradle.kts`
[X] `build.gradle.kts` (root)
[X] `gradle/libs.versions.toml` — AGP 8.7.3, Kotlin 2.0.21, Compose BOM, DataStore, Google Fonts
[X] `gradle/wrapper/gradle-wrapper.properties` — Gradle 8.9
[X] `app/build.gradle.kts` — minSdk 26, targetSdk 35
[X] `app/src/main/AndroidManifest.xml` — permiso VIBRATE, portrait locked
[X] `app/src/main/kotlin/com/gaston/vibro/MainActivity.kt`
[X] `app/src/main/kotlin/com/gaston/vibro/ui/theme/VivroTheme.kt`
[X] `app/src/main/res/values/strings.xml` y `themes.xml`
[X] `app/proguard-rules.pro`
[ ] Smoke test: `./gradlew assembleDebug` → BUILD SUCCESSFUL *(checkpoint manual Gastón)*

> Completado por: android-architect | 2026-06-02

---

## Naming de patrones — definitivo

[X] v3 FINAL: tres categorías por intensidad, 21 patrones

**SUAVE (7):** Caricia · Susurro · Roce · Murmullo · Latido · Onda · Deriva
**ASCENSO (7):** Oleada · Pulso · Marea · Vértigo · Espiral · Tormenta · Tsunami
**CIMA (7):** Pulse Nova · Big Bang · Earthquake · Volcano · Supernova · Singularity · Aftershock

> Cerrado por: Gastón | 2026-06-02

---

## M2 — Motor háptico nativo

[X] `HapticsConstants.kt`
[X] `VivroPattern.kt` + `PatternCategory` + `RampType`
[X] `RampFunctions.kt` — 6 curvas matemáticas
[X] `HapticsEngine.kt` — amplitude real + fallback PWM
[X] `HapticsViewModel.kt` — StateFlow<HapticsState>
[ ] Smoke test háptico *(checkpoint manual Gastón)*

> Completado por: haptics-engineer | 2026-06-02

---

## M3 — Controles UI básicos

[X] `IntensitySlider.kt` — 10 dots táctiles
[X] `FrequencyControls.kt` — sliders on-time / off-time
[X] `PlayStopButton.kt` — botón con animación pulse
[X] `MainActivity.kt` — prototipo funcional cableado al ViewModel
[ ] Smoke test UI + háptico *(checkpoint manual Gastón)*

> Completado por: compose-ui-designer | 2026-06-02

---

## M4 — Rampas de transición (UI)

[ ] `RampSelector.kt` — grid 2×3 con SVG preview de cada curva
[ ] Integración en pantalla principal
[ ] QA en device: diferencia perceptible entre las 6 curvas

> Responsable: compose-ui-designer

---

## M5 — 21 patrones preset

[X] `PatternsRegistry.kt` — 21 waveforms completos
[X] SUAVE: Caricia, Susurro, Roce, Murmullo, Latido, Onda, Deriva
[X] ASCENSO: Oleada, Pulso, Marea, Vértigo, Espiral, Tormenta, Tsunami
[X] CIMA: Pulse Nova, Big Bang, Earthquake, Volcano, Supernova, Singularity, Aftershock
[ ] `PatternSelector.kt` — grid por categoría (en M6)
[ ] QA háptico *(checkpoint manual Gastón)*

> Completado parcialmente por: haptics-engineer | 2026-06-02

---

## M6 — Identidad visual completa

[ ] `VivroColors.kt`, `VivroTypography.kt`
[ ] `VivroSkin.kt` + `SkinRegistry.kt`
[ ] Skin Boudoir: `#0A0008` / `#E8185C` / `#C4A84A` + OrganicCanvas orquídea
[ ] Skin Seda & Piel: `#150C0C` / `#C0143C` / `#F2A59D` + curvas cálidas
[ ] `MainScreen.kt` — layout completo
[ ] `PatternSelector.kt` — grid con selección activa
[ ] `RampSelector.kt` — 6 chips SVG
[ ] Selector de skins in-app
[ ] Persistencia skin en DataStore

> Responsable: compose-ui-designer

---

## M7 — Skins adicionales

[ ] 2 skins claros
[ ] 2 skins explícitos
[ ] Galería de skins

> Responsable: compose-ui-designer

---

## M8 — Favoritos

[ ] `FavoritesRepository.kt` con DataStore
[ ] Guardar configuración completa (patrón + intensidad + rampa + frecuencia)
[ ] UI favoritos: lista, editar nombre, swipe-to-delete

> Responsable: haptics-engineer

---

## M9 — Build APK release

[ ] Ícono orgánico
[ ] Splash screen
[ ] Firma APK *(keystore — manual Gastón)*
[ ] `./gradlew assembleRelease` → BUILD SUCCESSFUL
[ ] QA completo en device

> Responsable: android-architect

---

## M10 — Creador de patrones custom

> Nueva feature aprobada por Gastón | 2026-06-02

[ ] `WaveformCanvas.kt` — Modo whiteboard: dibujar curva en canvas (X=tiempo, Y=intensidad)
[ ] `TapRecorder.kt` — Modo tap: grabar ritmo con el dedo, captura timing y amplitud
[ ] `SegmentEditor.kt` — Modo editor: lista de segmentos con sliders, drag-to-reorder
[ ] `PatternCreatorScreen.kt` — flujo completo: elegir modo → crear → preview → nombre → guardar
[ ] Room DB: `CustomPatternEntity`, `CustomPatternDao`, `CustomPatternRepository`
[ ] Integración en PatternSelector: los patrones propios aparecen con badge 📌
[ ] `libs.versions.toml` y `app/build.gradle.kts` actualizados con Room

> Responsable: haptics-engineer + compose-ui-designer

---

## M11 — Backend comunidad Vibro

> Backend TBD — a decidir cuando Gastón tenga PC

[ ] Elección de stack (Firebase vs Supabase)
[ ] Setup proyecto backend
[ ] Auth: Google Sign-In
[ ] Endpoints: upload, browse, like, user patterns
[ ] Moderación básica (flag + revisión manual)
[ ] Integración Android SDK del backend elegido

> Responsable: android-architect

---

## M12 — UI comunidad

[ ] Tab "Comunidad" en bottom nav
[ ] Browse: grid con filtros (Nuevo / Popular / Categoría)
[ ] Card de patrón: nombre, autor, likes, mini-waveform
[ ] Preview antes de descargar (sentir el patrón)
[ ] Publicar: desde creador propio → comunidad
[ ] Perfil: mis patrones, likes recibidos

> Responsable: compose-ui-designer

---

## Notas técnicas acumuladas

- **minSdk 26 innegociable:** `VibrationEffect.createWaveform` con amplitudes solo desde API 26.
- **hasAmplitudeControl():** no universal. Siempre fallback PWM.
- **Emulador:** sin motor háptico real. Todo QA requiere device físico.
- **VibratorManager:** API 31+. Para 26-30 usar `Vibrator` directamente.
- **Repeat con ramp:** repeat index desplazado N posiciones para omitir fade-in.
- **Amplitudes en Registry:** relativos al máximo. HapticsEngine escala por intensityLevel.
- **WaveformCanvas (M10):** muestrear gesto cada ~20ms → agrupar segmentos similares → VivroPattern.
- **Room (M10):** usar TypeConverter para LongArray/IntArray → JSON. No DataStore (listas variables).
- **Community (M11):** Google Sign-In recomendado — cero fricción en Android, funciona con Firebase y Supabase.
