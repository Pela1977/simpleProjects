# Skill: progress-log

Estandariza el registro de progreso para que todos los agentes lo escriban de forma idéntica en `PROGRESS.md`.

## Cuándo usar

Al final de CUALQUIER tarea completada por cualquier agente.

## Formato obligatorio

En el bloque del milestone correspondiente en `PROGRESS.md`:

1. Cambiar `[ ]` por `[X]` en cada ítem completado
2. Agregar al pie del bloque (después del último ítem):

```
> Completado por: <nombre-agente> | <fecha YYYY-MM-DD>
```

3. Si la tarea introdujo un patrón técnico nuevo, agregar nota:
```
> Nota técnica: <descripción breve del patrón/gotcha descubierto>
```

## Ejemplo de bloque completado

```markdown
## M2 — Motor háptico nativo

[X] HapticsConstants.kt creado
[X] VivroPattern.kt data class + enum
[X] RampFunctions.kt — 6 curvas
[X] HapticsEngine.kt con fallback
[X] HapticsViewModel.kt con StateFlow
[ ] Smoke test en device (checkpoint manual Gastón)

> Completado por: haptics-engineer | 2026-06-05
> Nota técnica: en Pixel 6 hasAmplitudeControl() devuelve true pero amplitudes < 30 no se perciben
```

## Qué NO hacer

- No agregar comentarios genéricos tipo "todo bien"
- No marcar como completo lo que está pendiente de QA manual en device
- No modificar ítems de otros milestones
