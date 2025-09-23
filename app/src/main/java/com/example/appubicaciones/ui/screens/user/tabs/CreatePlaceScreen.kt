package com.example.appubicaciones.ui.screens.user.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.appubicaciones.data.model.Days
import com.example.appubicaciones.data.model.PlaceCategory
import com.example.appubicaciones.ui.screens.generics.DropdownSelector
import com.example.appubicaciones.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaceScreen(
    onSaveClick: (
        name: String,
        description: String,
        dayFrom: Days,
        dayTo: Days,
        openHour: String,
        closeHour: String,
        phones: String,
        category: PlaceCategory,
        address: String
    ) -> Unit
) {
    val scrollState = rememberScrollState()

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var openHour by remember { mutableStateOf("") }
    var closeHour by remember { mutableStateOf("") }
    var phones by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    var dayFrom by remember { mutableStateOf(Days.MONDAY) }
    var dayTo by remember { mutableStateOf(Days.SUNDAY) }
    var category by remember { mutableStateOf(PlaceCategory.FOOD) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(stringResource(R.string.create_place_title), style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Nombre
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(R.string.create_place_name)) },
            modifier = Modifier.fillMaxWidth()
        )

        // Descripción
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(R.string.create_place_description)) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            stringResource(R.string.create_place_schedule_label)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Horario - Desde y Hasta
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                DropdownSelector(
                    label = stringResource(R.string.create_place_from_day),
                    options = Days.entries,
                    selectedOption = dayFrom,
                    onOptionSelected = { dayFrom = it },
                    getOptionLabel = { it.displayName }
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                DropdownSelector(
                    label = stringResource(R.string.create_place_to_day),
                    options = Days.entries,
                    selectedOption = dayTo,
                    onOptionSelected = { dayTo = it },
                    getOptionLabel = { it.displayName }
                )
            }
        }

        // Horas de apertura y cierre
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                value = openHour,
                onValueChange = { openHour = it },
                label = { Text(stringResource(R.string.create_place_from_hour)) },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = closeHour,
                onValueChange = { closeHour = it },
                label = { Text(stringResource(R.string.create_place_to_hour)) },
                modifier = Modifier.weight(1f)
            )
        }

        // Teléfono
        OutlinedTextField(
            value = phones,
            onValueChange = { phones = it },
            label = { Text(stringResource(R.string.create_place_phone)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Categoría
        DropdownSelector(
            label = stringResource(R.string.create_place_category),
            options = PlaceCategory.entries,
            selectedOption = category,
            onOptionSelected = { category = it },
            getOptionLabel = { it.displayName }
        )

        // Dirección
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text(stringResource(R.string.create_place_address)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón guardar
        Button(
            onClick = {
                onSaveClick(
                    name, description, dayFrom, dayTo,
                    openHour, closeHour, phones, category, address
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7237EC))
        ) {
            Text(stringResource(R.string.save_place))
        }
    }
}
