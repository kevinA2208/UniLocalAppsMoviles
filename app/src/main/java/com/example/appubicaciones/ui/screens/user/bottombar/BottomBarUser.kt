package com.example.appubicaciones.ui.screens.user.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.appubicaciones.R
import com.example.appubicaciones.ui.screens.user.nav.UserRouteTab

@Composable
fun BottomBarUser(
    tabNavController: NavHostController
){

    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        Destination.entries.forEachIndexed { index, destination ->

            val isSelected = currentDestination?.route == destination.route::class.qualifiedName

            // Profile
            NavigationBarItem(
                label = {
                    Text(text = stringResource(destination.label)
                    )
                },
                onClick = {
                    tabNavController.navigate(destination.route)
                },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = stringResource(destination.label)
                    )
                },
                selected = isSelected
            )
        }
    }
}

enum class Destination(
    val route: UserRouteTab,
    val label: Int,
    val icon: ImageVector
){
    HOME(UserRouteTab.Map, R.string.nav_home, Icons.Default.Home),
    FAVORITES(UserRouteTab.Favorites, R.string.nav_favorites, Icons.Default.Favorite),
    PROFILE(UserRouteTab.UserProfile, R.string.nav_profile, Icons.Default.AccountCircle)
}