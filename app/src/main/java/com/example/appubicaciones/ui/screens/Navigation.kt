package com.example.appubicaciones.ui.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.appubicaciones.config.RouteScreen
import com.example.appubicaciones.ui.screens.user.HomeUserScreen

@Composable
fun Navigation() {

    val navController = rememberNavController()
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = RouteScreen.Home,
        modifier = Modifier
    ) {
        composable<RouteScreen.Login> {
            LoginScreen(
                onRegisterClick = {
                    navController.navigate(RouteScreen.Register)
                },
                onLoginClick = { email, password ->
                    if (email == "admin@gmail.com" && password == "12345") {
                        isLoggedIn = true
                        navController.navigate(RouteScreen.Home) {
                            popUpTo(RouteScreen.Login) { inclusive = true }
                        }
                    } else {
                        Log.d("LoginScreen", "Credenciales incorrectas")
                    }
                }
            )
        }

        composable<RouteScreen.Register> {
            RegisterScreen(
                onLoginClick = {
                    navController.popBackStack()
                },
                onRegisterClick = { names, lastNames, username, email, city, password ->
                    Log.d("RegisterScreen", "Registrando usuario: $names $lastNames, $username, $email, $city")
                    navController.navigate(RouteScreen.Home) {
                        popUpTo(RouteScreen.Register) { inclusive = true }
                    }
                }
            )
        }

        composable<RouteScreen.Home> {
            HomeUserScreen(
                isLoggedIn = isLoggedIn,
                onLoginSuccess = { isLoggedIn = true },
                rootNavController = navController
            )
        }
    }
}
