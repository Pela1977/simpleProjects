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
| M5 | haptics-engineer | 21 patrones preset | ⬜ Pendiente |
| M6 | compose-ui-designer | Skins Boudoir + Seda & Piel, UI completa | ⬜ Pendiente |
| M7 | compose-ui-designer | Skins claros y explícitos adicionales | ⬜ Pendiente |
| M8 | haptics-engineer | Favoritos y patrones custom | ⬜ Pendiente |
| M9 | android-architect | Build APK release, ícono, splash | ⬜ Pendiente |

---

## Los 21 patrones — naming definitivo

Tres categorías que narran una progresión de intensidad:

### SUAVE (7) — inicio, exploración, calidez

Sensaciones sutiles, casi imperceptibles. El punto de partida.

| ID | Nombre | Vibe háptico |
|----|--------|--------------|
| `caricia` | **Caricia** | Toque suavísimo, intermitente, casi no se siente |
| `susurro` | **Susurro** | Vibración muy fina y rápida, baja amplitud constante |
| `roce` | **Roce** | Pulsos cortos separados, como un roce de piel |
| `murmullo` | **Murmullo** | Ondulación lenta, suave, repetitiva |
| `latido` | **Latido** | Dos pulsos juntos + pausa larga, como un corazón en reposo |
| `onda` | **Onda** | Ciclo largo, sube y baja gradualmente |
| `deriva` | **Deriva** | Ritmo irregular lento, flotante, sin urgencia |

### ASCENSO (7) — deseo que escala, urgencia creciente

Intensidad media y en aumento. El cuerpo pide más.

| ID | Nombre | Vibe háptico |
|----|--------|--------------|
| `oleada` | **Oleada** | Ola que crece y cae, ciclos medianos |
| `pulso` | **Pulso** | Ritmo constante acelerándose progresivamente |
| `marea` | **Marea** | Ciclo muy largo, irresistible, como una fuerza que arrastra |
| `vertigo` | **Vértigo** | Aceleración que marea, cada ciclo más corto |
| `espiral` | **Espiral** | Cada repetición más intensa que la anterior |
| `tormenta` | **Tormenta** | Caótico, eléctrico, ráfagas irregulares |
| `tsunami` | **Tsunami** | Construcción lenta e imparable hacia un pico brutal |

### CIMA (7) — explosión, clímax, el punto sin retorno

Máxima intensidad. Fenómenos naturales y astronómicos que describen lo indescriptible.

| ID | Nombre | Vibe háptico |
|----|--------|--------------|
| `pulse_nova` | **Pulse Nova** | Pulsos de alta energía en ráfagas cortas, luminosos |
| `big_bang` | **Big Bang** | Un silencio → explosión total → expansión decreciente |
| `earthquake` | **Earthquake** | Vibración profunda y sostenida, baja frecuencia máxima |
| `volcano` | **Volcano** | Construcción lenta hasta erupción + lava sostenida |
| `supernova` | **Supernova** | El pico absoluto: máxima amplitud, sostenida, luego silencio |
| `singularity` | **Singularity** | Aceleración hasta el límite donde las reglas dejan de aplicar |
| `aftershock` | **Aftershock** | Réplicas post-clímax: intensidad decreciente, eco del orgasmo |

---

## Detalle por milestone

### M1 — Scaffolding Android (android-architect) ✅
- Kotlin + Compose, Gradle Kotlin DSL, minSdk 26
- Permiso VIBRATE, portrait locked
- VivroTheme placeholder Boudoir colors
- Proyecto abre en Android Studio y compila

### M2 — Motor háptico (haptics-engineer)
- `VibrationEffect.createWaveform(timings, amplitudes, repeat)`
- `hasAmplitudeControl()` + fallback PWM
- `HapticsEngine` como singleton inyectable via ViewModel
- `HapticsViewModel` con StateFlow: `isPlaying`, `activePattern`, `intensityLevel`, `activeRamp`
- `vibrator.cancel()` en `onCleared()` y al pausar

### M3 — Controles UI básicos (haptics-engineer + compose-ui-designer)
- Slider tiempo activo: 50ms–2000ms
- Slider tiempo inactivo: 50ms–2000ms
- Slider intensidad: 10 niveles discretos (nivel 1 = amp 25, nivel 10 = amp 255)
- Botón play/stop central
- Indicador de estado animado

### M4 — Rampas (haptics-engineer)
- 6 curvas: recta asc, recta desc, parábola, hipérbola, logarítmica, exponencial
- Discretizadas en 20 pasos
- Se aplican al fade-in y fade-out de cada ciclo
- UI: 6 chips con preview visual de la curva
- QA: diferencia perceptible entre recta y parábola en device

### M5 — 21 patrones (haptics-engineer + skill pattern-composer)
- `PatternsRegistry.kt` con los 21 patrones
- Timings + amplitudes calibrados para sentirse distintos entre sí
- `PatternSelector.kt`: grid por categoría (SUAVE / ASCENSO / CIMA)
- QA háptico en device real

### M6 — UI completa + skins (compose-ui-designer)
**Boudoir (default):** `#0A0008` / `#E8185C` / `#C4A84A` — pétalos de orquídea animados
**Seda & Piel (bundled):** `#150C0C` / `#C0143C` / `#F2A59D` — curvas cálidas
Layout: canvas orgánico detrás, botón play grande, sliders abajo, selector de patrón arriba.

### M7 — Skins adicionales (compose-ui-designer)
- 2 skins claros (uso diurno)
- 2 skins explícitos (formas más directas)
- Galería de skins con preview

### M8 — Favoritos (haptics-engineer)
- DataStore: guardar configuración completa (patrón + intensidad + rampa + frecuencia)
- Nombre editable, swipe-to-delete
- Exportar como JSON

### M9 — APK release (android-architect)
- Ícono orgánico, splash, firma, build release
- Distribución: APK directo (no Play Store en v1)

---

## Fuera de scope v1

- Bluetooth / sync con dispositivos externos
- Control remoto en pareja vía red
- Backend / cuentas de usuario
- Play Store (política de contenido)
- iOS
