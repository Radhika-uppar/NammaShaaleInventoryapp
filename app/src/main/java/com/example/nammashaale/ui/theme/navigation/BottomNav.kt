package com.example.nammashaale.ui.theme.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.nammashaale.ui.theme.BrandBlue

@Composable
fun BottomNav(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        contentColor = BrandBlue
    ) {
        NavigationBarItem(
            selected = currentRoute == "dashboard",
            onClick = { 
                if (currentRoute != "dashboard") {
                    navController.navigate("dashboard") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            },
            label = { Text("Dashboard") },
            icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandBlue,
                selectedTextColor = BrandBlue,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = currentRoute == "all_assets",
            onClick = { 
                if (currentRoute != "all_assets") {
                    navController.navigate("all_assets")
                }
            },
            label = { Text("Assets") },
            icon = { Icon(Icons.Default.Widgets, contentDescription = null) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandBlue,
                selectedTextColor = BrandBlue,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = currentRoute == "audit",
            onClick = { 
                if (currentRoute != "audit") {
                    navController.navigate("audit")
                }
            },
            label = { Text("Audit") },
            icon = { Icon(Icons.Default.Assignment, contentDescription = null) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandBlue,
                selectedTextColor = BrandBlue,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = currentRoute == "settings",
            onClick = { 
                if (currentRoute != "settings") {
                    navController.navigate("settings")
                }
            },
            label = { Text("Settings") },
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandBlue,
                selectedTextColor = BrandBlue,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
    }
}