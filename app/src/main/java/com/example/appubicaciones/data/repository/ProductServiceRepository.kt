package com.example.appubicaciones.data.repository

import com.example.appubicaciones.data.model.ProductService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductServiceRepository {

    private val db = FirebaseFirestore.getInstance()
    private val productsRef = db.collection("products_services")

    /** Crear nuevo producto/servicio */
    suspend fun createProductService(product: ProductService): Boolean {
        return try {
            productsRef.document(product.id).set(product).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /** Obtener todos los productos de un lugar */
    suspend fun getProductsByPlace(placeId: String): List<ProductService> {
        return try {
            productsRef.whereEqualTo("placeId", placeId)
                .get()
                .await()
                .toObjects(ProductService::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /** Obtener un producto por ID */
    suspend fun getProductById(productId: String): ProductService? {
        return try {
            productsRef.document(productId)
                .get()
                .await()
                .toObject(ProductService::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /** Eliminar un producto */
    suspend fun deleteProductService(productId: String): Boolean {
        return try {
            productsRef.document(productId).delete().await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
