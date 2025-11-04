package com.example.appubicaciones.ui.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.example.appubicaciones.config.RouteScreen
import com.example.appubicaciones.ui.screens.user.HomeUserScreen
import com.example.appubicaciones.viewmodel.UserViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appubicaciones.ui.screens.user.tabs.RecoverPasswordScreen

@Composable
fun Navigation() {

    val navController = rememberNavController()
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = RouteScreen.Home(),
        modifier = Modifier
    ) {
        composable<RouteScreen.Login> {

            val userViewModel: UserViewModel = viewModel()

            // Estados del ViewModel
            val isLoading by userViewModel.isLoading.collectAsState()
            val isSuccess by userViewModel.isSuccess.collectAsState()
            val errorMessage by userViewModel.errorMessage.collectAsState()

            LaunchedEffect(isSuccess) {
                if (isSuccess) {
                    isLoggedIn = true
                    navController.navigate(RouteScreen.Home) {
                        popUpTo(RouteScreen.Login) { inclusive = true }
                    }
                }
            }


            LoginScreen(
                onRegisterClick = {
                    navController.navigate(RouteScreen.Register)
                },
                onLoginClick = { email, password ->
                    userViewModel.loginUser(email, password)
                },
                isLoading = isLoading,
                errorMessage = errorMessage,
                onRecoverPasswordClick = {
                    navController.navigate(RouteScreen.RecoverPassword)
                }
            )

            if (errorMessage != null) {
                Log.e("Login", "Error al iniciar sesión: $errorMessage")
            }
        }

        composable<RouteScreen.Register> {
            val userViewModel: UserViewModel = viewModel()

            RegisterScreen(
                viewModel = userViewModel,
                onLoginClick = {
                    navController.popBackStack()
                },
                onRegisterClick = { _, _, _, _, _, _ ->
                    navController.navigate(RouteScreen.Home(openCreate = false)) {
                        popUpTo(RouteScreen.Register) { inclusive = true }
                    }
                }
            )
        }

        composable<RouteScreen.Home> { backStackEntry ->
            val args = backStackEntry.toRoute<RouteScreen.Home>()
            HomeUserScreen(
                isLoggedIn = isLoggedIn,
                onLoginSuccess = { isLoggedIn = true },
                rootNavController = navController,
                openCreate = args.openCreate
            )
        }



        composable<RouteScreen.RecoverPassword> {
            RecoverPasswordScreen(
                onSubmitClick = { newPassword, otp ->
                    navController.popBackStack()
                },
                onResendClick = {
                    // Lógica para reenviar OTP
                }
            )
        }
    }
}
