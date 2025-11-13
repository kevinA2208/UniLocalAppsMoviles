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
    data object  DetailProductService : UserRouteTab()

    @Serializable
    data object CreateProductService: UserRouteTab()

    @Serializable
    data object AddImageProductService: UserRouteTab()

    @Serializable
    data object UserCreatedPlaces : UserRouteTab()

    @Serializable
    data class PlaceDetail(val placeId: String) : UserRouteTab()

    @Serializable
    data object AddLocation : UserRouteTab()

    @Serializable
    data object AddImages : UserRouteTab()


    @Serializable
    data class PlaceComments(val placeId: String) : UserRouteTab()

    @Serializable
    data class CommentDetail(val placeId: String, val commentId: String) : UserRouteTab()

    @Serializable
    data object SearchPlaces : UserRouteTab()


}