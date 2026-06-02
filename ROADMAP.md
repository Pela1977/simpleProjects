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

### Tres modos de creación

#### Modo A: Whiteboard (principal)
El usuario dibuja la forma de onda directamente en un canvas:
- **Eje X** = tiempo (el ancho del canvas representa la duración total del ciclo)
- **Eje Y** = intensidad (arriba = máximo, abajo = silencio)
- El dedo dibuja la curva de amplitud
- Cuando el dedo toca = motor encendido; cuando se levanta = off
- La curva se muestrea cada ~20ms para generar `timings[] + amplitudes[]`
- Grid de referencia (líneas de cuadrícula) para orientación
- Botón “sentir” para previsualizar hapticamente mientras dibújas
- Implementación: `WaveformCanvas.kt` con `Modifier.pointerInput` + `Canvas` composable

#### Modo B: Tap-to-record
- Un área grande tactil en el centro
- Cada toque = pulso (duración = tiempo que se mantiene el dedo)
- El gap entre toques = pausa
- La intensidad puede fijarse antes o variarse con la presión (si el device la soporta)
- Preview visual de la onda generada en tiempo real
- Implementación: `TapRecorder.kt` con `detectTapGestures` + timer

#### Modo C: Editor por segmentos
- Lista de segmentos: cada uno tiene duración (ms) + amplitud (0-255)
- Drag-to-reorder, swipe-to-delete
- Agregar segmento: aparece row con sliders
- Vista de onda vectorial arriba que se actualiza en tiempo real
- El modo más técnico y preciso
- Implementación: `SegmentEditor.kt` con `LazyColumn` + `DragAndDropColumn`

### Flujo de creación
```
[+] Nuevo patrón
       ↓
   elegir modo
  (A | B | C)
       ↓
  crear waveform
       ↓
  sentir (preview)
       ↓
  (opcional) refinar en editor de segmentos
       ↓
  nombrar + categorizar
       ↓
  guardar local  →  [publicar en comunidad]
```

### Persistencia local
- **Room DB** (no DataStore — los patrones son listas de longitud variable)
- Entidad: `CustomPatternEntity` con `timingsJson` + `amplitudesJson` (TypeConverter)
- DAO: CRUD + query por categoría + query por `isPublished`
- Repository: `CustomPatternRepository` que expone `Flow<List<CustomPattern>>`
- Los patrones propios aparecen en el selector principal con badge 📌

---

## M11 — Backend comunidad Vibro

> **Backend TBD** — a decidir cuando Gastón tenga PC

Opciones evaluadas:
- **Firebase** — Firestore + Auth (Google Sign-In). SDK oficial Kotlin/Android. Gratis hasta escala moderada.
- **Supabase** — PostgreSQL + REST + Realtime. Open source, self-hosteable, TypeScript edge functions.

### Endpoints necesarios (independientes del stack)
- `POST /patterns` — subir patrón (autenticado)
- `GET /patterns` — listar comunidad (paginado, filtros por categoría/popularidad/nuevo)
- `GET /patterns/:id` — patrón individual
- `POST /patterns/:id/like` — dar like
- `GET /users/:id/patterns` — patrones de un usuario

### Autenticación
- Google Sign-In (cero fricción en Android, no requiere email/password)
- Alias público opcional (ej. “vibro_gaston”)

### Moderación
- Los patrones se publican inmediatamente pero pueden ser reportados
- Flag `isFlagged` en base de datos — revisado manualmente
- Los patrones CIMA van con advertencia de intensidad

---

## M12 — UI comunidad

- **Pantalla Comunidad**: tab en bottom nav (icono globo)
- **Browse**: grid de patrones, filtros (Nuevo / Popular / Categoría)
- **Card de patrón**: nombre, autor, categoría, likes, mini-preview waveform
- **Preview antes de descargar**: sentir el patrón antes de guardarlo
- **Publicar**: botón en pantalla de creación, agrega nombre del autor y descripción
- **Perfil**: mis patrones publicados, likes recibidos, patrones descargados

---

## 6 curvas de rampa

| Tipo | Descripción | Sensación |
|------|-------------|----------|
| LINEAR_UP | Crece lineal | Entrada suave y predecible |
| LINEAR_DOWN | Decrece lineal | Ataque directo, salida suave |
| PARABOLA | x² — lento al inicio | Sorpresa tarda en llegar |
| HYPERBOLA | 1-(1-x)² — rápido al inicio | Impacto inmediato, plateau |
| LOGARITHMIC | ln(x) — subida rápida | Sube rapido, se mantiene |
| EXPONENTIAL | e^x — surge al final | Calma, calma... IMPACTO |

---

## Skins

### Incluidos (M6)
- **Boudoir** (default): `#0A0008` / `#E8185C` / `#C4A84A` — orquídea animada, tipografía elegante
- **Seda & Piel**: `#150C0C` / `#C0143C` / `#F2A59D` — curvas cálidas, sensual

### Adicionales (M7)
- 2 skins claros (paleta luminosa, uso diurno)
- 2 skins explícitos (formas más directas, activo/off toggle en settings)

---

## Fuera de scope

- iOS
- Bluetooth / control remoto
- Play Store
- Detección de ritmo desde música
- Llamadas / notificaciones integradas
