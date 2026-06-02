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

[ ] Init proyecto Android en `app/` (Kotlin + Compose)
[ ] `build.gradle.kts` con minSdk 26, targetSdk 35, Compose habilitado
[ ] `AndroidManifest.xml` con permiso `VIBRATE`
[ ] Estructura de packages: `haptics/`, `ui/`, `data/`
[ ] Hola-mundo: pantalla vacía que levanta sin errores
[ ] `MainActivity.kt` con Compose entry point
[ ] Smoke test: compilar localmente (checkpoint manual Gastón)

> Responsable: android-architect

---

## M2 — Motor háptico nativo

[ ] `HapticsConstants.kt` — constantes de amplitud, duraciones base
[ ] `VivroPattern.kt` — data class + enum `PatternCategory`
[ ] `RampFunction.kt` — typealias + 6 implementaciones de curvas
[ ] `HapticsEngine.kt` — wrapper de VibrationEffect, detección `hasAmplitudeControl()`, fallback
[ ] `HapticsViewModel.kt` — estado reactivo con StateFlow
[ ] Test de smoke háptico en device (checkpoint manual Gastón)

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

[ ] `PatternsRegistry.kt` — los 20 patrones definidos
[ ] 7 patrones categoría CIRCULATORIO
[ ] 7 patrones categoría ELEMENTO
[ ] 6 patrones categoría FENOMENO
[ ] `PatternSelector.kt` — UI grid/lista para elegir patrón
[ ] QA háptico de los 20 patrones en device

> Responsable: haptics-engineer (skill: pattern-composer)

---

## M6 — Identidad visual completa (skin Boudoir default)

[ ] `VivroColors.kt`, `VivroTypography.kt`, `VivroTheme.kt`
[ ] `VivroSkin.kt` data class + `SkinRegistry.kt`
[ ] Skin Boudoir: fondo `#0A0008`, fucsia `#E8185C`, dorado `#C4A84A`
[ ] Canvas orgánico: formas de orquídea/pétalos que pulsan con la vibración
[ ] Skin Seda & Piel: fondo `#150C0C`, carmesí `#C0143C`, rosa piel `#F2A59D`
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
[ ] APK distribuible por APK directo (no Play Store por ahora)

> Responsable: android-architect

---

## Notas técnicas acumuladas

_(agentes agregan aquí gotchas y decisiones técnicas a medida que aparecen)_
