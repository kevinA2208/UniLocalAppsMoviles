package com.example.appubicaciones.ui.screens.user.nav

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appubicaciones.config.RouteScreen
import com.example.appubicaciones.data.model.ProductoServicio
import com.example.appubicaciones.data.mocks.mockPlaces
import com.example.appubicaciones.ui.screens.LoginScreen
import com.example.appubicaciones.ui.screens.ServiceScreen
import com.example.appubicaciones.ui.screens.user.tabs.CreatePlaceScreen
import com.example.appubicaciones.ui.screens.user.tabs.EditUserProfileScreen
import com.example.appubicaciones.ui.screens.user.tabs.MapScreen
import com.example.appubicaciones.ui.screens.user.tabs.PlaceDetailScreen
import com.example.appubicaciones.ui.screens.user.tabs.UserCreatedPlacesScreen
import com.example.appubicaciones.ui.screens.user.tabs.UserFavoritesScreen
import com.example.appubicaciones.ui.screens.user.tabs.UserProfileScreen

@Composable
fun ContentUser(
    padding: PaddingValues,
    tabNavController: NavHostController,
    rootNavController: NavHostController,
    isLoggedIn: Boolean,
    onLoginSuccess: () -> Unit
) {

    // Lista simulada de productos / servicios (mock data)
    var productos by remember {
        mutableStateOf(
            listOf(
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
        )
    }

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
                }
            )
        }
        composable<UserRouteTab.Favorites> {
            UserFavoritesScreen()
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
                            tabNavController.navigate(UserRouteTab.UserProfile) {
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

        composable <UserRouteTab.CreatePlace> {
            CreatePlaceScreen(
                onSaveClick = { name, description, dayFrom, dayTo, openHour, closeHour, phones, category, address ->
                    tabNavController.popBackStack()
                }
            )
        }

        composable<UserRouteTab.Services> {
            ServiceScreen(
                navController = tabNavController,
                productos = productos
            )
        }

        composable<UserRouteTab.PlaceDetail> { backStackEntry ->
            val placeId = backStackEntry.arguments?.getInt("placeId")
                ?: backStackEntry.destination.arguments["placeId"]?.defaultValue as? Int
                ?: return@composable

            val place = mockPlaces.find { it.id == placeId }

            place?.let {
                PlaceDetailScreen(
                    place = it,
                    onViewComments = { /* TODO */ },
                    onViewProducts = { /* TODO */ },
                    onDeletePlace = { /* TODO */ }
                )
            }
        }
    }
}