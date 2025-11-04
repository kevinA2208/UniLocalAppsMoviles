package com.example.appubicaciones.ui.screens.user.nav

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appubicaciones.config.RouteScreen
import com.example.appubicaciones.ui.screens.LoginScreen
import com.example.appubicaciones.ui.screens.user.tabs.CreatePlaceScreen
import com.example.appubicaciones.ui.screens.user.tabs.EditUserProfileScreen
import com.example.appubicaciones.ui.screens.user.tabs.MapScreen
import com.example.appubicaciones.ui.screens.user.tabs.UserFavoritesScreen
import com.example.appubicaciones.ui.screens.user.tabs.UserProfileScreen
import com.example.appubicaciones.viewmodel.UserViewModel

@Composable
fun ContentUser(
    padding: PaddingValues,
    tabNavController: NavHostController,
    rootNavController: NavHostController,
    isLoggedIn: Boolean,
    onLoginSuccess: () -> Unit
) {

    val userViewModel: UserViewModel = viewModel()

    val isLoading by userViewModel.isLoading.collectAsState()
    val isSuccess by userViewModel.isSuccess.collectAsState()
    val errorMessage by userViewModel.errorMessage.collectAsState()

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
                    onEditClick = { tabNavController.navigate(UserRouteTab.EditProfile) }
                )
            } else {
                LoginScreen(
                    onRegisterClick = {
                        rootNavController.navigate(RouteScreen.Register)
                    },
                    onLoginClick = { email, password ->
                        userViewModel.loginUser(email, password)
                    },
                    isLoading = isLoading,
                    errorMessage = errorMessage
                )

                if (isSuccess) {
                    onLoginSuccess()
                    tabNavController.navigate(UserRouteTab.UserProfile)

                }

                errorMessage?.let {
                    Log.e("Login", "Error al iniciar sesión: $it")
                }
            }
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
    }
}