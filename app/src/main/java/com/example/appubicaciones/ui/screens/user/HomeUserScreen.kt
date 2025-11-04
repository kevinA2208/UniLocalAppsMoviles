@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.appubicaciones.ui.screens.user

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.appubicaciones.ui.screens.user.bottombar.BottomBarUser
import com.example.appubicaciones.ui.screens.user.nav.ContentUser
import com.example.appubicaciones.ui.screens.user.topbar.TopBarUser


@Composable
fun HomeUserScreen(
    isLoggedIn: Boolean,
    onLoginSuccess: () -> Unit,
    rootNavController: NavHostController,
    openCreate: Boolean = false
){

    val tabNavController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarUser()
        },
        bottomBar = {
            BottomBarUser(
                tabNavController = tabNavController
            )
        }
    ) { padding ->
        ContentUser(
            padding = padding,
            tabNavController = tabNavController,
            rootNavController = rootNavController,
            isLoggedIn = isLoggedIn,
            onLoginSuccess = onLoginSuccess,
            openCreate = openCreate
        )
    }
}

