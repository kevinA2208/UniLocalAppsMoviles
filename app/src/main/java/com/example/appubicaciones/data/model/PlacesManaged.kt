package com.example.appubicaciones.data.model

data class PlaceManaged(
    val id: String = "",
    val userId: String = "", // Admin
    val placeId: String = "",
    val approved: Boolean = false
)