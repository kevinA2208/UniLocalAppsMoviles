package com.example.appubicaciones.data.model;


data class Place(
    val name: String,
    val description: String,
    val openDay: Days,
    val closeDay: Days,
    val openingHour: String,
    val closingHour: String,
    val phone: String,
    val category: PlaceCategory,
    val address: String
)
