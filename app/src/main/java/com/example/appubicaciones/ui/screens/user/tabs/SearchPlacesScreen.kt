package com.example.appubicaciones.ui.screens.user.tabs

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appubicaciones.data.model.*
import com.example.appubicaciones.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPlacesScreen(
    allPlaces: List<Place>,
    categories: List<PlaceCategory> = PlaceCategory.entries,
    onPlaceClick: (Place) -> Unit = {}
) {
    var name by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf<PlaceCategory?>(null) }
    var distance by rememberSaveable { mutableStateOf(35) }
    var unit by rememberSaveable { mutableStateOf("Km") }
    var catExpanded by remember { mutableStateOf(false) }
    var unitExpanded by remember { mutableStateOf(false) }
    var showFilters by rememberSaveable { mutableStateOf(true) }

    var results by rememberSaveable(stateSaver = listSaver(
        save = { list -> list.map { it.id } },
        restore = { ids -> allPlaces.filter { it.id in ids } }
    )) { mutableStateOf(allPlaces) }

    val cs = MaterialTheme.colorScheme
    val ty = MaterialTheme.typography

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = stringResource(R.string.search_places_title),
            style = ty.bodyMedium,
            color = cs.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        // Toggle acordeón
        TextButton(onClick = { showFilters = !showFilters }) {
            Icon(if (showFilters) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore, null)
            Spacer(Modifier.width(6.dp))
            Text(
                text = if (showFilters)
                    stringResource(R.string.search_places_hide_filters)
                else
                    stringResource(R.string.search_places_show_filters)
            )
        }

        // Filtros
        AnimatedVisibility(
            visible = showFilters,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(stringResource(R.string.search_places_placeholder)) },
                    singleLine = true,
                    shape = MaterialTheme.shapes.large
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    stringResource(R.string.search_places_category_filter_label),
                    style = ty.labelLarge,
                    color = cs.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        stringResource(R.string.search_places_category_label),
                        modifier = Modifier.weight(1f),
                        color = cs.onSurface
                    )
                    ExposedDropdownMenuBox(
                        expanded = catExpanded,
                        onExpandedChange = { catExpanded = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = category?.displayName ?: stringResource(R.string.search_places_all_categories),
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(catExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(expanded = catExpanded, onDismissRequest = { catExpanded = false }) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.search_places_all_categories)) },
                                onClick = { category = null; catExpanded = false }
                            )
                            categories.forEach { c ->
                                DropdownMenuItem(
                                    text = { Text(c.displayName) },
                                    onClick = { category = c; catExpanded = false }
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    stringResource(R.string.search_places_distance_filter_label),
                    style = ty.labelLarge,
                    color = cs.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        stringResource(R.string.search_places_distance_label),
                        modifier = Modifier.weight(1f),
                        color = cs.onSurface
                    )
                    OutlinedTextField(
                        value = distance.toString(),
                        onValueChange = { v -> v.toIntOrNull()?.let { distance = it.coerceIn(0, 999) } },
                        modifier = Modifier.width(90.dp),
                        singleLine = true
                    )
                    Spacer(Modifier.width(8.dp))
                    ExposedDropdownMenuBox(expanded = unitExpanded, onExpandedChange = { unitExpanded = it }) {
                        OutlinedTextField(
                            value = unit,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(unitExpanded) },
                            modifier = Modifier.menuAnchor().width(90.dp)
                        )
                        ExposedDropdownMenu(expanded = unitExpanded, onDismissRequest = { unitExpanded = false }) {
                            listOf("Km", "m").forEach { u ->
                                DropdownMenuItem(
                                    text = { Text(u) },
                                    onClick = { unit = u; unitExpanded = false }
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = {
                        results = filterPlaces(allPlaces, name, category, distance, unit)
                        showFilters = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cs.primaryContainer,
                        contentColor = cs.onPrimaryContainer
                    )
                ) {
                    Text(stringResource(R.string.search_places_apply_filters))
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Resultados
        if (results.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.search_places_no_results), color = cs.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(results, key = { it.id }) { place ->
                    ResultItemCard(place = place, onClick = { onPlaceClick(place) })
                }
            }
        }
    }
}

@Composable
private fun ResultItemCard(place: Place, onClick: () -> Unit) {
    var expanded by rememberSaveable(place.id) { mutableStateOf(false) }

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(horizontal = 12.dp, vertical = 12.dp)) {
            Text(place.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(2.dp))
            Text(place.address, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

            TextButton(onClick = { expanded = !expanded }, modifier = Modifier.align(Alignment.End)) {
                Text(
                    if (expanded)
                        stringResource(R.string.search_places_hide_details)
                    else
                        stringResource(R.string.search_places_view_more)
                )
            }

            AnimatedVisibility(visible = expanded, enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkVertically()) {
                Column(Modifier.padding(top = 6.dp)) {
                    Text(stringResource(R.string.search_places_description, place.description.ifBlank { stringResource(R.string.search_places_no_description) }))
                    Text(stringResource(R.string.search_places_phone, place.phone))
                    Text(stringResource(R.string.search_places_category, place.category.displayName))
                    Text(
                        stringResource(
                            R.string.search_places_schedule,
                            place.openDay.displayName,
                            place.openingHour,
                            place.closeDay.displayName,
                            place.closingHour
                        )
                    )
                }
            }
        }
    }
}

private fun filterPlaces(
    data: List<Place>,
    name: String,
    category: PlaceCategory?,
    distance: Int,
    unit: String
): List<Place> {
    val nameNorm = name.trim().lowercase()
    return data.filter { p ->
        (nameNorm.isBlank() || p.name.lowercase().contains(nameNorm)) &&
                (category == null || p.category == category)
    }
}