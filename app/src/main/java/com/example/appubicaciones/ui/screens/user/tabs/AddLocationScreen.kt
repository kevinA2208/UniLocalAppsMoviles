package com.example.appubicaciones.ui.screens.user.tabs

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appubicaciones.R
import com.example.appubicaciones.ui.screens.generics.Map
import com.mapbox.geojson.Point

@Composable
fun AddLocationScreen(
    initialAddress: String = "",
    onSaveLocation: (Point) -> Unit,
    onBack: () -> Unit = {}
) {
    var clickedPoint by rememberSaveable { mutableStateOf<Point?>(null) }

    val context = LocalContext.current

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

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
                    .height(400.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6))
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {



                    Map (
                        modifier = Modifier
                            .fillMaxSize()
                            .height(400.dp),
                        activateClick = true,
                        onMapClickListener = { point ->
                            clickedPoint = point
                        },
                        zoomValue = 10.0
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Botón Guardar
            Button(
                onClick = { clickedPoint?.let { onSaveLocation(it) } },
                enabled = clickedPoint != null,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF61C38A)) // verde suave
            ) {
                Text(stringResource(R.string.add_location_save_button))
            }
        }
    }
}
