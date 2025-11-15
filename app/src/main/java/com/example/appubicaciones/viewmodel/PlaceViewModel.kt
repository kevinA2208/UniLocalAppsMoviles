package com.example.appubicaciones.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appubicaciones.data.model.Place
import com.example.appubicaciones.data.model.PlaceCategory
import com.example.appubicaciones.data.repository.PlaceRepository
import com.example.appubicaciones.data.repository.CloudinaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.content.Context
import android.net.Uri

class PlaceViewModel(
    private val repository: PlaceRepository = PlaceRepository()
) : ViewModel() {

    private val cloudinaryRepository = CloudinaryRepository()

    private val _places = MutableStateFlow<List<Place>>(emptyList())
    val places: StateFlow<List<Place>> = _places

    private val _filteredPlaces = MutableStateFlow<List<Place>>(emptyList())
    val filteredPlaces: StateFlow<List<Place>> = _filteredPlaces

    private val _userPlaces = MutableStateFlow<List<Place>>(emptyList())
    val userPlaces: StateFlow<List<Place>> = _userPlaces

    private val _selectedPlace = MutableStateFlow<Place?>(null)
    val selectedPlace: StateFlow<Place?> = _selectedPlace

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    /** Crear lugar */
    fun createPlace(place: Place, imageUris: List<Uri>, context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            _isSuccess.value = false
            _errorMessage.value = null

            try {
                // 1. Subir imágenes a Cloudinary
                val uploadResult = cloudinaryRepository.uploadImages(imageUris, context, "places_upload")

                if (uploadResult.isSuccess) {
                    val imageUrls = uploadResult.getOrNull() ?: emptyList()

                    // 2. Crear el objeto Place con las URLs
                    val placeWithImages = place.copy(images = imageUrls)

                    // 3. Guardar el Place en Firestore
                    val createResult = repository.createPlace(placeWithImages)
                    _isLoading.value = false

                    createResult.onSuccess {
                        _isSuccess.value = true
                    }.onFailure {
                        _errorMessage.value = it.message
                    }
                } else {
                    // La subida a Cloudinary falló
                    _isLoading.value = false
                    _errorMessage.value = uploadResult.exceptionOrNull()?.message ?: "Error al subir imágenes"
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = e.message
            }
        }
    }

    /** Obtener todos los lugares de Firestore */
    fun getAllPlaces() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getAllPlaces()
            _isLoading.value = false
            result.onSuccess { list ->
                _places.value = list
                _filteredPlaces.value = list
            }.onFailure {
                _errorMessage.value = it.message
            }
        }
    }

    /** Filtrar lugares por nombre y categoría */
    fun filterPlaces(name: String, category: PlaceCategory?) {
        val nameNorm = name.trim().lowercase()
        _filteredPlaces.value = _places.value.filter { p ->
            (nameNorm.isBlank() || p.name.lowercase().contains(nameNorm)) &&
                    (category == null || p.category == category)
        }
    }

    /** Cargar lugares del usuario */
    fun getPlacesByUser(userId: String) {
        viewModelScope.launch {
            val places = repository.getPlacesByUser(userId)
            _userPlaces.value = places
        }
    }

    fun getPlaceById(placeId: String) {
        viewModelScope.launch {
            val place = repository.getPlaceById(placeId)
            _selectedPlace.value = place
        }
    }

    fun deletePlace(placeId: String, userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _isSuccess.value = false

            val result = repository.deletePlace(placeId)
            _isLoading.value = false

            result.onSuccess {
                _isSuccess.value = true
                getPlacesByUser(userId)
            }.onFailure {
                _errorMessage.value = it.message
            }
        }
    }

    fun resetStatus() {
        _isSuccess.value = false
        _errorMessage.value = null
    }
}