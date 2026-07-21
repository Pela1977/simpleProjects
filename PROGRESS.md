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

[X] `RampSelector.kt` — grid 2×3, curva real muestreada de la misma RampFunction del engine
[X] Integración en MainScreen
[ ] QA en device: diferencia perceptible entre las 6 curvas *(checkpoint manual Gastón)*

> Completado por: compose-ui-designer | 2026-07-21

---

## M5 — 21 patrones preset

[X] `PatternsRegistry.kt` — 21 waveforms completos
[X] SUAVE: Caricia, Susurro, Roce, Murmullo, Latido, Onda, Deriva
[X] ASCENSO: Oleada, Pulso, Marea, Vértigo, Espiral, Tormenta, Tsunami
[X] CIMA: Pulse Nova, Big Bang, Earthquake, Volcano, Supernova, Singularity, Aftershock
[X] `PatternSelector.kt` — tabs por categoría + tab MÍOS + grid con mini-waveforms
[ ] QA háptico *(checkpoint manual Gastón)*

> Completado por: haptics-engineer + compose-ui-designer | 2026-07-21

---

## M6 — Identidad visual completa

[X] `VivroSkin.kt` + `VivroSkinColors` + enum CanvasStyle
[X] `SkinRegistry.kt` — 6 skins bundled
[X] `VivroTypography.kt` — serif para display, sans para cuerpo
[X] `VivroTheme.kt` — CompositionLocal LocalVivroSkin + MaterialTheme dark/light
[X] Skin Boudoir: `#0A0008` / `#E8185C` / `#C4A84A` + OrganicCanvas orquídea (5 pétalos animados)
[X] Skin Seda & Piel: `#150C0C` / `#C0143C` / `#F2A59D` + óvalos de seda concéntricos
[X] `MainScreen.kt` — layout completo: canvas + play + intensidad + frecuencia + rampas + patrones + favorito
[X] `SkinsScreen.kt` — galería con paleta preview, badge 18+, selección persistida
[X] `SkinRepository.kt` — skin activo en DataStore
[X] Navegación bottom-nav propia (Inicio / Crear / Favoritos / Skins) sin dependencia extra

> Completado por: compose-ui-designer | 2026-07-21

---

## M7 — Skins adicionales

[X] 2 skins claros: **Alba** (crema/coral) y **Nácar** (perla/lila)
[X] 2 skins explícitos: **Rubí** (anillos de choque) y **Obsidiana** (rayos radiales) — badge 18+
[X] Galería de skins con preview de paleta
[X] Cada skin tiene su propio CanvasStyle (ORCHID, SILK, DAWN, PEARL, EMBER, NOIR)

> Completado por: compose-ui-designer | 2026-07-21

---

## M8 — Favoritos

[X] `FavoritesRepository.kt` — DataStore + JSON (org.json, sin dependencias nuevas)
[X] Guardar configuración completa (patrón + intensidad + rampa + frecuencia)
[X] `FavoritesScreen.kt` — lista, tap para reproducir, borrar
[X] Diálogo "Guardar como favorito" en MainScreen
[ ] QA en device *(checkpoint manual Gastón)*

> Completado por: haptics-engineer | 2026-07-21

---

## M9 — Build APK release

[X] Ícono adaptativo: orquídea vectorial 5 pétalos + centro dorado (`ic_launcher_foreground.xml`)
[X] Splash mínimo: windowBackground Boudoir en `themes.xml`
[ ] Firma APK *(keystore — manual Gastón)*
[ ] `./gradlew assembleRelease` → BUILD SUCCESSFUL *(manual Gastón)*
[ ] QA completo en device *(manual Gastón)*

> Completado parcialmente por: android-architect | 2026-07-21 (todo lo que no requiere PC)

---

## M10 — Creador de patrones custom

> Nueva feature aprobada por Gastón | 2026-06-02

[X] `WaveformCanvas.kt` — whiteboard: dibujo libre → detección de picos/valles → nodos arrastrables + Catmull-Rom · tap agrega nodo · tap largo borra · slider de duración de ciclo
[X] `TapRecorder.kt` — grabación por toques con velocidad de captura 0.25×/0.5×/1×/2× (slow-mo normalizado), amplitud regulable, deshacer/limpiar
[X] `SegmentEditor.kt` — tramos con sliders duración+amplitud, reordenar ▲▼, borrar, agregar pulso/pausa
[X] `FractalExpander.kt` — Pattern DNA: eco ×0.5 + base + expansión ×2 atenuada, profundidad 1-3
[X] `PatternMixer.kt` — Layer Mixer: hasta 4 capas con peso, muestreo 20ms, suma clampeada a 255
[X] `MixerPanel.kt` — UI del mixer con preview en vivo y "usar como base"
[X] `CreatorScreen.kt` — 4 modos (Dibujar/Grabar/Tramos/Mezclar) que convergen en la misma lista de segmentos → Sentir → nombrar+categorizar → guardar
[X] `CustomPatternRepository.kt` — **DataStore+JSON en lugar de Room** (decisión: sin KSP en entorno sin compilación; migrable a Room)
[X] `Segment.kt` + `SegmentTools` — sanitización, merge, conversión a VivroPattern
[X] Integración en PatternSelector: tab MÍOS con borrar
[ ] QA del creador en device *(checkpoint manual Gastón)*

> Completado por: haptics-engineer + compose-ui-designer | 2026-07-21

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
- **Custom patterns:** persistidos en DataStore+JSON (no Room) — evita KSP en entorno sin build; decisión revisable.
- **MIN/MAX_TIMING_MS (10/3000):** clamp físico de tramos del waveform, separado del rango de los sliders on/off — fix de bug que deformaba patrones rápidos (Earthquake 25ms) y custom largos.
- **material-icons eliminado:** el artifact estaba mal nombrado en el toml (habría roto el build) y ningún archivo lo usa — play/stop se dibujan con Canvas.
- **gradlew ausente:** el wrapper script no está en el repo. Android Studio lo regenera al abrir el proyecto (File → Sync), o `gradle wrapper` con Gradle local.
- **onPause() → viewModel.stop():** regla de no-vibración-zombie implementada en MainActivity.
