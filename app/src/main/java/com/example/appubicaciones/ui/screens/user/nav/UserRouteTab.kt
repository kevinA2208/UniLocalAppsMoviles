package com.example.appubicaciones.ui.screens.user.nav


import kotlinx.serialization.Serializable

sealed class UserRouteTab {

    @Serializable
    data object Map : UserRouteTab()

    @Serializable
    data object Favorites : UserRouteTab()

    @Serializable
    data object UserProfile : UserRouteTab()

    @Serializable
    data object EditProfile : UserRouteTab()

    @Serializable
    data object CreatePlace : UserRouteTab()

    @Serializable
    data object Services : UserRouteTab()

    @Serializable
    data object UserCreatedPlaces : UserRouteTab()

    @Serializable
    data class PlaceDetail(val placeId: Int) : UserRouteTab()
}