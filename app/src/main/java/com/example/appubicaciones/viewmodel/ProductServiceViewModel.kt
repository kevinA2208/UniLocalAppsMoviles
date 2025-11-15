package com.example.appubicaciones.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appubicaciones.data.model.ProductService
import com.example.appubicaciones.data.repository.CloudinaryRepository
import com.example.appubicaciones.data.repository.ProductServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductServiceViewModel : ViewModel() {

    private val repo = ProductServiceRepository()
    private val cloudinaryRepo = CloudinaryRepository()

    private val _products = MutableStateFlow<List<ProductService>>(emptyList())
    val products: StateFlow<List<ProductService>> = _products

    private val _selectedProduct = MutableStateFlow<ProductService?>(null)
    val selectedProduct: StateFlow<ProductService?> = _selectedProduct

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    /** Crear producto */
    fun createProduct(product: ProductService, imageUris: List<Uri>, context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _isSuccess.value = false

            try {
                // 1. Subir imágenes a Cloudinary
                val uploadResult = cloudinaryRepo.uploadImages(
                    imageUris,
                    context,
                    "products_upload"
                )

                if (uploadResult.isSuccess) {
                    val imageUrls = uploadResult.getOrNull() ?: emptyList()

                    // 2. Crear el objeto con las URLs
                    val productWithImages = product.copy(images = imageUrls)

                    // 3. Guardar en Firestore
                    val saveResult = repo.createProductService(productWithImages)
                    _isSuccess.value = saveResult
                    if (!saveResult) _errorMessage.value = "Error al guardar el producto"

                } else {
                    // La subida a Cloudinary falló
                    _errorMessage.value = uploadResult.exceptionOrNull()?.message ?: "Error al subir imágenes"
                }

            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    /** Cargar productos por ID de lugar */
    fun loadProductsForPlace(placeId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null

                val result = repo.getProductsByPlace(placeId)
                _products.value = result

            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error cargando productos"
                _products.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /** Obtener producto por ID */
    fun loadProductById(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _selectedProduct.value = repo.getProductById(productId)
            _isLoading.value = false
        }
    }

    /** Eliminar producto */
    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repo.deleteProductService(productId)
            _isSuccess.value = result
            if (!result) _errorMessage.value = "No se pudo eliminar el producto"
            _isLoading.value = false
        }
    }

    fun resetStatus() {
        _isSuccess.value = false
        _errorMessage.value = null
    }
}
