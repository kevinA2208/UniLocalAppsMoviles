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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appubicaciones.R
import com.example.appubicaciones.ui.screens.generics.Map
import com.example.appubicaciones.viewmodel.PlaceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onAddPlaceClick: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {

    val placeViewModel: PlaceViewModel = viewModel()
    val places by placeViewModel.places.collectAsState()

    LaunchedEffect(Unit) {
        placeViewModel.getAllPlaces()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
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

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Mapa
                Map(
                    modifier = Modifier.fillMaxSize(),
                    places = places,
                    zoomValue = 2.0
                )

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
    }
}

