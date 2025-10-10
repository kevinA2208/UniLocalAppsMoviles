package com.example.appubicaciones.data.model

data class ProductService(
    val id: String,
    val name: String = "",
    val description: String = "",
    val applyPrice: Boolean = false,
    val price: Double? = null,
    val images: List<String> = emptyList()
)