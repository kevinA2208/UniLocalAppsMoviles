package com.example.appubicaciones.ui.screens.user.nav

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appubicaciones.R
import com.example.appubicaciones.config.RouteScreen
import com.example.appubicaciones.data.mocks.listProductsServices
import com.example.appubicaciones.data.mocks.approvedPlaces
import com.example.appubicaciones.data.mocks.mockPlaces
import com.example.appubicaciones.ui.screens.LoginScreen
import com.example.appubicaciones.ui.screens.services.CreateProductServiceScreen
import com.example.appubicaciones.ui.screens.services.DetailProductServiceScreen
import com.example.appubicaciones.data.model.Days
import com.example.appubicaciones.data.model.Place
import com.example.appubicaciones.data.model.PlaceCategory
import com.example.appubicaciones.ui.screens.comments.PlaceCommentsScreen
import com.example.appubicaciones.ui.screens.services.AddImageProductServiceScreen
import com.example.appubicaciones.ui.screens.services.ServiceScreen
import com.example.appubicaciones.ui.screens.user.tabs.CommentDetailScreen
import com.example.appubicaciones.ui.screens.user.tabs.AddImagesScreen
import com.example.appubicaciones.ui.screens.user.tabs.AddLocationScreen
import com.example.appubicaciones.ui.screens.user.tabs.CreatePlaceScreen
import com.example.appubicaciones.ui.screens.user.tabs.EditUserProfileScreen
import com.example.appubicaciones.ui.screens.user.tabs.MapScreen
import com.example.appubicaciones.ui.screens.user.tabs.PlaceDetailScreen
import com.example.appubicaciones.ui.screens.user.tabs.SearchPlacesScreen
import com.example.appubicaciones.ui.screens.user.tabs.ResponseScreen
import com.example.appubicaciones.ui.screens.user.tabs.UserCreatedPlacesScreen
import com.example.appubicaciones.ui.screens.user.tabs.UserFavoritesScreen
import com.example.appubicaciones.ui.screens.user.tabs.UserProfileScreen

@Composable
fun ContentUser(
    padding: PaddingValues,
    tabNavController: NavHostController,
    rootNavController: NavHostController,
    isLoggedIn: Boolean,
    onLoginSuccess: () -> Unit,
    openCreate: Boolean = false
) {
    LaunchedEffect(openCreate, isLoggedIn) {
        if (openCreate && isLoggedIn) {
            tabNavController.navigate(UserRouteTab.CreatePlace)
        }
    }

    var mockProductServices by remember { mutableStateOf(listProductsServices) }

    NavHost(
        modifier = Modifier.padding(padding),
        navController = tabNavController,
        startDestination = UserRouteTab.Map
    ){
        composable<UserRouteTab.Map> {
            MapScreen(
                onAddPlaceClick = {
                    if(isLoggedIn){
                        tabNavController.navigate(UserRouteTab.CreatePlace)
                    } else {
                        tabNavController.navigate(UserRouteTab.UserProfile)
                    }
                },
                onSearchClick = { tabNavController.navigate(UserRouteTab.SearchPlaces) }
            )
        }
        composable<UserRouteTab.CreatePlace> { backStackEntry ->
            val imgsFlow = backStackEntry
                .savedStateHandle
                .getStateFlow("picked_images", emptyList<String>())
            val pickedImageStrings by imgsFlow.collectAsStateWithLifecycle()
            val pickedUris = remember(pickedImageStrings) { pickedImageStrings.map(Uri::parse) }

            val addrFlow = backStackEntry
                .savedStateHandle
                .getStateFlow("picked_address", "")
            val addr by addrFlow.collectAsStateWithLifecycle()

            CreatePlaceScreen(
                initialAddress = addr,
                pickedImages = pickedUris,
                onAddImagesClick = { tabNavController.navigate(UserRouteTab.AddImages) },
                onLoadLocationClick = { tabNavController.navigate(UserRouteTab.AddLocation) },
                onSaveClick = { name, description, dayFrom, dayTo, openHour, closeHour, phones, category, address ->
                    tabNavController.popBackStack()
                }
            )
        }

        composable<UserRouteTab.AddLocation> {
            AddLocationScreen(
                initialAddress = "",
                onSaveLocation = { newAddress ->
                    tabNavController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("picked_address", newAddress)

                    tabNavController.popBackStack()
                },
                onBack = { tabNavController.popBackStack() }
            )
        }

        composable<UserRouteTab.Favorites> {
            val places = remember { approvedPlaces }

            val favs = remember { mutableStateMapOf<Int, Boolean>().apply {
                places.forEach { put(it.id, true) }
            }}

            if (!isLoggedIn) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.favorites_need_login),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            } else {
                UserFavoritesScreen(
                    places = places,
                    isLoggedIn = isLoggedIn,
                    onPlaceClick = { place ->
                        tabNavController.navigate(UserRouteTab.PlaceDetail(place.id))
                    },
                    onToggleFavorite = { place, isFav ->
                        favs[place.id] = isFav
                    }
                )
            }
        }

        composable<UserRouteTab.AddImages> {
            val prevStrings = tabNavController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<List<String>>("picked_images")
                ?: emptyList()
            val prevUris = prevStrings.map(Uri::parse)

            AddImagesScreen(
                initialImages = prevUris,
                onSaveImages = { uris ->
                    tabNavController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("picked_images", uris.map { it.toString() })
                    tabNavController.popBackStack()
                },
                onBack = { tabNavController.popBackStack() }
            )

        }
        composable<UserRouteTab.Services> {
            ServiceScreen(
                navController = tabNavController,
                products = mockProductServices
            )
        }

        composable<UserRouteTab.UserProfile> {
            if(isLoggedIn){
                UserProfileScreen(
                    tabNavController = tabNavController,
                    onEditClick = { tabNavController.navigate(UserRouteTab.EditProfile) },
                    onRecoverPasswordClick = { rootNavController.navigate(RouteScreen.RecoverPassword) },
                    onHistoryClick = { tabNavController.navigate(UserRouteTab.UserCreatedPlaces) }
                )
            } else {
                LoginScreen(
                    onRegisterClick = {
                        rootNavController.navigate(RouteScreen.Register)
                    },
                    onLoginClick = { email, password ->
                        if (email == "usuario@gmail.com" && password == "12345") {
                            onLoginSuccess()

                            tabNavController.navigate(UserRouteTab.Map) {
                                popUpTo(UserRouteTab.UserProfile) { inclusive = true }
                            }
                            true
                        } else {
                            Log.d("LoginScreen", "Credenciales incorrectas")
                            false
                        }
                    },
                    onRecoverPasswordClick = {
                        rootNavController.navigate(RouteScreen.RecoverPassword)
                    }
                )
            }
        }

        composable<UserRouteTab.UserCreatedPlaces> {
            UserCreatedPlacesScreen(
                places = mockPlaces,
                onPlaceClick = { place ->
                    tabNavController.navigate(UserRouteTab.PlaceDetail(place.id))
                }
            )
        }

        composable<UserRouteTab.EditProfile> {
            EditUserProfileScreen(
                onSaveClick = { names, lastnames, username, city ->
                    tabNavController.popBackStack()
                }
            )
        }

        composable<UserRouteTab.Services> {
            ServiceScreen(
                navController = tabNavController,
                products = mockProductServices,
                onViewDetailProduct = { tabNavController.navigate(UserRouteTab.DetailProductService) }
            )
        }

        composable<UserRouteTab.DetailProductService> {
            DetailProductServiceScreen(
                navController = tabNavController,
                product = mockProductServices.get(0)
            )
        }

        composable<UserRouteTab.AddImageProductService> {
            AddImageProductServiceScreen(
                navController = tabNavController
            )
        }

        composable<UserRouteTab.CreateProductService> {
            CreateProductServiceScreen(
                navController = tabNavController
            ) { nuevo ->
                /* TODO */
//                mockProductServices = mockProductServices + nuevo
            }
        }

        composable<UserRouteTab.PlaceDetail> { backStackEntry ->
            val placeId = backStackEntry.arguments?.getInt("placeId")
                ?: backStackEntry.destination.arguments["placeId"]?.defaultValue as? Int
                ?: return@composable

            val place = mockPlaces.find { it.id == placeId }

            place?.let {
                PlaceDetailScreen(
                    place = it,
                    onViewComments = { tabNavController.navigate(UserRouteTab.PlaceComments(it.id)) },
                    onViewProducts = { tabNavController.navigate(UserRouteTab.Services) },
                    onDeletePlace = { /* TODO */ }
                )
            }
        }
        composable<UserRouteTab.EditProfile> {
            EditUserProfileScreen(
                onSaveClick = { names, lastnames, username, city ->
                    tabNavController.popBackStack()
                }
            )
        }

        composable<UserRouteTab.SearchPlaces> {
            SearchPlacesScreen(
                allPlaces = mockPlaces,
                onPlaceClick = { place ->
                    tabNavController.navigate(UserRouteTab.PlaceDetail(place.id))
                }
            )
        }

        composable<UserRouteTab.PlaceComments> { backStackEntry ->
            val placeId = backStackEntry.arguments?.getInt("placeId")
                ?: backStackEntry.destination.arguments["placeId"]?.defaultValue as? Int
                ?: return@composable

            val place = mockPlaces.find { it.id == placeId }

            place?.let {
                PlaceCommentsScreen(
                    placeName = it.name,
                    comments = listOf(
                        "Kevin" to "Excelente lugar",
                        "Ana" to "Muy buena atención",
                        "Carlos" to "Volvería sin dudarlo"
                    ),
                    onCommentClick = { commentIndex ->
                        tabNavController.navigate(UserRouteTab.CommentDetail(it.id, commentIndex))
                    }
                )
            }
        }

        composable<UserRouteTab.CommentDetail> { backStackEntry ->
            val placeId = backStackEntry.arguments?.getInt("placeId")
                ?: backStackEntry.destination.arguments["placeId"]?.defaultValue as? Int
                ?: return@composable

            val commentId = backStackEntry.arguments?.getInt("commentId")
                ?: backStackEntry.destination.arguments["commentId"]?.defaultValue as? Int
                ?: return@composable

            val place = mockPlaces.find { it.id == placeId }

            val comment = "Comentario de ejemplo $commentId"

            place?.let {
                CommentDetailScreen(
                    placeName = it.name,
                    userName = "Usuario Ejemplo",
                    comment = comment,
                    responses = listOf(
                        "Ana" to "Totalmente de acuerdo",
                        "Carlos" to "Buen comentario"
                    ),
                    onRespondClick = { tabNavController.navigate(UserRouteTab.CommentResponse(commentId)) }
                )
            }
        }

        composable<UserRouteTab.CommentResponse> { backStackEntry ->
            val commentId = backStackEntry.arguments?.getInt("commentId")
                ?: backStackEntry.destination.arguments["commentId"]?.defaultValue as? Int
                ?: return@composable

            ResponseScreen(
                navController = tabNavController,
                commentId = commentId
            )
        }
    }
}