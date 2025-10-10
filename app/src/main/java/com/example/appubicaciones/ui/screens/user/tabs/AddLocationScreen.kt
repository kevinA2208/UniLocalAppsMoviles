package com.example.appubicaciones.ui.screens.user.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appubicaciones.R

@Composable
fun AddLocationScreen(
    initialAddress: String = "",
    onSaveLocation: (String) -> Unit,
    onBack: () -> Unit = {}
) {
    var address by remember { mutableStateOf(initialAddress) }

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Encabezado
            Text(
                text = stringResource(R.string.add_location_app_name),
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFF6A1B9A)
            )
            Text(
                text = stringResource(R.string.add_location_title),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )

            // Contenedor de ícono
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6))
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Image(
                        painter = rememberVectorPainter(Icons.Filled.LocationOn),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Campo de texto
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text(stringResource(R.string.add_location_label_address)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Botón Guardar
            Button(
                onClick = { onSaveLocation(address) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF61C38A)) // verde suave
            ) {
                Text(stringResource(R.string.add_location_save_button))
            }
        }
    }
}
