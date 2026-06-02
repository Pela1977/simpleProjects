package com.gaston.vibro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gaston.vibro.haptics.HapticsViewModel
import com.gaston.vibro.haptics.PatternsRegistry
import com.gaston.vibro.ui.components.FrequencyControls
import com.gaston.vibro.ui.components.IntensitySlider
import com.gaston.vibro.ui.components.PlayStopButton
import com.gaston.vibro.ui.theme.VivroTheme

// Pantalla de prueba M3 — será reemplazada por MainScreen en M6
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VivroTheme {
                val viewModel: HapticsViewModel = viewModel()
                val state by viewModel.state.collectAsState()

                // Selecciona el primer patrón al iniciar
                LaunchedEffect(Unit) {
                    if (state.activePattern == null) {
                        viewModel.selectPattern(PatternsRegistry.all.first())
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF0A0008))
                        .statusBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "vibro",
                        color = Color(0xFFE8185C),
                        style = MaterialTheme.typography.displayMedium
                    )

                    Text(
                        text = state.activePattern?.name ?: "—",
                        color = Color.White.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = state.activePattern?.description ?: "",
                        color = Color.White.copy(alpha = 0.45f),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PlayStopButton(
                        isPlaying = state.isPlaying,
                        onClick = { viewModel.togglePlayback() }
                    )

                    IntensitySlider(
                        level = state.intensityLevel,
                        onLevelChange = { viewModel.setIntensityLevel(it) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    FrequencyControls(
                        onTimeMs = state.onTimeMs,
                        offTimeMs = state.offTimeMs,
                        onOnTimeChange = { viewModel.setOnTime(it) },
                        onOffTimeChange = { viewModel.setOffTime(it) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (!viewModel.hasAmplitudeControl) {
                        Text(
                            text = "⚠️ Modo PWM — dispositivo sin control de amplitud",
                            color = Color(0xFFC4A84A),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
