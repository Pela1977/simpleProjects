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
| M1 | android-architect | Scaffolding Android project | ⬜ Pendiente |
| M2 | haptics-engineer | Motor háptico nativo + VibrationEffect | ⬜ Pendiente |
| M3 | haptics-engineer + compose-ui-designer | Controles frecuencia/intensidad | ⬜ Pendiente |
| M4 | haptics-engineer | 6 rampas de transición + preview visual | ⬜ Pendiente |
| M5 | haptics-engineer | 20 patrones preset | ⬜ Pendiente |
| M6 | compose-ui-designer | Skins Boudoir + Seda & Piel, UI completa | ⬜ Pendiente |
| M7 | compose-ui-designer | Skins claros y explícitos adicionales | ⬜ Pendiente |
| M8 | haptics-engineer | Favoritos y patrones custom | ⬜ Pendiente |
| M9 | android-architect | Build APK release, ícono, splash | ⬜ Pendiente |

---

## Detalle por milestone

### M1 — Scaffolding Android (android-architect)
Objetivo: proyecto Android que compila y levanta vacío.
- Init con Kotlin + Compose
- Gradle Kotlin DSL, minSdk 26
- Permiso VIBRATE en Manifest
- Package base `com.gaston.vibro`
- Entrypoint `MainActivity` con Compose
- Primer commit: hola-mundo en device

### M2 — Motor háptico (haptics-engineer)
Objetivo: poder llamar `playPattern(pattern, intensity)` desde cualquier punto y sentirlo en el device.
- VibrationEffect.createWaveform con amplitudes
- Detección hasAmplitudeControl() + fallback PWM
- HapticsEngine como singleton inyectable
- HapticsViewModel con StateFlow: isPlaying, activePattern, intensityLevel
- Cancelación automática al limpiar ViewModel

### M3 — Controles UI básicos (haptics-engineer + compose-ui-designer)
Objetivo: UI funcional mínima que conecta sliders al motor.
- Slider tiempo activo: 50ms–2000ms
- Slider tiempo inactivo: 50ms–2000ms  
- Slider intensidad: 10 niveles discretos (Nivel 1 = amp 25, Nivel 10 = amp 255)
- Indicador de estado (vibrando / detenido)
- Todo cableado al HapticsViewModel

### M4 — Rampas (haptics-engineer)
Objetivo: 6 curvas que se sienten distintas en el device.
- Cada curva discretizada en 20 pasos
- Se aplica al inicio (fade-in) y al final (fade-out) del ciclo
- UI: 6 chips con icono de la curva + preview animado
- QA: el usuario debe sentir diferencia perceptible entre recta y parábola

### M5 — 20 patrones (haptics-engineer)
Nombres por categoría:

**CIRCULATORIO (7):** Latido, Sístole, Diástole, Pulso, Arritmia, Taquicardia, Flujo
**ELEMENTO (7):** Brisa, Marea, Ceniza, Corriente, Lava, Niebla, Cristal
**FENOMENO (6):** Aurora, Tormenta, Temblor, Oleaje, Relámpago, Eclipse

Cada patrón definido con timings + amplitudes calibrados para sentirse distintos.
Formato: `VivroPattern` — ver CONVENTIONS.md §4.

### M6 — UI completa + skins (compose-ui-designer)
Skin Boudoir (default):
- Fondo: `#0A0008`, fucsia: `#E8185C`, dorado: `#C4A84A`
- Forma canvas: pétalos de orquídea que se abren/cierran con la vibración
- Tipografía: serif elegante (Playfair Display o similar via Google Fonts)

Skin Seda & Piel (bundled, descargable):
- Fondo: `#150C0C`, carmesí: `#C0143C`, rosa piel: `#F2A59D`
- Forma canvas: curvas cálidas, ondas que evocan piel
- Tipografía: sans redondeada

Layout: pantalla vertical, botón play grande central, sliders abajo, selector de patrón arriba.

### M7 — Skins adicionales (compose-ui-designer)
- 2 skins claros: paleta luminosa para uso diurno
- 2 skins explícitos: formas más directas, para build sin restricciones de store
- Galería de skins con preview

### M8 — Favoritos (haptics-engineer)
- DataStore: lista de patrones favoritos guardados
- Guardar configuración completa: patrón + intensidad + rampa + frec
- Nombre editable
- Pantalla de favoritos con swipe-to-delete

### M9 — APK release (android-architect)
- Ícono: forma orgánica, nocturna, consistente con Boudoir
- Splash: breve, elegante
- Build release con firma
- Checklist QA en device real
- Distribución: APK directo (no Play Store en v1)

---

## Fuera de scope v1

- Bluetooth / sync con dispositivos externos
- Modo multijugador / control remoto en pareja vía red
- Backend / cuentas de usuario
- Play Store (política de contenido lo complica)
- iOS
