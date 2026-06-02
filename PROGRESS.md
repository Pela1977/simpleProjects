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
[X] `gradle/libs.versions.toml` — version catalog con AGP, Kotlin, Compose BOM, DataStore, Google Fonts
[X] `gradle/wrapper/gradle-wrapper.properties` — Gradle 8.9
[X] `app/build.gradle.kts` — minSdk 26, targetSdk 35, Compose habilitado, dependencias
[X] `app/src/main/AndroidManifest.xml` — permiso VIBRATE, portrait locked, no action bar
[X] `app/src/main/kotlin/com/gaston/vibro/MainActivity.kt` — Compose entry point con VivroTheme
[X] `app/src/main/kotlin/com/gaston/vibro/ui/theme/VivroTheme.kt` — placeholder Boudoir colors
[X] `app/src/main/res/values/strings.xml` — app_name = vibro
[X] `app/src/main/res/values/themes.xml` — Theme.Vibro base
[X] `app/proguard-rules.pro`
[ ] Smoke test: `./gradlew assembleDebug` → BUILD SUCCESSFUL (checkpoint manual Gastón)

> Completado por: android-architect | 2026-06-02
> Nota técnica: minSdk 26 es el mínimo para VibrationEffect con amplitudes. No bajar bajo ningún concepto.

---

## M2 — Motor háptico nativo

[ ] `HapticsConstants.kt` — constantes de amplitud, duraciones base
[ ] `VivroPattern.kt` — data class + enum `PatternCategory`
[ ] `RampFunctions.kt` — typealias + 6 implementaciones de curvas
[ ] `HapticsEngine.kt` — wrapper de VibrationEffect, detección `hasAmplitudeControl()`, fallback
[ ] `HapticsViewModel.kt` — estado reactivo con StateFlow
[ ] Smoke test háptico en device (checkpoint manual Gastón)

> Responsable: haptics-engineer

---

## M3 — Controles de frecuencia e intensidad (UI)

[ ] `FrequencyControls.kt` — sliders tiempo-on / tiempo-off
[ ] `IntensityControls.kt` — 10 niveles visuales
[ ] Cableado UI → HapticsViewModel → HapticsEngine
[ ] Feedback visual básico (indicador de "vibrando")

> Responsable: haptics-engineer + compose-ui-designer

---

## M4 — Rampas de transición

[ ] 6 funciones de rampa implementadas y testeadas matemáticamente
[ ] `RampSelector.kt` — UI selector de 6 presets con preview visual de curva
[ ] Integración de rampa al engine: apply ramp on activo↔inactivo
[ ] QA en device: verificar diferencia háptica perceptible entre curvas

> Responsable: haptics-engineer

---

## M5 — 20 patrones preset

[ ] `PatternsRegistry.kt` — los 20 patrones definidos con naming definitivo
[ ] 7 patrones CIRCULATORIO: Latido, Pulso, Oleada, Arrebato, Ansia, Umbral, Cima
[ ] 7 patrones ELEMENTO: Brasa, Lava, Vapor, Llama, Marea, Tormenta, Ardor
[ ] 6 patrones FENOMENO: Éxtasis, Vértigo, Frenesí, Trance, Delirio, Temblor
[ ] `PatternSelector.kt` — UI grid/lista para elegir patrón
[ ] QA háptico de los 20 patrones en device

> Responsable: haptics-engineer (skill: pattern-composer)

---

## M6 — Identidad visual completa

[ ] `VivroColors.kt`, `VivroTypography.kt` — sistema de colores/tipografía por skin
[ ] `VivroSkin.kt` data class + `SkinRegistry.kt`
[ ] Skin Boudoir completo: fondo `#0A0008`, fucsia `#E8185C`, dorado `#C4A84A`
[ ] Canvas orgánico: pétalos de orquídea que pulsan con la vibración
[ ] Skin Seda & Piel: fondo `#150C0C`, carmesí `#C0143C`, rosa piel `#F2A59D`
[ ] Canvas Seda: curvas cálidas y ondas
[ ] Selector de skins in-app
[ ] Persistencia del skin activo en DataStore

> Responsable: compose-ui-designer

---

## M7 — Skins adicionales

[ ] Diseño de 2 skins claros (paleta luminosa diurna)
[ ] Diseño de 2 skins explícitos (formas más directas)
[ ] Integración en SkinRegistry
[ ] Pantalla de galería de skins

> Responsable: compose-ui-designer

---

## M8 — Favoritos y patrones custom

[ ] `FavoritesRepository.kt` con DataStore
[ ] Guardar patrón activo con nombre custom
[ ] Pantalla de favoritos (lista, editar nombre, borrar)
[ ] Exportar favorito como JSON (share)

> Responsable: haptics-engineer

---

## M9 — Build APK release

[ ] Ícono de app (orgánico, consistente con skin Boudoir)
[ ] Splash screen
[ ] Firma del APK (keystore — manual Gastón)
[ ] Build release: `./gradlew assembleRelease`
[ ] Matriz de QA completa en device real
[ ] APK distribuible

> Responsable: android-architect

---

## Notas técnicas acumuladas

- **minSdk 26 innegociable:** `VibrationEffect.createWaveform(timings, amplitudes, repeat)` solo existe desde API 26. Sin esto no hay control de intensidad real.
- **hasAmplitudeControl():** no es universal. Siempre implementar fallback PWM (duty-cycle via timings).
