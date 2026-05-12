package com.example.nammashaale

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nammashaale.ui.theme.BrandBlue
import com.example.nammashaale.ui.theme.NammaShaaleInventoryTheme
import com.example.nammashaale.ui.theme.navigation.BottomNav
import com.example.nammashaale.ui.theme.screen.*
import com.example.nammashaale.viewmodel.AssetViewModel
import com.example.nammashaale.viewmodel.AuthViewModel
import com.example.nammashaale.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val authViewModel: AuthViewModel = hiltViewModel()
            val userViewModel: UserViewModel = hiltViewModel()
            val assetViewModel: AssetViewModel = hiltViewModel()
            
            // Collect the auth state to determine the start destination
            val currentUser by authViewModel.currentUser.collectAsState()
            
            NammaShaaleInventoryTheme(darkTheme = userViewModel.isDarkMode) {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                
                val mainRoutes = listOf("dashboard", "all_assets", "audit", "settings")
                val showBars = currentRoute in mainRoutes

                Scaffold(
                    bottomBar = {
                        if (showBars) {
                            BottomNav(navController)
                        }
                    },
                    floatingActionButton = {
                        if (showBars) {
                            FloatingActionButton(
                                onClick = { navController.navigate("add_asset") },
                                containerColor = BrandBlue,
                                contentColor = Color.White,
                                shape = CircleShape
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add Asset")
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                        NavHost(
                            navController = navController, 
                            // Automatically skip login if user is already authenticated
                            startDestination = if (currentUser != null) "dashboard" else "login"
                        ) {
                            composable("login") {
                                LoginScreen(
                                    authViewModel = authViewModel,
                                    onLoginSuccess = {
                                        navController.navigate("dashboard") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    },
                                    onCreateAccountClick = {
                                        navController.navigate("register")
                                    }
                                )
                            }

                            composable("register") {
                                RegisterScreen(
                                    authViewModel = authViewModel,
                                    userViewModel = userViewModel,
                                    onNavigateBack = { navController.popBackStack() },
                                    onRegisterSuccess = {
                                        navController.navigate("dashboard") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                )
                            }

                            composable("dashboard") {
                                DashboardScreen(onNavigate = { route -> navController.navigate(route) })
                            }

                            composable("audit") {
                                AuditScreen(onScanClick = { navController.navigate("scan_barcode") })
                            }

                            composable("settings") {
                                SettingsScreen(
                                    userViewModel = userViewModel,
                                    onLogout = {
                                        // 1. Sign out from Firebase
                                        authViewModel.logout()
                                        // 2. Clear local Room data
                                        assetViewModel.clearAllAssets()
                                        // 3. Reset navigation to Login
                                        navController.navigate("login") {
                                            popUpTo(navController.graph.id) {
                                                inclusive = true
                                            }
                                        }
                                    },
                                    onNavigateToHelpCenter = { navController.navigate("help_center") },
                                    onNavigateToAddAsset = { navController.navigate("add_asset") }
                                )
                            }

                            composable("add_asset") {
                                AddAssetScreen(
                                    userViewModel = userViewModel,
                                    onBackClick = { navController.popBackStack() }
                                )
                            }

                            composable(
                                "asset_details/{assetId}",
                                arguments = listOf(navArgument("assetId") { type = NavType.LongType })
                            ) { backStackEntry ->
                                val id = backStackEntry.arguments?.getLong("assetId")
                                AssetDetailScreen(
                                    assetId = id,
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            }

                            composable("all_assets") {
                                AllAssetsScreen(
                                    onNavigateBack = { navController.popBackStack() },
                                    onAssetClick = { id -> navController.navigate("asset_details/$id") }
                                )
                            }

                            composable("report") {
                                SummaryReportDialog(onDismiss = { navController.popBackStack() })
                            }
                            
                            composable("scan_barcode") {
                                ScanBarcodeScreen(onScanResult = { serial ->
                                    navController.popBackStack()
                                    navController.navigate("asset_details_by_serial/$serial")
                                }, onBack = { navController.popBackStack() })
                            }

                            composable("asset_details_by_serial/{serial}") { backStackEntry ->
                                val serial = backStackEntry.arguments?.getString("serial")
                                AssetDetailScreen(
                                    assetId = null,
                                    serialNumber = serial,
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            }
                            
                            composable("help_center") {
                                HelpCenterScreen(onBack = { navController.popBackStack() })
                            }
                        }
                    }
                }
            }
        }
    }
}
