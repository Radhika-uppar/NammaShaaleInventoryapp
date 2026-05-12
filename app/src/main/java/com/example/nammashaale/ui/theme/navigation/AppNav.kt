package com.example.nammashaale.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nammashaale.ui.theme.screen.AddAssetScreen
import com.example.nammashaale.ui.theme.screen.DashboardScreen
import com.example.nammashaale.viewmodel.UserViewModel

@Composable
fun AppNav() {

    val navController = rememberNavController()
    val userViewModel: UserViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {

        composable("dashboard") {
            DashboardScreen(
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable("add_asset") {
            AddAssetScreen(
                userViewModel = userViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}