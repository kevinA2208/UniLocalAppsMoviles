package com.example.appubicaciones.data.mocks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.appubicaciones.data.model.ProductoServicio

// Lista simulada de productos / servicios (mock data)
val services = listOf(
    ProductoServicio(
        id = "1",
        nombre = "Subway",
        descripcion = "Restaurante de sándwiches frescos y personalizados.",
        aplicaPrecio = true,
        precio = 18000.0,
        imagenes = listOf("https://upload.wikimedia.org/wikipedia/commons/a/a2/Subway.logo.8.5.svg")
    ),
    ProductoServicio(
        id = "2",
        nombre = "Davivienda",
        descripcion = "Sede de banco Davivienda con cajeros automáticos.",
        aplicaPrecio = false,
        imagenes = listOf("https://upload.wikimedia.org/wikipedia/commons/a/a2/Subway.logo.8.5.svg")
    ),
    ProductoServicio(
        id = "3",
        nombre = "Cinemark",
        descripcion = "Cadena de cines con salas modernas y servicio de confitería.",
        aplicaPrecio = true,
        precio = 25000.0,
        imagenes = listOf("https://upload.wikimedia.org/wikipedia/commons/a/a2/Subway.logo.8.5.svg")
    ),
    ProductoServicio(
        id = "4",
        nombre = "Éxito",
        descripcion = "Supermercado con amplia variedad de productos.",
        aplicaPrecio = false,
        imagenes = listOf("https://upload.wikimedia.org/wikipedia/commons/a/a2/Subway.logo.8.5.svg")
    ),
    ProductoServicio(
        id = "5",
        nombre = "Frisby",
        descripcion = "Restaurante especializado en pollo frito colombiano.",
        aplicaPrecio = true,
        precio = 20000.0,
        imagenes = listOf("https://upload.wikimedia.org/wikipedia/commons/a/a2/Subway.logo.8.5.svg")
    ),
    ProductoServicio(
        id = "6",
        nombre = "Frisby",
        descripcion = "Restaurante especializado en pollo frito colombiano.",
        aplicaPrecio = true,
        precio = 20000.0,
        imagenes = listOf("https://upload.wikimedia.org/wikipedia/commons/a/a2/Subway.logo.8.5.svg")
    ),
    ProductoServicio(
        id = "7",
        nombre = "Frisby",
        descripcion = "Restaurante especializado en pollo frito colombiano.",
        aplicaPrecio = true,
        precio = 20000.0,
        imagenes = listOf("https://upload.wikimedia.org/wikipedia/commons/a/a2/Subway.logo.8.5.svg")
    )
)