package com.example.appubicaciones

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appubicaciones.ui.theme.AppUbicacionesTheme
import com.example.appubicaciones.ui.screens.LoginScreen
import com.example.appubicaciones.ui.screens.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppUbicacionesTheme {
                RegisterScreen(
                    onRegisterClick = { names, lastNames, username, email, city, password ->
                        // Lógica de registro
                    },
                    onLoginClick = {
                        // Navegar a la pantalla de login
                    }
                )
            }
        }
    }
}