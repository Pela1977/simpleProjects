package com.gaston.vibro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaston.vibro.haptics.HapticsViewModel
import com.gaston.vibro.haptics.PatternsRegistry
import com.gaston.vibro.ui.screens.CreatorScreen
import com.gaston.vibro.ui.screens.FavoritesScreen
import com.gaston.vibro.ui.screens.MainScreen
import com.gaston.vibro.ui.screens.SkinsScreen
import com.gaston.vibro.ui.theme.VivroTheme

private enum class VibroTab(val label: String) {
    HOME("Inicio"),
    CREATE("Crear"),
    FAVORITES("Favoritos"),
    SKINS("Skins")
}

class MainActivity : ComponentActivity() {

    private val viewModel: HapticsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val skin by viewModel.skin.collectAsState()
            VivroTheme(skin = skin) {
                VibroApp(viewModel)
            }
        }
    }

    // Regla innegociable: nunca dejar vibración zombie al salir o pausar.
    override fun onPause() {
        super.onPause()
        viewModel.stop()
    }
}

@Composable
private fun VibroApp(viewModel: HapticsViewModel) {
    val skin = com.gaston.vibro.ui.theme.LocalVivroSkin.current
    val state by viewModel.state.collectAsState()
    var activeTab by remember { mutableStateOf(VibroTab.HOME) }

    LaunchedEffect(Unit) {
        if (state.activePattern == null) {
            viewModel.selectPattern(PatternsRegistry.all.first())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(skin.colors.background)
            .statusBarsPadding()
    ) {
        Box(modifier = Modifier.weight(1f)) {
            when (activeTab) {
                VibroTab.HOME -> MainScreen(viewModel)
                VibroTab.CREATE -> CreatorScreen(viewModel)
                VibroTab.FAVORITES -> FavoritesScreen(viewModel)
                VibroTab.SKINS -> SkinsScreen(viewModel)
            }
        }

        // Bottom nav propio, fiel a la estética del skin
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(skin.colors.surface)
                .navigationBarsPadding()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            VibroTab.entries.forEach { tab ->
                val isActive = tab == activeTab
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { activeTab = tab }
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .background(
                                color = if (isActive) skin.colors.primary
                                    else skin.colors.textSecondary.copy(alpha = 0.35f),
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                            .padding(if (isActive) 5.dp else 3.dp)
                    )
                    Text(
                        text = tab.label,
                        color = if (isActive) skin.colors.primary
                            else skin.colors.textSecondary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}
