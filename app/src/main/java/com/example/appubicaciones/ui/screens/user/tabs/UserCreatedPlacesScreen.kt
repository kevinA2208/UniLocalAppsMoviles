package com.example.appubicaciones.ui.screens.user.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appubicaciones.R
import com.example.appubicaciones.data.model.Place

@Composable
fun UserCreatedPlacesScreen(
    places: List<Place>,
    onPlaceClick: (Place) -> Unit
) {

    var showPending by remember { mutableStateOf(false) }

    val filteredPlaces = places.filter { it.verification_completed == showPending }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.created_places_title),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        // Botones de filtro (Aprobados / Pendientes)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { showPending = false },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!showPending) Color(0xFF7237EC) else Color.LightGray
                )
            ) {
                Text(stringResource(R.string.places_approved_title))
            }

            Button(
                onClick = { showPending = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (showPending) Color(0xFF7237EC) else Color.LightGray
                )
            ) {
                Text(stringResource(R.string.places_pending_title))
            }
        }

        if (filteredPlaces.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (showPending)
                        stringResource(R.string.no_created_places_pending)
                    else
                        stringResource(R.string.no_created_places_approved),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        } else {
            // Lista de lugares
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredPlaces) { place ->
                    PlaceItemCard(
                        place = place,
                        onClick = { onPlaceClick(place) }
                    )
                }
            }
        }
    }
}

@Composable
fun PlaceItemCard(
    place: Place,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF9F2FF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = place.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = place.address,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            IconButton(onClick = { onClick() }) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = stringResource(R.string.detail_place),
                    tint = Color.Black
                )
            }
        }
    }
}
