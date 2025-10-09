package com.example.appubicaciones.ui.screens.user.nav

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appubicaciones.config.RouteScreen
import com.example.appubicaciones.ui.screens.LoginScreen
import com.example.appubicaciones.ui.screens.user.tabs.CreatePlaceScreen
import com.example.appubicaciones.ui.screens.user.tabs.EditUserProfileScreen
import com.example.appubicaciones.ui.screens.user.tabs.MapScreen
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
                    onRecoverPasswordClick = { rootNavController.navigate(RouteScreen.RecoverPassword) }
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