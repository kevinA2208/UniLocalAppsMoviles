package com.example.appubicaciones.data.repository

import com.example.appubicaciones.data.model.Place
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PlaceRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    private val collection = db.collection("places")

    /** Crear un nuevo lugar */
    suspend fun createPlace(place: Place): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: return Result.failure(Exception("Usuario no autenticado"))
            val newId = collection.document().id
            val placeWithId = place.copy(id = newId, userId = userId)
            collection.document(newId).set(placeWithId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Cargar los lugares creados por el usuario actual */
    suspend fun getPlacesByUser(userId: String): List<Place> {
        return try {
            db.collection("places")
                .whereEqualTo("userId", userId)
                .get()
                .await()
                .toObjects(Place::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    /** Obtener todos los lugares (para búsquedas o mapa) */
    suspend fun getAllPlaces(): Result<List<Place>> {
        return try {
            val snapshot = collection.get().await()
            val places = snapshot.toObjects(Place::class.java)
            Result.success(places)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPlaceById(placeId: String): Place? {
        return db.collection("places")
            .document(placeId)
            .get()
            .await()
            .toObject(Place::class.java)
    }

    suspend fun deletePlace(placeId: String): Result<Unit> {
        return try {
            db.collection("places").document(placeId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}