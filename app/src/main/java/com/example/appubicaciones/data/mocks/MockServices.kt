package com.example.appubicaciones.data.mocks

import com.example.appubicaciones.data.model.ProductService

// Lista simulada de productos / servicios (mock data)
val listProductsServices = listOf(
    ProductService(
        id = "1",
        name = "Subway",
        description = "Restaurante de sándwiches frescos y personalizados.",
        applyPrice = true,
        price = 18000.0,
        images = listOf("https://upload.wikimedia.org/wikipedia/commons/a/a2/Subway.logo.8.5.svg")
    ),
    ProductService(
        id = "2",
        name = "Davivienda",
        description = "Sede de banco Davivienda con cajeros automáticos.",
        applyPrice = false,
        images = listOf("https://upload.wikimedia.org/wikipedia/commons/a/a2/Subway.logo.8.5.svg")
    ),
    ProductService(
        id = "3",
        name = "Cinemark",
        description = "Cadena de cines con salas modernas y servicio de confitería.",
        applyPrice = true,
        price = 25000.0,
        images = listOf("https://upload.wikimedia.org/wikipedia/commons/a/a2/Subway.logo.8.5.svg")
    ),
    ProductService(
        id = "4",
        name = "Éxito",
        description = "Supermercado con amplia variedad de productos.",
        applyPrice = false,
        price = 18000.0,
        images = listOf("https://upload.wikimedia.org/wikipedia/commons/a/a2/Subway.logo.8.5.svg")
    ),
    ProductService(
        id = "5",
        name = "Frisby",
        description = "Restaurante especializado en pollo frito colombiano.",
        applyPrice = true,
        price = 20000.0,
        images = listOf("https://upload.wikimedia.org/wikipedia/commons/a/a2/Subway.logo.8.5.svg")
    ),
    ProductService(
        id = "6",
        name = "Frisby",
        description = "Restaurante especializado en pollo frito colombiano.",
        applyPrice = true,
        price = 20000.0,
        images = listOf("https://upload.wikimedia.org/wikipedia/commons/a/a2/Subway.logo.8.5.svg")
    ),
    ProductService(
        id = "7",
        name = "Frisby",
        description = "Restaurante especializado en pollo frito colombiano.",
        applyPrice = true,
        price = 20000.0,
        images = listOf("https://upload.wikimedia.org/wikipedia/commons/a/a2/Subway.logo.8.5.svg")
    )
)

var mockProductServices = listProductsServices