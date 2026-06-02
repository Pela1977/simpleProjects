# CLAUDE.md — simpleProjects / Vibro

> Leer completo antes de tocar cualquier archivo. Aplica a TODA sesión de trabajo.

---

## 1. Qué es este proyecto

**Vibro** es una app Android nativa (Kotlin + Jetpack Compose) para controlar el motor de vibración del teléfono con fines de placer personal y en pareja. No es una app de productividad ni de negocio — es un producto de ocio íntimo, sensual y adulto.

**Repo:** `Pela1977/simpleprojects`  
**Rama de desarrollo:** `claude/vibration-motor-app-plan-V69Qq`  
**Autor:** Gastón Aguiar — gaston.aguiar1977@gmail.com

---

## 2. Stack técnico

| Capa | Tecnología |
|------|------------|
| Lenguaje | Kotlin |
| UI | Jetpack Compose |
| Haptics | `VibrationEffect.createWaveform(timings, amplitudes, repeat)` (API 26+) |
| Estado | ViewModel + StateFlow |
| Persistencia | Jetpack DataStore (favoritos y skin activo) |
| Build | Gradle (Kotlin DSL) |
| Min SDK | 26 (Android 8.0 — requerido para amplitudes reales) |
| Target SDK | 35 |

---

## 3. Estructura de carpetas

```
simpleProjects/
├── CLAUDE.md            ← este archivo
├── CONVENTIONS.md       ← convenciones técnicas + reglas de tracking
├── PROGRESS.md          ← tracking de sesiones y tareas
├── ROADMAP.md           ← roadmap detallado M0→M9
├── .claude/
│   ├── agents/
│   │   ├── haptics-engineer.md
│   │   ├── compose-ui-designer.md
│   │   └── android-architect.md
│   └── skills/
│       ├── pattern-composer/SKILL.md
│       └── progress-log/SKILL.md
└── app/                 ← proyecto Android Studio
    ├── src/main/
    │   ├── kotlin/com/gaston/vibro/
    │   │   ├── haptics/      ← engine, ramps, patterns, types
    │   │   ├── ui/           ← screens, components, theme, skins
    │   │   ├── data/         ← DataStore, favoritos
    │   │   └── MainActivity.kt
    │   └── res/
    ├── build.gradle.kts
    └── AndroidManifest.xml
```

---

## 4. Features core

1. **Control de frecuencia** — sliders para tiempo activo (ms), tiempo inactivo (ms) y tasa de ciclos.
2. **Control de intensidad** — 10 niveles (mapeados a amplitud 25–255). Con fallback PWM si el hardware no soporta `hasAmplitudeControl()`.
3. **Rampas de transición** — 6 curvas que generan la envolvente activo↔inactivo: recta ascendente, recta descendente, parábola, hipérbola, logarítmica, exponencial.
4. **20 patrones preset** — nombres temáticos (sistema circulatorio, elementos, fenómenos naturales).
5. **Sistema de skins** — temas visuales intercambiables.
6. **Favoritos** — guardar patrones custom con nombre.

---

## 5. Sistema de skins

| Skin | Estado | Descripción |
|------|--------|-------------|
| **Boudoir** | Default (bundled) | Fondo `#0A0008`, fucsia `#E8185C`, dorado `#C4A84A`, orquídea |
| **Seda & Piel** | Bundled (descargable in-app) | Fondo `#150C0C`, carmesí `#C0143C`, rosa piel `#F2A59D` |
| **Claros** | Roadmap M7 | Paletas luminosas para uso diurno |
| **Explícitos** | Roadmap M7 | Formas más directas, solo en build no restringida |

Cada skin es una `data class VivroSkin(colors, shapes, typography)` cargada en el `MaterialTheme`.

---

## 6. Reglas de implementación (innegociables)

| Regla | Razón |
|-------|-------|
| Siempre verificar `vibrator.hasAmplitudeControl()` antes de usar amplitudes | Fallback obligatorio en hardware antiguo |
| Siempre llamar `vibrator.cancel()` al salir de la pantalla o pausar | Evita vibración zombie si el usuario sale |
| Patrones como `data class` inmutables con `timings: LongArray` y `amplitudes: IntArray` | Schema validado, no strings mágicos |
| Sin hardcodear duraciones en la UI — usar constantes en `HapticsConstants.kt` | Mantenibilidad |
| `PROGRESS.md` actualizado al inicio y fin de cada tarea por el agente que la ejecuta | Continuidad de contexto |
| Commits en formato convencional `tipo(scope): descripción` | Historial legible |

---

## 7. Limitaciones de entorno de desarrollo remoto

Este proyecto se desarrolla en un entorno cloud (Claude Code remoto). El entorno **puede escribir código y pushear al repo**, pero **no puede compilar el APK ni sentir la vibración**. Los checkpoints de build y QA háptico son tareas manuales de Gastón con Android Studio y un device real.

---

## 8. Checkpoint manual de Gastón (QA en device)

Después de cada milestone que involucre cambios hápticos:
1. Clonar/pullar la rama en Android Studio
2. Build → Run en dispositivo Android real (API 26+)
3. Probar todos los patrones afectados
4. Registrar resultado en PROGRESS.md
