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

[X] `settings.gradle.kts` — nombre del proyecto, include :app
[X] `build.gradle.kts` (root) — plugins classpath via version catalog
[X] `gradle/libs.versions.toml` — AGP 8.7.3, Kotlin 2.0.21, Compose BOM, DataStore, Google Fonts
[X] `gradle/wrapper/gradle-wrapper.properties` — Gradle 8.9
[X] `app/build.gradle.kts` — minSdk 26, targetSdk 35, Compose habilitado
[X] `app/src/main/AndroidManifest.xml` — permiso VIBRATE, portrait locked
[X] `app/src/main/kotlin/com/gaston/vibro/MainActivity.kt` — Compose entry point
[X] `app/src/main/kotlin/com/gaston/vibro/ui/theme/VivroTheme.kt` — placeholder Boudoir
[X] `app/src/main/res/values/strings.xml` y `themes.xml`
[X] `app/proguard-rules.pro`
[ ] Smoke test: `./gradlew assembleDebug` → BUILD SUCCESSFUL *(checkpoint manual Gastón)*

> Completado por: android-architect | 2026-06-02
> Nota técnica: minSdk 26 innegociable — VibrationEffect con amplitudes solo existe desde API 26.

---

## Naming de patrones — iteraciones

[X] v1: CIRCULATORIO / ELEMENTO / FENOMENO (descartado — temático, no de intensidad)
[X] v2: nombres sensuales pero con términos de temperatura (Vapor, Brasa, Calor) — descartados
[X] v3 FINAL: tres categorías por intensidad, 21 patrones

**SUAVE (7):** Caricia · Susurro · Roce · Murmullo · Latido · Onda · Deriva
**ASCENSO (7):** Oleada · Pulso · Marea · Vértigo · Espiral · Tormenta · Tsunami
**CIMA (7):** Pulse Nova · Big Bang · Earthquake · Volcano · Supernova · Singularity · Aftershock

> Cerrado por: Gastón | 2026-06-02

---

## M2 — Motor háptico nativo

[X] `HapticsConstants.kt` — MAX_AMPLITUDE 255, MIN_AMPLITUDE 20, LEVEL_COUNT 10, AMPLITUDE_LEVELS[]
[X] `VivroPattern.kt` — class VivroPattern + enum PatternCategory (SUAVE, ASCENSO, CIMA) + enum RampType
[X] `RampFunctions.kt` — typealias RampFunction + 6 implementaciones (LINEAR_UP, LINEAR_DOWN, PARABOLA, HYPERBOLA, LOGARITHMIC, EXPONENTIAL)
[X] `HapticsEngine.kt` — VibrationEffect.createWaveform con amplitudes; fallback PWM duty-cycle para hasAmplitudeControl() == false
[X] `HapticsViewModel.kt` — AndroidViewModel + StateFlow<HapticsState> con todos los controles
[ ] Smoke test háptico en device *(checkpoint manual Gastón)*

> Completado por: haptics-engineer | 2026-06-02
> Nota: ramp se prepende como N pasos de fade-in; repeat index se desplaza N posiciones para que el loop omita el ramp.

---

## M3 — Controles frecuencia e intensidad (UI)

[X] `IntensitySlider.kt` — 10 dots táctiles, fill progresivo, color configurable
[X] `FrequencyControls.kt` — sliders on-time / off-time con labels dinámicos en ms
[X] `PlayStopButton.kt` — botón circular con animación pulse activa cuando isPlaying
[X] `MainActivity.kt` — pantalla prototipo funcional cableada a HapticsViewModel + PatternsRegistry
[ ] Smoke test UI + háptico en device *(checkpoint manual Gastón)*

> Completado por: compose-ui-designer | 2026-06-02
> Nota: pantalla de prueba será reemplazada por MainScreen completo en M6.

---

## M4 — Rampas de transición (UI)

[ ] `RampSelector.kt` — 6 chips en grid 2×3, SVG preview de curva por tipo
[ ] Integración RampSelector en pantalla principal
[ ] QA en device: diferencia perceptible entre las 6 curvas

> Responsable: compose-ui-designer + haptics-engineer

---

## M5 — 21 patrones preset

[X] `PatternsRegistry.kt` — 21 patrones con waveforms diseñados
[X] 7 patrones SUAVE: Caricia, Susurro, Roce, Murmullo, Latido, Onda, Deriva
[X] 7 patrones ASCENSO: Oleada, Pulso, Marea, Vértigo, Espiral, Tormenta, Tsunami
[X] 7 patrones CIMA: Pulse Nova, Big Bang, Earthquake, Volcano, Supernova, Singularity, Aftershock
[ ] `PatternSelector.kt` — grid por categoría con selección activa
[ ] QA háptico de los 21 patrones en device *(checkpoint manual Gastón)*

> Completado parcialmente por: haptics-engineer | 2026-06-02
> Pendiente: PatternSelector UI (M6) y QA en device.

---

## M6 — Identidad visual completa

[ ] `VivroColors.kt`, `VivroTypography.kt` — sistema de colores/tipografía por skin
[ ] `VivroSkin.kt` data class + `SkinRegistry.kt`
[ ] Skin Boudoir completo: `#0A0008` / `#E8185C` / `#C4A84A` + canvas orquídea
[ ] Skin Seda & Piel: `#150C0C` / `#C0143C` / `#F2A59D` + canvas curvas cálidas
[ ] `MainScreen.kt` — layout completo con OrganicCanvas, PatternSelector, controles
[ ] `PatternSelector.kt` — grid por categoría
[ ] `RampSelector.kt` — 6 chips con SVG curves
[ ] Selector de skins in-app
[ ] Persistencia skin activo en DataStore

> Responsable: compose-ui-designer

---

## M7 — Skins adicionales

[ ] 2 skins claros (paleta luminosa diurna)
[ ] 2 skins explícitos (formas más directas)
[ ] Integración en SkinRegistry
[ ] Galería de skins con preview

> Responsable: compose-ui-designer

---

## M8 — Favoritos y patrones custom

[ ] `FavoritesRepository.kt` con DataStore
[ ] Guardar configuración completa (patrón + intensidad + rampa + frecuencia)
[ ] Pantalla favoritos: lista, editar nombre, swipe-to-delete
[ ] Exportar favorito como JSON

> Responsable: haptics-engineer

---

## M9 — Build APK release

[ ] Ícono orgánico (consistente con skin Boudoir)
[ ] Splash screen
[ ] Firma APK *(keystore — manual Gastón)*
[ ] `./gradlew assembleDebug` → BUILD SUCCESSFUL
[ ] QA completo en device real
[ ] APK distribuible

> Responsable: android-architect

---

## Notas técnicas acumuladas

- **minSdk 26 innegociable:** `VibrationEffect.createWaveform(timings, amplitudes, repeat)` solo existe desde API 26.
- **hasAmplitudeControl():** no universal. Siempre implementar fallback PWM.
- **Emulador:** no tiene motor háptico real. Todo QA háptico requiere device físico.
- **Android Studio:** disponible para Windows, Mac y Linux. No existe versión para tablet/teléfono.
- **VibratorManager:** requiere API 31+. Para API 26-30 se usa `Vibrator` directamente (deprecated pero funcional).
- **Repeat index con ramp prepend:** al anteponer N pasos de ramp, el repeat index se desplaza a `rampSteps + pattern.repeat` para que el loop omita el fade-in inicial.
- **Amplitudes en PatternsRegistry:** los valores son relativos al máximo (255). HapticsEngine los escala automáticamente según el intensityLevel seleccionado.
