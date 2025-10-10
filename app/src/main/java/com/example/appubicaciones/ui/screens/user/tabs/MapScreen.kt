package com.example.appubicaciones.ui.screens.user.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appubicaciones.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onAddPlaceClick: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Barra de búsqueda
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text(stringResource(R.string.map_search_placeholder)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.map_search_icon_description)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSearchClick() },
                enabled = false,
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

            // Texto de mapa no disponible
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.error_map_not_found),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Botón flotante para añadir lugar
        FloatingActionButton(
            onClick = onAddPlaceClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.map_add_place_description)
            )
        }
    }
}