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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appubicaciones.data.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPlacesScreen(
    allPlaces: List<Place>, // pásame la fuente (mockPlaces o VM)
    categories: List<PlaceCategory> = PlaceCategory.entries,
    onPlaceClick: (Place) -> Unit = {}
) {
    // ===== Filtros (persisten) =====
    var name by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf<PlaceCategory?>(null) }
    var distance by rememberSaveable { mutableStateOf(35) }
    var unit by rememberSaveable { mutableStateOf("Km") }
    var catExpanded by remember { mutableStateOf(false) }
    var unitExpanded by remember { mutableStateOf(false) }

    // Acordeón de filtros
    var showFilters by rememberSaveable { mutableStateOf(true) }

    // Resultados
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
        Text("UniLocal", style = ty.headlineLarge.copy(fontWeight = FontWeight.SemiBold), color = cs.primary)
        Text("Búsqueda de lugares", style = ty.bodyMedium, color = cs.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp))

        // Toggle acordeón
        TextButton(onClick = { showFilters = !showFilters }) {
            Icon(if (showFilters) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore, null)
            Spacer(Modifier.width(6.dp))
            Text(if (showFilters) "Ocultar filtros" else "Mostrar filtros")
        }

        // ===== Filtros =====
        AnimatedVisibility(
            visible = showFilters,
            enter = fadeIn() + expandVertically(),
            exit  = fadeOut() + shrinkVertically()
        ) {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar") },
                    singleLine = true,
                    shape = MaterialTheme.shapes.large
                )

                Spacer(Modifier.height(16.dp))

                Text("Filtrar lugares por categoría", style = ty.labelLarge, color = cs.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text("Categoría:", modifier = Modifier.weight(1f), color = cs.onSurface)
                    ExposedDropdownMenuBox(
                        expanded = catExpanded,
                        onExpandedChange = { catExpanded = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = category?.displayName ?: "Todas",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(catExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(expanded = catExpanded, onDismissRequest = { catExpanded = false }) {
                            DropdownMenuItem(text = { Text("Todas") }, onClick = { category = null; catExpanded = false })
                            categories.forEach { c ->
                                DropdownMenuItem(text = { Text(c.displayName) }, onClick = { category = c; catExpanded = false })
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text("Filtrar lugares dentro de la distancia ingresada", style = ty.labelLarge, color = cs.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text("Distancia:", modifier = Modifier.weight(1f), color = cs.onSurface)
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
                                DropdownMenuItem(text = { Text(u) }, onClick = { unit = u; unitExpanded = false })
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = {
                        results = filterPlaces(allPlaces, name, category, distance, unit)
                        showFilters = false // opcional: plegar tras aplicar
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cs.primaryContainer,
                        contentColor = cs.onPrimaryContainer
                    )
                ) { Text("Aplicar filtros") }
            }
        }

        Spacer(Modifier.height(12.dp))

        // ===== Resultados (lista tipo favoritos) =====
        if (results.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No se encontraron lugares.", color = cs.onSurfaceVariant)
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
                Text(if (expanded) "Ocultar" else "Ver más")
            }

            AnimatedVisibility(visible = expanded, enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkVertically()) {
                Column(Modifier.padding(top = 6.dp)) {
                    Text("Descripción: " + place.description.ifBlank { "Sin descripción" })
                    Text("Teléfono: " + place.phone)
                    Text("Categoría: " + place.category.displayName)
                    Text("Horario: ${place.openDay.displayName} ${place.openingHour} - ${place.closeDay.displayName} ${place.closingHour}")
                }
            }
        }
    }
}

/** Filtro simple: por nombre y categoría.
 * Si quieres filtrar por distancia real, calcula distancias aquí. */
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
