package com.example.appubicaciones.data.mocks

import com.example.appubicaciones.data.model.Days
import com.example.appubicaciones.data.model.Place
import com.example.appubicaciones.data.model.PlaceCategory

val approvedPlaces = listOf(
    Place(
        id = 1,
        name = "Café del Sol",
        description = "Lugar acogedor con buena música",
        openDay = Days.MONDAY,
        closeDay = Days.SUNDAY,
        openingHour = "08:00 AM",
        closingHour = "22:00 PM",
        phone = "3214567890",
        category = PlaceCategory.FOOD,
        address = "Calle 123 #45-67",
        verification_completed = true
    ),
    Place(
        id = 2,
        name = "Parque Central",
        description = "Espacio verde ideal para familias",
        openDay = Days.MONDAY,
        closeDay = Days.SUNDAY,
        openingHour = "06:00 AM",
        closingHour = "20:00 PM",
        phone = "3209876543",
        category = PlaceCategory.PARK,
        address = "Carrera 10 #20-30",
        verification_completed = true
    )
)

val pendingPlaces = listOf(
    Place(
        id = 3,
        name = "Restaurante La Sazón",
        description = "Comida típica con recetas tradicionales",
        openDay = Days.MONDAY,
        closeDay = Days.SATURDAY,
        openingHour = "11:00 AM",
        closingHour = "23:00 PM",
        phone = "3126547890",
        category = PlaceCategory.FOOD,
        address = "Avenida Bolívar #12-34",
        verification_completed = false
    ),
    Place(
        id = 4,
        name = "Galería Arte Vivo",
        description = "Exposición de artistas locales en el centro de la ciudad",
        openDay = Days.TUESDAY,
        closeDay = Days.SATURDAY,
        openingHour = "09:00 AM",
        closingHour = "19:00 PM",
        phone = "3139998888",
        category = PlaceCategory.MUSEUM,
        address = "Carrera 8 #18-22",
        verification_completed = false
    )
)

val mockPlaces = approvedPlaces + pendingPlaces