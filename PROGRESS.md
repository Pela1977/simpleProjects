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

[ ] `HapticsConstants.kt` — MAX_AMPLITUDE, MIN_AMPLITUDE, LEVEL_COUNT, duraciones base
[ ] `VivroPattern.kt` — data class + enum PatternCategory (SUAVE, ASCENSO, CIMA)
[ ] `RampFunctions.kt` — typealias RampFunction + 6 implementaciones
[ ] `HapticsEngine.kt` — wrapper VibrationEffect + fallback PWM
[ ] `HapticsViewModel.kt` — StateFlow: isPlaying, activePattern, intensityLevel, activeRamp
[ ] Smoke test háptico en device *(checkpoint manual Gastón)*

> Responsable: haptics-engineer

---

## M3 — Controles frecuencia e intensidad (UI)

[ ] `FrequencyControls.kt` — sliders tiempo-on / tiempo-off
[ ] `IntensityControls.kt` — 10 niveles discretos
[ ] Cableado UI → HapticsViewModel → HapticsEngine
[ ] Botón play/stop + indicador de estado animado

> Responsable: haptics-engineer + compose-ui-designer

---

## M4 — Rampas de transición

[ ] 6 curvas matemáticas implementadas y validadas
[ ] `RampSelector.kt` — UI 6 chips con preview de curva
[ ] Integración rampa al engine (fade-in / fade-out por ciclo)
[ ] QA en device: diferencia perceptible entre curvas

> Responsable: haptics-engineer

---

## M5 — 21 patrones preset

[ ] `PatternsRegistry.kt` — 21 patrones con naming definitivo
[ ] 7 patrones SUAVE: Caricia, Susurro, Roce, Murmullo, Latido, Onda, Deriva
[ ] 7 patrones ASCENSO: Oleada, Pulso, Marea, Vértigo, Espiral, Tormenta, Tsunami
[ ] 7 patrones CIMA: Pulse Nova, Big Bang, Earthquake, Volcano, Supernova, Singularity, Aftershock
[ ] `PatternSelector.kt` — grid por categoría
[ ] QA háptico de los 21 patrones en device

> Responsable: haptics-engineer (skill: pattern-composer)

---

## M6 — Identidad visual completa

[ ] `VivroColors.kt`, `VivroTypography.kt` — sistema de colores/tipografía por skin
[ ] `VivroSkin.kt` data class + `SkinRegistry.kt`
[ ] Skin Boudoir completo: `#0A0008` / `#E8185C` / `#C4A84A` + canvas orquídea
[ ] Skin Seda & Piel: `#150C0C` / `#C0143C` / `#F2A59D` + canvas curvas cálidas
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
[ ] `./gradlew assembleRelease` → BUILD SUCCESSFUL
[ ] QA completo en device real
[ ] APK distribuible

> Responsable: android-architect

---

## Notas técnicas acumuladas

- **minSdk 26 innegociable:** `VibrationEffect.createWaveform(timings, amplitudes, repeat)` solo existe desde API 26.
- **hasAmplitudeControl():** no universal. Siempre implementar fallback PWM.
- **Emulador:** no tiene motor háptico real. Todo QA háptico requiere device físico.
- **Android Studio:** disponible para Windows, Mac y Linux. No existe versión para tablet/teléfono.
