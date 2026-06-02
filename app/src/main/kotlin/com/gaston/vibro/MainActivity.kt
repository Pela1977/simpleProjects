package com.gaston.vibro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.gaston.vibro.ui.theme.VivroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VivroTheme {
                // M3+: reemplazar por MainScreen()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF0A0008)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "vibro",
                        color = Color(0xFFE8185C),
                        fontSize = 28.sp
                    )
                }
            }
        }
    }
}
