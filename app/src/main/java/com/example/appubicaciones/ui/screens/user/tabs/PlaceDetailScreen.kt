package com.example.appubicaciones.ui.screens.user.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appubicaciones.data.model.Place
import androidx.compose.material.icons.outlined.Star as StarOutline
import com.example.appubicaciones.R

@Composable
fun PlaceDetailScreen(
    place: Place,
    onViewComments: () -> Unit = {},
    onViewProducts: () -> Unit = {},
    onDeletePlace: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Título
        Text(
            text = stringResource(R.string.place_detail_title),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 12.dp)
        )

        // Imagen o texto si no hay imagen disponible (por ahora solo texto)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.place_detail_no_images),
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Nombre + botón eliminar (en fila)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = place.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = onDeletePlace,
                modifier = Modifier.size(48.dp) // tamaño fijo
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar lugar",
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(28.dp) // ícono siempre igual
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Estado
        val color = Color(0xFF388E3C)
        Text(
            text = stringResource(R.string.place_detail_open),
            color = color,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Descripción
        Text(
            text = stringResource(R.string.place_detail_description, place.description),
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Dirección
        Text(
            text = stringResource(R.string.place_detail_address, place.address),
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Categoría
        Text(
            text = stringResource(R.string.place_detail_category, place.category.displayName),
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Horario
        Text(
            text = stringResource(
                R.string.place_detail_schedule,
                place.openDay.displayName,
                place.closeDay.displayName,
                place.openingHour,
                place.closingHour
            ),
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Teléfonos
        Text(
            text = stringResource(R.string.place_detail_phone, place.phone),
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Calificaciones (mock de estrellas)
        Text(
            text = stringResource(R.string.place_detail_calification),
            fontWeight = FontWeight.Bold
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            repeat(4) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star",
                    tint = Color(0xFFFFD700)
                )
            }
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = "Star outline",
                tint = Color(0xFFFFD700)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botones inferiores
        Button(
            onClick = onViewComments,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD1C4E9))
        ) {
            Text(stringResource(R.string.place_detail_comments), color = Color.Black)
        }

        Button(
            onClick = onViewProducts,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
        ) {
            Text(stringResource(R.string.place_detail_products_services), color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))

    }
}
