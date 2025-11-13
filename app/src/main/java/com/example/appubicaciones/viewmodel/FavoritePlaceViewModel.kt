package com.example.appubicaciones.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appubicaciones.data.model.FavoritePlace
import com.example.appubicaciones.data.model.Place
import com.example.appubicaciones.data.repository.FavoritePlaceRepository
import com.example.appubicaciones.data.repository.PlaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class FavoritePlaceViewModel : ViewModel() {

    private val repo = FavoritePlaceRepository()
    private val placeRepo = PlaceRepository()

    private val _favorites = MutableStateFlow<List<FavoritePlace>>(emptyList())
    val favorites: StateFlow<List<FavoritePlace>> = _favorites

    private val _favoritePlaces = MutableStateFlow<List<Place>>(emptyList())
    val favoritePlaces: StateFlow<List<Place>> = _favoritePlaces

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadFavoritesForUser(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Obtener los favoritos (solo userId/placeId)
                val favs = repo.getFavoritesByUser(userId)
                _favorites.value = favs

                // Consultar los detalles de cada lugar
                val places = mutableListOf<Place>()
                for (fav in favs) {
                    placeRepo.getPlaceById(fav.placeId)?.let { places.add(it) }
                }
                _favoritePlaces.value = places

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(userId: String, placeId: String) {
        viewModelScope.launch {
            try {
                val isFav = repo.isFavorite(userId, placeId)
                if (isFav) {
                    val fav = _favorites.value.find { it.placeId == placeId }
                    fav?.let { repo.removeFavoritePlace(it.id) }
                    _favorites.value = _favorites.value.filter { it.placeId != placeId }
                } else {
                    val newFav = FavoritePlace(
                        id = UUID.randomUUID().toString(),
                        userId = userId,
                        placeId = placeId
                    )
                    repo.addFavoritePlace(newFav)
                    _favorites.value = _favorites.value + newFav
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun isFavorite(placeId: String): Boolean {
        return _favorites.value.any { it.placeId == placeId }
    }
}
