package com.example.appubicaciones.ui.screens.user.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.appubicaciones.R

@Composable
fun ResponseScreen(
    navController: NavHostController,
    commentId: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F6FF))
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = stringResource(R.string.response_screen_title),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 16.dp)
        )

        // Formulario
        ResponseForm(
            onSendResponse = { responseText ->
                println("Respuesta enviada al comentario $commentId: $responseText")
                navController.popBackStack()
            },
            onCancel = {
                navController.popBackStack()
            }
        )
    }
}
