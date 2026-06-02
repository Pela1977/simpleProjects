# ROADMAP.md — simpleProjects / Vibro

> Documento vivo. El estado actual siempre está en PROGRESS.md.

---

## Visión del producto

App Android nativa para controlar el motor de vibración del teléfono con fines de placer íntimo. Intensidad y rampas reales usando `VibrationEffect.createWaveform()` (API 26+). Estética sensual, adulta y elegante con sistema de skins intercambiables.

---

## Milestones

| Milestone | Agente principal | Descripción | Estado |
|-----------|-----------------|-------------|--------|
| M0 | setup | Fundaciones PM | ✅ Hecho |
| M1 | android-architect | Scaffolding Android project | ✅ Hecho |
| M2 | haptics-engineer | Motor háptico nativo + VibrationEffect | ⬜ Pendiente |
| M3 | haptics-engineer + compose-ui-designer | Controles frecuencia/intensidad | ⬜ Pendiente |
| M4 | haptics-engineer | 6 rampas de transición + preview visual | ⬜ Pendiente |
| M5 | haptics-engineer | 20 patrones preset | ⬜ Pendiente |
| M6 | compose-ui-designer | Skins Boudoir + Seda & Piel, UI completa | ⬜ Pendiente |
| M7 | compose-ui-designer | Skins claros y explícitos adicionales | ⬜ Pendiente |
| M8 | haptics-engineer | Favoritos y patrones custom | ⬜ Pendiente |
| M9 | android-architect | Build APK release, ícono, splash | ⬜ Pendiente |

---

## Los 20 patrones — naming definitivo

Nombres evocativos del placer, el éxtasis y el deseo. Organizados en tres categorías.

### CIRCULATORIO (7) — sensaciones del cuerpo
| ID | Nombre | Vibe |
|----|--------|------|
| `latido` | **Latido** | Pulso cardíaco lento, íntimo |
| `pulso` | **Pulso** | Ritmo constante, creciente |
| `oleada` | **Oleada** | Ola de sensación que sube y baja |
| `arrebato` | **Arrebato** | Arranque súbito de intensidad |
| `ansia` | **Ansia** | Urgencia, deseo que no puede esperar |
| `umbral` | **Umbral** | Borde del límite, acumulación |
| `cima` | **Cima** | El pico, el clímax |

### ELEMENTO (7) — naturaleza sensual
| ID | Nombre | Vibe |
|----|--------|------|
| `brasa` | **Brasa** | Calor lento, profundo, persistente |
| `lava` | **Lava** | Flujo denso y caliente, imparable |
| `vapor` | **Vapor** | Suave, envolvente, cálido |
| `llama` | **Llama** | Intensidad que oscila |
| `marea` | **Marea** | Ritmo largo, hipnótico |
| `tormenta` | **Tormenta** | Caótico, eléctrico, salvaje |
| `ardor` | **Ardor** | Quemazón del deseo, sin pausa |

### FENOMENO (6) — estados de éxtasis
| ID | Nombre | Vibe |
|----|--------|------|
| `extasis` | **Éxtasis** | El momento cumbre, sostenido |
| `vertigo` | **Vértigo** | Mareo de placer, pérdida de control |
| `frenesi` | **Frenesí** | Aceleración hasta el límite |
| `trance` | **Trance** | Repetición hipnótica, meditativa |
| `delirio` | **Delirio** | Caos de sensaciones mezcladas |
| `temblor` | **Temblor** | Vibración fina y profunda, el estremecimiento final |

---

## Detalle por milestone

### M1 — Scaffolding Android (android-architect)
- Init con Kotlin + Compose, Gradle Kotlin DSL, minSdk 26
- Permiso VIBRATE en Manifest, portrait locked
- Package base `com.gaston.vibro`
- VivroTheme placeholder (Boudoir colors base)
- Primer commit: hola-mundo compilable

### M2 — Motor háptico (haptics-engineer)
- VibrationEffect.createWaveform con amplitudes
- hasAmplitudeControl() + fallback PWM
- HapticsEngine como singleton inyectable
- HapticsViewModel con StateFlow: isPlaying, activePattern, intensityLevel
- Cancelación automática al limpiar ViewModel

### M3 — Controles UI básicos (haptics-engineer + compose-ui-designer)
- Slider tiempo activo: 50ms–2000ms
- Slider tiempo inactivo: 50ms–2000ms
- Slider intensidad: 10 niveles discretos (Nivel 1 = amp 25, Nivel 10 = amp 255)
- Indicador de estado (vibrando / detenido)

### M4 — Rampas (haptics-engineer)
- 6 curvas discretizadas en 20 pasos
- Se aplica al inicio (fade-in) y final (fade-out) del ciclo
- UI: 6 chips con preview animado de la curva

### M5 — 20 patrones (haptics-engineer)
- PatternsRegistry.kt con los 20 patrones definitivos
- Timings + amplitudes calibrados para sentirse distintos
- QA háptico en device real

### M6 — UI completa + skins (compose-ui-designer)
Skin Boudoir (default): `#0A0008` / `#E8185C` / `#C4A84A`, orquídea animada
Skin Seda & Piel (bundled): `#150C0C` / `#C0143C` / `#F2A59D`, ondas cálidas

### M7 — Skins adicionales (compose-ui-designer)
- 2 skins claros para uso diurno
- 2 skins explícitos (formas más directas)
- Galería de skins con preview

### M8 — Favoritos (haptics-engineer)
- DataStore: guardar configuración completa (patrón + intensidad + rampa + frec)
- Nombre editable, swipe-to-delete

### M9 — APK release (android-architect)
- Ícono orgánico, splash, firma, build release
- Distribución: APK directo

---

## Fuera de scope v1

- Bluetooth / sync con dispositivos externos
- Control remoto en pareja vía red
- Backend / cuentas de usuario
- Play Store (política de contenido)
- iOS
