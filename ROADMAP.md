# ROADMAP — Vibro

> App Android nativa (Kotlin + Jetpack Compose) para control del motor de vibración.
> minSdk 26 — VibrationEffect con amplitudes reales.

---

## Milestones

| # | Nombre | Estado | Responsable |
|---|--------|--------|-------------|
| M0 | Fundaciones PM | ✅ Completo | setup |
| M1 | Android project scaffold | ✅ Completo | android-architect |
| M2 | Motor háptico nativo | ✅ Completo | haptics-engineer |
| M3 | Controles UI básicos | ✅ Completo | compose-ui-designer |
| M4 | Rampas de transición (UI) | 🔜 Siguiente | compose-ui-designer |
| M5 | 21 patrones preset | ✅ Completo (data) | haptics-engineer |
| M6 | Identidad visual completa | 🔜 Pendiente | compose-ui-designer |
| M7 | Skins adicionales | 🔜 Pendiente | compose-ui-designer |
| M8 | Favoritos (DataStore) | 🔜 Pendiente | haptics-engineer |
| M9 | Build APK release | 🔜 Pendiente | android-architect |
| M10 | Creador de patrones custom | 🔜 Pendiente | haptics-engineer + compose-ui-designer |
| M11 | Backend comunidad Vibro | 🔜 Pendiente (backend TBD) | android-architect |
| M12 | UI comunidad — explorar + publicar | 🔜 Pendiente | compose-ui-designer |

---

## Patrones preset (21, definitivos)

### SUAVE
| ID | Nombre | Vibe |
|----|--------|------|
| suave_caricia | Caricia | Roce continuo y suave, sin interrupciones |
| suave_susurro | Susurro | Casi imperceptible, como un aliento |
| suave_roce | Roce | Toque breve, larga pausa |
| suave_murmullo | Murmullo | Aleteo rápido y suave |
| suave_latido | Latido | Ritmo lub-dub del corazón |
| suave_onda | Onda | Ola que sube y baja |
| suave_deriva | Deriva | Lenta, soñadora, hipnótica |

### ASCENSO
| ID | Nombre | Vibe |
|----|--------|------|
| ascenso_oleada | Oleada | Ola que crece en intensidad |
| ascenso_pulso | Pulso | Pulso fuerte y rítmico |
| ascenso_marea | Marea | Marea poderosa y lenta |
| ascenso_vertigo | Vértigo | Escala sin aviso, desorientador |
| ascenso_espiral | Espiral | Se aprieta y acelera |
| ascenso_tormenta | Tormenta | Turbulenta e irregular |
| ascenso_tsunami | Tsunami | Una ola imparable |

### CIMA
| ID | Nombre | Vibe |
|----|--------|------|
| cima_pulse_nova | Pulse Nova | Pulsos rápidos como estrella de neutrones |
| cima_big_bang | Big Bang | Expansión de cero al universo |
| cima_earthquake | Earthquake | Suelo que tiembla sin parar |
| cima_volcano | Volcano | Erupcón que escala |
| cima_supernova | Supernova | Explosión + máximo sostenido |
| cima_singularity | Singularity | Máximo absoluto, sostenido |
| cima_aftershock | Aftershock | Ecos que disminuyen |

---

## M10 — Creador de patrones custom

### Modo A: Whiteboard (con nodos Catmull-Rom)

El usuario dibuja la curva de amplitud con el dedo sobre un canvas:
- **Eje X** = tiempo (el ancho del canvas = duración del ciclo)
- **Eje Y** = intensidad (arriba = máximo, abajo = silencio)
- Grid de referencia con ticks de tiempo

**Post-dibujo — edición por nodos:**
- El sistema detecta automáticamente los puntos clave (picos, valles, inflexiones, breaks)
- Se coloca un **nodo editable (handle)** en cada punto clave
- Entre nodos: interpolación **Catmull-Rom spline** (suave, pasa por los puntos exactos)
- El usuario puede:
  - Arrastrar nodos para ajustar la curva
  - Tocar entre dos nodos para agregar uno nuevo
  - Mantener presionado un nodo para eliminarlo
- Botón “Sentir”: previsualiza hápticamente en tiempo real mientras se edita
- Botón “Limpiar”: vuelve a dibujo libre
- Implementación: `WaveformCanvas.kt` con `Modifier.pointerInput` + `DrawScope`, detección de extremos locales

### Modo B: Tap-to-record con control de velocidad de captura

El usuario toca un área grande para grabar el ritmo:
- **Velocidad de captura** seleccionable antes de grabar: `0.25× / 0.5× / 1× / 2×`
  - `0.5×` = grabás en cámara lenta (más fácil ser preciso)
  - Los timings se normalizan al finalizar para que el patrón suene a `1×`
  - Ejemplo: grabás a 0.5× un pulso de 200ms reales → el patrón queda con 100ms
- Cada toque = pulso (duración = tiempo que el dedo está presionado)
- El gap entre toques = pausa
- Visualización de la onda generada en tiempo real (vista previa animada)
- Botón “Deshacerútilmo toque” durante la grabación
- Implementación: `TapRecorder.kt` con `SystemClock.elapsedRealtime()` + factor de normalización

### Modo C: Editor de segmentos + Fractal DNA + Layer Mixer

#### Editor base
- Lista de segmentos: cada uno tiene `durationMs` + `amplitude`
- Drag-to-reorder con haptic feedback al levantar
- Swipe-to-delete con confirmación
- Agregar segmento al final o entre dos existentes
- Vista de onda vectorial en la parte superior, se actualiza en tiempo real

#### Feature 1: Fractal / Pattern DNA
Tomás una secuencia base y la replicar a múltiples escalas de tiempo:
- Escalás el patrón base a `×0.5`, `×1` y `×2` (o cualquier combinación)
- Las versiones se intercalan en el timeline creando un patrón fractal
- Slider de "profundidad fractal" (1 = solo base, 3 = 3 niveles de anidado)
- Resultado: complejidad orgánica y autósimilar desde una forma simple
- Ejemplo: base `[on 80ms amp 150, off 40ms]` → fractal 2 niveles:
  `[on 40ms, off 20ms, on 80ms amp 150, off 40ms, on 160ms amp 100, off 80ms]`
- Implementación: `FractalExpander.kt` — función pura `(base: List<Segment>, depth: Int) -> List<Segment>`

#### Feature 2: Layer Mixer (capas de patrones)
Mezclador de patrones existentes como capas:
- Podés combinar hasta 4 patrones (de la biblioteca preset o propios) en capas
- Cada capa tiene su propio slider de intensidad (0–100%)
- Las amplitudes se suman por frame, clampeadas a 255
- Preview háptico del resultado mezclado en tiempo real
- Ejemplo: Caricia × 40% + Pulso × 60% = patrón hírido suave-rítmico
- El resultado se puede guardar como nuevo patrón propio
- Implementación: `PatternMixer.kt` — alinea arrays por mínimo común múltiplo de duración

### Flujo de creación (todos los modos)
```
[+] Nuevo patrón
       ↓
   elegir modo: Whiteboard | Tap | Segmentos
       ↓
  crear / grabar / mezclar
       ↓
  Sentir (preview háptico)
       ↓
  (opcional) refinar en editor de segmentos
       ↓
  Nombrar + categorizar (SUAVE / ASCENSO / CIMA)
       ↓
  Guardar local  ►  [Publicar en comunidad]
```

### Persistencia local
- **Room DB** (no DataStore — arrays de longitud variable)
- `CustomPatternEntity`: id, name, description, category, timingsJson, amplitudesJson, repeat, createdAt, isPublished
- `CustomPatternDao`: CRUD + `Flow<List<CustomPatternEntity>>`
- `CustomPatternRepository`: convierte entidades → `VivroPattern` (misma clase del engine)
- Los patrones propios aparecen en el selector principal con badge 📌

---

## M11 — Backend comunidad Vibro

> **Backend TBD** — a decidir cuando Gastón tenga PC

Opciones:
- **Firebase** — Firestore + Auth Google. SDK oficial Kotlin/Android.
- **Supabase** — PostgreSQL + REST + Realtime. Open source, self-hosteable.

### Endpoints necesarios
- `POST /patterns` — subir patrón (autenticado)
- `GET /patterns` — listar comunidad (paginado, filtros)
- `GET /patterns/:id` — patrón individual
- `POST /patterns/:id/like` — dar like
- `GET /users/:id/patterns` — patrones de un usuario

### Autenticación
- Google Sign-In (cero fricción en Android)
- Alias público opcional

### Moderación
- Publicación inmediata + sistema de reportes
- Flag `isFlagged` — revisión manual
- Patrones CIMA muestran advertencia de intensidad

---

## M12 — UI comunidad

- **Tab “Comunidad”** en bottom nav (icono globo)
- **Browse**: grid con filtros (Nuevo / Popular / Categoría)
- **Card de patrón**: nombre, autor, categoría, likes, mini-waveform vectorial
- **Preview antes de descargar**: sentir el patrón antes de guardarlo
- **Publicar**: desde creador propio → comunidad (nombre, descripción, categoría)
- **Perfil**: mis patrones publicados, likes recibidos, biblioteca descargada

---

## 6 curvas de rampa

| Tipo | Descripción | Sensación |
|------|-------------|----------|
| LINEAR_UP | Crece lineal | Entrada suave y predecible |
| LINEAR_DOWN | Decrece lineal | Ataque directo, salida suave |
| PARABOLA | x² — lento al inicio | Sorpresa tarda en llegar |
| HYPERBOLA | 1-(1-x)² — rápido al inicio | Impacto inmediato, plateau |
| LOGARITHMIC | ln(x) — subida rápida | Sube rápido, se mantiene |
| EXPONENTIAL | e^x — surge al final | Calma, calma… IMPACTO |

---

## Skins

### Incluidos (M6)
- **Boudoir** (default): `#0A0008` / `#E8185C` / `#C4A84A` — orquídea animada
- **Seda & Piel**: `#150C0C` / `#C0143C` / `#F2A59D` — curvas cálidas

### Adicionales (M7)
- 2 skins claros (paleta luminosa, uso diurno)
- 2 skins explícitos (formas más directas)

---

## Fuera de scope

- iOS
- Bluetooth / control remoto
- Play Store
- Detección de ritmo desde música
- Llamadas / notificaciones integradas
