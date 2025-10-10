package com.example.appubicaciones.ui.screens.user.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appubicaciones.R
import com.example.appubicaciones.data.model.Days
import com.example.appubicaciones.data.model.Place
import com.example.appubicaciones.data.model.PlaceCategory

@Composable
fun UserFavoritesScreen(
    places: List<Place>,
    isLoggedIn: Boolean,
    onPlaceClick: (Place) -> Unit = {},
    onToggleFavorite: (Place, Boolean) -> Unit = { _, _ -> }
) {
    val favMap = remember(places) {
        mutableStateMapOf<Int, Boolean>().apply {
            places.forEach { put(it.id, true) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        // Título principal
        Text(
            text = stringResource(R.string.favorites_title),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 12.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (places.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.favorites_empty_message),
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(places, key = { it.id }) { place ->
                        var expanded by rememberSaveable(place.id) { mutableStateOf(false) }

                        FavoriteItemCard(
                            place = place,
                            isFavorite = favMap[place.id] == true,
                            expanded = expanded,
                            onToggleExpand = { expanded = !expanded },
                            onRowClick = { onPlaceClick(place) },
                            showFavoriteAction = isLoggedIn,
                            onToggleFavorite = { nowFav ->
                                favMap[place.id] = nowFav
                                onToggleFavorite(place, nowFav)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteItemCard(
    place: Place,
    isFavorite: Boolean,
    expanded: Boolean,
    onToggleExpand: () -> Unit,
    onRowClick: () -> Unit,
    showFavoriteAction: Boolean,
    onToggleFavorite: (Boolean) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4ECFF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = place.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                    color = Color(0xFF2B2B2B)
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = place.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B6B6B)
                )
            }

            if (showFavoriteAction) {
                val favColor by animateColorAsState(
                    targetValue = if (isFavorite) Color(0xFF7237EC) else Color(0xFFBDBDBD),
                    label = "favTint"
                )
                IconButton(onClick = { onToggleFavorite(!isFavorite) }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = stringResource(
                            if (isFavorite) R.string.favorites_remove else R.string.favorites_add
                        ),
                        tint = favColor
                    )
                }
            }

            IconButton(onClick = onToggleExpand) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = stringResource(
                        if (expanded) R.string.favorites_collapse else R.string.favorites_expand
                    ),
                    tint = Color(0xFF7237EC)
                )
            }
        }

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                DetailRow(
                    stringResource(R.string.favorites_description),
                    place.description.ifBlank { stringResource(R.string.favorites_no_description) }
                )
                DetailRow(stringResource(R.string.favorites_phone), place.phone)
                DetailRow(stringResource(R.string.favorites_category), place.category.displayName)
                DetailRow(stringResource(R.string.favorites_open), "${place.openDay.displayName} ${place.openingHour}")
                DetailRow(stringResource(R.string.favorites_close), "${place.closeDay.displayName} ${place.closingHour}")

                TextButton(
                    onClick = onRowClick,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = stringResource(R.string.favorites_see_more),
                        color = Color(0xFF7237EC)
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(Modifier.padding(vertical = 4.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color(0xFF6A1B9A))
        Text(value, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF2B2B2B))
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewFavorites() {
    val mock = listOf(
        Place(1, "Lugar 1","", Days.MONDAY,Days.SUNDAY,"08:00","18:00","+57", PlaceCategory.PARK,"Dirección 1"),
        Place(2, "Lugar 2","", Days.MONDAY,Days.SUNDAY,"08:00","18:00","+57", PlaceCategory.FOOD,"Dirección 2"),
        Place(3, "Lugar 3","", Days.MONDAY,Days.SUNDAY,"08:00","18:00","+57", PlaceCategory.MUSEUM,"Dirección 3")
    )
    UserFavoritesScreen(places = mock, isLoggedIn = true)
}
