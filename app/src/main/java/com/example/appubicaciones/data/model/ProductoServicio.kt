package com.example.appubicaciones.data.model

data class ProductoServicio(
    val id: String,
    val nombre: String = "",
    val descripcion: String = "",
    val aplicaPrecio: Boolean = false,
    val precio: Double? = null,
    val imagenes: List<String> = emptyList()
)