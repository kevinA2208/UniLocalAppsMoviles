package com.example.appubicaciones.data.model

data class User(
    val names: String,
    val lastnames: String,
    val username: String,
    val email: String,
    val city: City,
    val password: String,
    val role: Role
)