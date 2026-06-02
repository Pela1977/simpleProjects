# Skill: pattern-composer

Dado un brief creativo (nombre + categoría + "vibe"), produce la definición completa y validada de un `VivroPattern` como bloque Kotlin listo para copiar en `PatternsRegistry.kt`.

## Input esperado

```
nombre: Latido
categoría: CIRCULATORIO
vibe: dos golpes rápidos seguidos de pausa, como un corazón real
duración aproximada del ciclo: 800ms
intensidad máxima: alta (200/255)
```

## Output esperado

```kotlin
VivroPattern(
    id = "latido",
    name = "Latido",
    category = PatternCategory.CIRCULATORIO,
    timings = longArrayOf(80, 60, 80, 580),
    amplitudes = intArrayOf(200, 0, 180, 0),
    repeat = -1,
    description = "Dos golpes cardíacos seguidos de pausa, como un corazón en reposo."
)
```

## Reglas de validación (verificar antes de entregar)

1. `timings.size == amplitudes.size` — siempre
2. Amplitudes: 0 = silencio, 1–255 = activo (evitar < 20, se siente igual que 0 en muchos devices)
3. Suma de timings ≈ duración total del ciclo pedida
4. El patrón se siente distinto a los ya existentes (revisar la lista en PatternsRegistry)
5. El nombre del `id` es único y snake_case

## Cómo usar este skill

Llamar con el brief de UN patrón. Devolver el bloque Kotlin validado. Si el brief es ambiguo, hacer UNA pregunta de clarificación antes de generar.
