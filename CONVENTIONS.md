# CONVENTIONS.md — simpleProjects / Vibro

> Documento vivo. Actualizar cuando se introduce un patrón nuevo.

---

## §1. Tracking de progreso (REGLA MÁS IMPORTANTE)

Cada agente, al terminar cualquier tarea:

1. Abrir `PROGRESS.md`
2. Marcar `[X]` los ítems completados
3. Agregar al pie del bloque de tarea:
   ```
   > Completado por: <nombre-agente> | <fecha YYYY-MM-DD>
   ```
4. Si introdujo un patrón técnico nuevo → agregar sección en este `CONVENTIONS.md`
5. Si encontró un anti-pattern o gotcha → documentarlo en `PROGRESS.md` bajo "Notas técnicas"

**Sin este registro, la tarea no se considera terminada.**

---

## §2. Commits

Formato: `tipo(scope): descripción en minúsculas`

| Tipo | Cuándo |
|------|--------|
| `feat` | nueva funcionalidad |
| `fix` | corrección de bug |
| `chore` | configuración, build, sin cambio funcional |
| `refactor` | reestructura sin cambio de comportamiento |
| `perf` | optimización |
| `style` | cambios visuales/UI sin lógica |
| `docs` | solo documentación |

Ejemplos:
```
feat(haptics): add waveform engine with amplitude fallback
feat(skins): add Seda & Piel skin with petal canvas
fix(haptics): cancel vibration on screen exit
chore(gradle): set minSdk to 26, configure Compose
```

---

## §3. Naming — Kotlin

| Elemento | Convención | Ejemplo |
|----------|------------|---------|
| Clases | `PascalCase` | `HapticsEngine`, `VivroSkin` |
| Funciones | `camelCase` | `buildWaveform()`, `applyRamp()` |
| Variables | `camelCase` | `activePattern`, `intensityLevel` |
| Constantes | `UPPER_SNAKE_CASE` en `object` | `MAX_AMPLITUDE = 255` |
| Archivos | `PascalCase.kt` para clases, `camelCase.kt` para utils | `HapticsEngine.kt`, `rampFunctions.kt` |
| Packages | `lowercase` | `com.gaston.vibro.haptics` |
| Recursos | `snake_case` | `ic_play_button`, `color_fucsia_primary` |

---

## §4. Estructura de un patrón háptico

Todo patrón es una `data class` con schema fijo:

```kotlin
data class VivroPattern(
    val id: String,              // snake_case único, ej: "latido_cardiaco"
    val name: String,            // nombre display, ej: "Latido"
    val category: PatternCategory,
    val timings: LongArray,      // [on_ms, off_ms, on_ms, ...] longitud par
    val amplitudes: IntArray,    // mismo largo que timings, valores 0-255
    val repeat: Int = -1,        // -1 = loop infinito
    val description: String = ""
)

enum class PatternCategory { CIRCULATORIO, ELEMENTO, FENOMENO }
```

Reglas:
- `timings` y `amplitudes` siempre mismo largo
- Primer elemento de `timings` = tiempo activo; alternar activo/inactivo
- Amplitud 0 = pausa; amplitud 1–255 = intensidad
- Validar con `require(timings.size == amplitudes.size)`

---

## §5. Motor de vibración — reglas

```kotlin
// Siempre verificar soporte antes de usar amplitudes
if (vibrator.hasAmplitudeControl()) {
    vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, repeat))
} else {
    vibrator.vibrate(VibrationEffect.createWaveform(timings, repeat)) // fallback sin amplitud
}

// Siempre cancelar al salir
override fun onCleared() { vibrator.cancel() }
```

---

## §6. Rampas — convención matemática

Una rampa toma N pasos y genera un array de amplitudes 0→maxAmp (ascendente) o maxAmp→0 (descendente).

```kotlin
typealias RampFunction = (step: Int, totalSteps: Int, maxAmplitude: Int) -> Int
```

| Nombre | Función | ID |
|--------|---------|----|
| Recta asc | `step * maxAmp / totalSteps` | `LINEAR_UP` |
| Recta desc | `(totalSteps - step) * maxAmp / totalSteps` | `LINEAR_DOWN` |
| Parábola | `(step² / totalSteps²) * maxAmp` | `PARABOLA` |
| Hipérbola | función hiperbólica normalizada | `HYPERBOLA` |
| Logarítmica | `ln(step+1) / ln(totalSteps+1) * maxAmp` | `LOGARITHMIC` |
| Exponencial | `(e^step - 1) / (e^totalSteps - 1) * maxAmp` | `EXPONENTIAL` |

---

## §7. Sistema de skins

Cada skin es un objeto `VivroSkin` con:

```kotlin
data class VivroSkin(
    val id: String,
    val name: String,
    val colors: VivroColors,
    val shapes: VivroShapes,   // tipo de forma orgánica en Canvas
    val typography: VivroTypography
)
```

- Los skins se registran en `SkinRegistry.kt`
- El skin activo se persiste en DataStore con clave `"active_skin_id"`
- Nombres de archivo de skin: `skin_<id>.kt` (ej: `skin_boudoir.kt`)

---

## §8. Prioridades de tarea

| Label | Significado |
|-------|-------------|
| P0 | Bloqueante — no se puede avanzar sin esto |
| P1 | Importante — entra en el milestone |
| P2 | Mejora — entra si hay tiempo, sino al siguiente |

---

## §9. Lo que no hacer

- No mezclar lógica háptica con lógica de UI (ViewModel media)
- No hardcodear colores en Composables — siempre desde `MaterialTheme.colorScheme`
- No crear patrones como strings o mapas — siempre `VivroPattern` con schema validado
- No commitear `*.apk` ni `*.keystore` (están en .gitignore)
- No modificar la `main` branch directamente — toda la dev va en `claude/vibration-motor-app-plan-V69Qq`
