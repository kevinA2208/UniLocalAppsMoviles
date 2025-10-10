package com.example.appubicaciones.ui.screens.user.bottombar

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavHostController
import com.example.appubicaciones.R
import com.example.appubicaciones.ui.screens.user.nav.UserRouteTab

@SuppressLint("RestrictedApi")
@Composable
fun BottomBarUser(tabNavController: NavHostController) {
    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentDestination: NavDestination? = navBackStackEntry?.destination

    NavigationBar {
        Destination.entries.forEach { destination ->
            val isSelected: Boolean = currentDestination
                ?.hierarchy
                ?.any { dest ->
                    val currentRoute = dest.route?.substringBefore('?')
                    val tabRoute     = destination.route::class.qualifiedName
                    currentRoute == tabRoute
                } == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    tabNavController.navigate(destination.route) {
                        popUpTo(tabNavController.graph.startDestinationId) {
                            inclusive = false
                            saveState = false
                        }
                        launchSingleTop = true
                        restoreState = false
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                        contentDescription = null
                    )
                },
                alwaysShowLabel = false,
                label = { Text(text = androidx.compose.ui.res.stringResource(destination.label)) }
            )
        }
    }
}

enum class Destination(
    val route: UserRouteTab,
    val label: Int,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector
) {
    FAVORITES(
        UserRouteTab.Favorites,
        R.string.nav_favorites,
        Icons.Filled.Favorite,
        Icons.Outlined.FavoriteBorder
    ),
    HOME(
        UserRouteTab.Map,
        R.string.nav_home,
        Icons.Filled.LocationOn,
        Icons.Outlined.LocationOn
    ),
    PROFILE(
        UserRouteTab.UserProfile,
        R.string.nav_profile,
        Icons.Filled.Person,
        Icons.Outlined.Person
    );
}
