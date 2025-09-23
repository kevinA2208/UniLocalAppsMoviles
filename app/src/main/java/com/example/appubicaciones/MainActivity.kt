package com.example.appubicaciones

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.appubicaciones.ui.theme.AppUbicacionesTheme
import com.example.appubicaciones.ui.screens.Navigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppUbicacionesTheme {
                Navigation()
            }
        }
    }
}