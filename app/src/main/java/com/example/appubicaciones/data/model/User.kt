package com.example.appubicaciones.data.model

data class User(
    val id: String = "",
    val names: String = "",
    val lastnames: String = "",
    val username: String = "",
    val email: String = "",
    val city: City = City.ARMENIA,
    val role: Role = Role.USER
)