package com.example.appubicaciones.ui.screens.user.tabs

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.appubicaciones.R
import com.example.appubicaciones.data.model.Days
import com.example.appubicaciones.data.model.PlaceCategory
import com.example.appubicaciones.ui.screens.generics.DropdownSelector

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreatePlaceScreen(
    initialAddress: String = "",
    pickedImages: List<Uri> = emptyList(),
    onAddImagesClick: () -> Unit,
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
    ) -> Unit,
    onLoadLocationClick: () -> Unit = {}
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

    val selectedImages = remember { mutableStateListOf<Uri>() }
    val maxImages = 6

    val pickImages = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxImages)
    ) { uris ->
        selectedImages.clear()
        selectedImages.addAll(uris.take(maxImages))
    }


    LaunchedEffect(initialAddress) {
        if (initialAddress.isNotBlank()) address = initialAddress
    }
    if (pickedImages.isNotEmpty()) {
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Imágenes seleccionadas: ${pickedImages.size}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.create_place_title), style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(R.string.create_place_name)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(R.string.create_place_description)) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 4
        )

        Spacer(Modifier.height(8.dp))
        Text(stringResource(R.string.create_place_schedule_label))
        Spacer(Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(Modifier.weight(1f)) {
                DropdownSelector(
                    label = stringResource(R.string.create_place_from_day),
                    options = Days.entries,
                    selectedOption = dayFrom,
                    onOptionSelected = { dayFrom = it },
                    getOptionLabel = { it.displayName }
                )
            }
            Box(Modifier.weight(1f)) {
                DropdownSelector(
                    label = stringResource(R.string.create_place_to_day),
                    options = Days.entries,
                    selectedOption = dayTo,
                    onOptionSelected = { dayTo = it },
                    getOptionLabel = { it.displayName }
                )
            }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
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

        OutlinedTextField(
            value = phones,
            onValueChange = { phones = it },
            label = { Text(stringResource(R.string.create_place_phone)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        DropdownSelector(
            label = stringResource(R.string.create_place_category),
            options = PlaceCategory.entries,
            selectedOption = category,
            onOptionSelected = { category = it },
            getOptionLabel = { it.displayName }
        )

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text(stringResource(R.string.create_place_address)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onAddImagesClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2D9FF)),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .weight(1f)
                    .height(45.dp)
            ) {
                Text("Imágenes", color = Color(0xFF5E35B1), style = MaterialTheme.typography.bodyMedium)
            }

            OutlinedButton(
                onClick = onLoadLocationClick,
                border = BorderStroke(1.dp, Color(0xFF5E35B1)),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.weight(1f).height(45.dp)
            ) { Text("Cargar ubicación", color = Color(0xFF5E35B1), style = MaterialTheme.typography.bodyMedium) }
        }

        if (selectedImages.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                maxItemsInEachRow = 3,
                modifier = Modifier.fillMaxWidth()
            ) {
                selectedImages.forEachIndexed { index, uri ->
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        AsyncImage(
                            model = uri,
                            contentDescription = "Imagen $index",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize()
                        )
                        IconButton(
                            onClick = { selectedImages.remove(uri) },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Eliminar",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            AssistChip(
                onClick = { selectedImages.clear() },
                label = { Text("Quitar todas") }
            )
        }
        Spacer(Modifier.height(16.dp))

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
