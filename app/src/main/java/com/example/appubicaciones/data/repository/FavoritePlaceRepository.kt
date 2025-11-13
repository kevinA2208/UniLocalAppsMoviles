package com.example.appubicaciones.data.repository

import com.example.appubicaciones.data.model.FavoritePlace
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FavoritePlaceRepository {

    private val db = FirebaseFirestore.getInstance()
    private val favoritesCollection = db.collection("favorite_places")

    suspend fun addFavoritePlace(favoritePlace: FavoritePlace) {
        favoritesCollection.document(favoritePlace.id).set(favoritePlace).await()
    }

    suspend fun removeFavoritePlace(favoriteId: String) {
        favoritesCollection.document(favoriteId).delete().await()
    }

    suspend fun getFavoritesByUser(userId: String): List<FavoritePlace> {
        val snapshot = favoritesCollection
            .whereEqualTo("userId", userId)
            .get()
            .await()
        return snapshot.toObjects(FavoritePlace::class.java)
    }

    suspend fun isFavorite(userId: String, placeId: String): Boolean {
        val snapshot = favoritesCollection
            .whereEqualTo("userId", userId)
            .whereEqualTo("placeId", placeId)
            .get()
            .await()
        return !snapshot.isEmpty
    }
}
