package com.gaston.vibro.haptics

object PatternsRegistry {

    // ── SUAVE ────────────────────────────────────────────────────────────────

    val caricia = VivroPattern(
        id = "suave_caricia",
        name = "Caricia",
        category = PatternCategory.SUAVE,
        description = "Suave y continua, como un roce de piel",
        timings    = longArrayOf(200, 30, 200, 30),
        amplitudes = intArrayOf( 45,  0,  45,  0),
        repeat = 0
    )

    val susurro = VivroPattern(
        id = "suave_susurro",
        name = "Susurro",
        category = PatternCategory.SUAVE,
        description = "Apenas perceptible, como un susurro en la piel",
        timings    = longArrayOf(120, 80, 80, 80, 120, 80),
        amplitudes = intArrayOf( 25,  0, 20,  0,  25,  0),
        repeat = 0
    )

    val roce = VivroPattern(
        id = "suave_roce",
        name = "Roce",
        category = PatternCategory.SUAVE,
        description = "Toque breve y ligero con pausa larga",
        timings    = longArrayOf(60, 140, 60, 140),
        amplitudes = intArrayOf(35,   0, 35,   0),
        repeat = 0
    )

    val murmullo = VivroPattern(
        id = "suave_murmullo",
        name = "Murmullo",
        category = PatternCategory.SUAVE,
        description = "Temblor suave y aleteo continuo",
        timings    = longArrayOf(40, 20, 40, 20, 40, 20, 40, 20),
        amplitudes = intArrayOf(30,  0, 30,  0, 30,  0, 30,  0),
        repeat = 0
    )

    val latido = VivroPattern(
        id = "suave_latido",
        name = "Latido",
        category = PatternCategory.SUAVE,
        description = "Ritmo de corazón — lub-dub, pausa, lub-dub",
        timings    = longArrayOf(80, 30, 130, 500),
        amplitudes = intArrayOf(55,  0,  70,   0),
        repeat = 0
    )

    val onda = VivroPattern(
        id = "suave_onda",
        name = "Onda",
        category = PatternCategory.SUAVE,
        description = "Ola que sube y baja con suavidad",
        timings    = longArrayOf(60, 20, 80, 20, 100, 20, 80, 20, 60, 20),
        amplitudes = intArrayOf(30,  0, 50,  0,  75,  0, 50,  0, 30,  0),
        repeat = 0
    )

    val deriva = VivroPattern(
        id = "suave_deriva",
        name = "Deriva",
        category = PatternCategory.SUAVE,
        description = "Lenta y soñadora, como flotar en el agua",
        timings    = longArrayOf(300, 100, 400, 150),
        amplitudes = intArrayOf( 40,   0,  55,   0),
        repeat = 0
    )

    // ── ASCENSO ──────────────────────────────────────────────────────────────

    val oleada = VivroPattern(
        id = "ascenso_oleada",
        name = "Oleada",
        category = PatternCategory.ASCENSO,
        description = "Oleada que crece en intensidad",
        timings    = longArrayOf(100, 30, 150, 30, 200, 50),
        amplitudes = intArrayOf( 70,  0, 100,  0, 130,  0),
        repeat = 0
    )

    val pulso = VivroPattern(
        id = "ascenso_pulso",
        name = "Pulso",
        category = PatternCategory.ASCENSO,
        description = "Pulso fuerte y rítmico, sin pausa",
        timings    = longArrayOf(160, 80, 160, 80),
        amplitudes = intArrayOf(110,  0, 110,  0),
        repeat = 0
    )

    val marea = VivroPattern(
        id = "ascenso_marea",
        name = "Marea",
        category = PatternCategory.ASCENSO,
        description = "Marea poderosa y lenta, como el mar",
        timings    = longArrayOf(250, 60, 350, 90),
        amplitudes = intArrayOf( 90,  0, 120,  0),
        repeat = 0
    )

    val vertigo = VivroPattern(
        id = "ascenso_vertigo",
        name = "Vértigo",
        category = PatternCategory.ASCENSO,
        description = "Escala sin aviso, desorientador",
        timings    = longArrayOf(90, 40, 70, 30, 90, 40, 70, 30, 110, 50),
        amplitudes = intArrayOf(90,  0, 110, 0, 110, 0, 130,  0, 150,  0),
        repeat = 0
    )

    val espiral = VivroPattern(
        id = "ascenso_espiral",
        name = "Espiral",
        category = PatternCategory.ASCENSO,
        description = "Espiral que se aprieta y acelera",
        timings    = longArrayOf(140, 70, 110, 55, 80, 40, 60, 25),
        amplitudes = intArrayOf( 90,  0, 110,  0, 130, 0, 150,  0),
        repeat = 0
    )

    val tormenta = VivroPattern(
        id = "ascenso_tormenta",
        name = "Tormenta",
        category = PatternCategory.ASCENSO,
        description = "Turbulenta e irregular, como una tormenta",
        timings    = longArrayOf(80, 30, 50, 20, 100, 40, 60, 20),
        amplitudes = intArrayOf(130,  0, 150, 0, 120,  0, 160,  0),
        repeat = 0
    )

    val tsunami = VivroPattern(
        id = "ascenso_tsunami",
        name = "Tsunami",
        category = PatternCategory.ASCENSO,
        description = "Una ola imparable que lo arrasa todo",
        timings    = longArrayOf(50, 30, 150, 50, 400, 100),
        amplitudes = intArrayOf(80,  0, 130,  0, 160,   0),
        repeat = 0
    )

    // ── CIMA ─────────────────────────────────────────────────────────────────

    val pulseNova = VivroPattern(
        id = "cima_pulse_nova",
        name = "Pulse Nova",
        category = PatternCategory.CIMA,
        description = "Pulsos rápidos e intensos, como una estrella de neutrones",
        timings    = longArrayOf(50, 20, 50, 20, 50, 20, 50, 20),
        amplitudes = intArrayOf(180, 0, 210, 0, 190, 0, 220, 0),
        repeat = 0
    )

    val bigBang = VivroPattern(
        id = "cima_big_bang",
        name = "Big Bang",
        category = PatternCategory.CIMA,
        description = "Expansión explosiva de cero al universo",
        timings    = longArrayOf(25, 25, 50, 30, 100, 40, 200, 60),
        amplitudes = intArrayOf(100,  0, 150,  0, 200,  0, 240,  0),
        repeat = 0
    )

    val earthquake = VivroPattern(
        id = "cima_earthquake",
        name = "Earthquake",
        category = PatternCategory.CIMA,
        description = "El suelo tiembla sin parar",
        timings    = longArrayOf(25, 10, 25, 10, 30, 15, 40, 15, 25, 10),
        amplitudes = intArrayOf(200,  0, 180,  0, 220, 0, 200,  0, 240,  0),
        repeat = 0
    )

    val volcano = VivroPattern(
        id = "cima_volcano",
        name = "Volcano",
        category = PatternCategory.CIMA,
        description = "Erupcón que escala hasta el máximo",
        timings    = longArrayOf(100, 50, 150, 40, 100, 30, 200, 60, 80, 40),
        amplitudes = intArrayOf(130,  0, 170,  0, 210,  0, 240,  0, 210, 0),
        repeat = 0
    )

    val supernova = VivroPattern(
        id = "cima_supernova",
        name = "Supernova",
        category = PatternCategory.CIMA,
        description = "Explosión estelar seguida de intensidad máxima sostenida",
        timings    = longArrayOf(30, 15, 30, 15, 30, 15, 250, 60),
        amplitudes = intArrayOf(210,  0, 230,  0, 250,  0, 255,  0),
        repeat = 0
    )

    val singularity = VivroPattern(
        id = "cima_singularity",
        name = "Singularity",
        category = PatternCategory.CIMA,
        description = "El máximo absoluto, sostenido, sin escapatoria",
        timings    = longArrayOf(600, 80),
        amplitudes = intArrayOf(255,  0),
        repeat = 0
    )

    val aftershock = VivroPattern(
        id = "cima_aftershock",
        name = "Aftershock",
        category = PatternCategory.CIMA,
        description = "Los ecos del éxtasis, que van disminuyendo",
        timings    = longArrayOf(120, 80, 90, 100, 60, 150, 40, 200),
        amplitudes = intArrayOf(220,  0, 180,   0, 140,   0, 100,   0),
        repeat = 0
    )

    // ── Colecciones ───────────────────────────────────────────────────────────

    val suave: List<VivroPattern> = listOf(
        caricia, susurro, roce, murmullo, latido, onda, deriva
    )

    val ascenso: List<VivroPattern> = listOf(
        oleada, pulso, marea, vertigo, espiral, tormenta, tsunami
    )

    val cima: List<VivroPattern> = listOf(
        pulseNova, bigBang, earthquake, volcano, supernova, singularity, aftershock
    )

    val all: List<VivroPattern> = suave + ascenso + cima

    fun byId(id: String): VivroPattern? = all.find { it.id == id }
}
