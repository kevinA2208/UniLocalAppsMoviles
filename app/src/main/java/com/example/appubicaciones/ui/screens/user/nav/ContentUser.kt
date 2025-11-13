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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.appubicaciones.data.model.City
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
import com.example.appubicaciones.viewmodel.UserViewModel

@Composable
fun ContentUser(
    padding: PaddingValues,
    tabNavController: NavHostController,
    rootNavController: NavHostController,
    isLoggedIn: Boolean,
    onLoginSuccess: () -> Unit,
    openCreate: Boolean = false,
    onLogout: () -> Unit
) {
    LaunchedEffect(openCreate, isLoggedIn) {
        if (openCreate && isLoggedIn) {
            tabNavController.navigate(UserRouteTab.CreatePlace)
        }
    }

    val userViewModel: UserViewModel = viewModel()

    val isLoading by userViewModel.isLoading.collectAsState()
    val isSuccess by userViewModel.isSuccess.collectAsState()
    val errorMessage by userViewModel.errorMessage.collectAsState()
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

            val favs = remember { mutableStateMapOf<String, Boolean>().apply {
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

            val currentUser by userViewModel.currentUser.collectAsState()
            val isLoading by userViewModel.isLoading.collectAsState()
            val isSuccess by userViewModel.isSuccess.collectAsState()
            val errorMessage by userViewModel.errorMessage.collectAsState()

            if (isSuccess && currentUser != null) {
                // Usuario autenticado correctamente
                UserProfileScreen(
                    names = currentUser?.names ?: "",
                    lastnames = currentUser?.lastnames ?: "",
                    username = currentUser?.username ?: "",
                    email = currentUser?.email ?: "",
                    city = currentUser?.city?.displayName ?: "",
                    tabNavController = tabNavController,
                    onEditClick = { tabNavController.navigate(UserRouteTab.EditProfile) },
                    onRecoverPasswordClick = { rootNavController.navigate(RouteScreen.RecoverPassword) },
                    onHistoryClick = { tabNavController.navigate(UserRouteTab.UserCreatedPlaces) },
                    onLogoutClick = {
                        // Limpiar datos en Firebase y memoria
                        userViewModel.logoutUser()

                        // Mover la pestaña interna al Mapa (para que no se quede en Profile)
                        tabNavController.navigate(UserRouteTab.Map) {
                            popUpTo(UserRouteTab.Map) { inclusive = true }
                        }

                        // Avisar al Root que ya no estamos logueados (para ocultar favoritos, etc.)
                        onLogout()
                    }
                )
            } else {
                // Usuario no autenticado → mostrar pantalla de login
                LoginScreen(
                    onRegisterClick = {
                        rootNavController.navigate(RouteScreen.Register)
                    },
                    onLoginClick = { email, password ->
                        userViewModel.loginUser(email, password)
                    },
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    onRecoverPasswordClick = {
                        rootNavController.navigate(RouteScreen.RecoverPassword)
                    }
                )

                errorMessage?.let {
                    Log.e("Login", "Error al iniciar sesión: $it")
                }
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
            val currentUser by userViewModel.currentUser.collectAsState()

            currentUser?.let { user ->
                EditUserProfileScreen(
                    initialNames = user.names,
                    initialLastnames = user.lastnames,
                    initialUsername = user.username,
                    initialCity = user.city.displayName,
                    onSaveClick = { names, lastnames, username, city ->

                        val updatedUser = user.copy(
                            names = names,
                            lastnames = lastnames,
                            username = username,
                            city = City.entries.first { it.displayName == city }
                        )

                        userViewModel.updateUser(updatedUser) { success ->
                            if (success) {
                                userViewModel.loadCurrentUser() // 🔄 Recarga el usuario actualizado
                                tabNavController.popBackStack() // ⬅️ Regresa al perfil
                            }
                        }
                    }
                )
            }
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
            val placeId = backStackEntry.arguments?.getString("placeId")
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

        composable<UserRouteTab.SearchPlaces> {
            SearchPlacesScreen(
                allPlaces = mockPlaces,
                onPlaceClick = { place ->
                    tabNavController.navigate(UserRouteTab.PlaceDetail(place.id))
                }
            )
        }

        composable<UserRouteTab.PlaceComments> { backStackEntry ->
            val placeId = backStackEntry.arguments?.getString("placeId")
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
            val placeId = backStackEntry.arguments?.getString("placeId")
                ?: backStackEntry.destination.arguments["placeId"]?.defaultValue as? String
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