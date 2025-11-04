package com.example.appubicaciones.data.model;


data class Place(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val openDay: Days = Days.MONDAY,
    val closeDay: Days = Days.FRIDAY,
    val openingHour: String = "",
    val closingHour: String = "",
    val phone: String = "",
    val category: PlaceCategory = PlaceCategory.FOOD,
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val userId: String = "",
    val approved: Boolean = false
)