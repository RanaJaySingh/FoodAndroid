package com.pipeats.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pipeats.app.data.local.DataStoreManager
import com.pipeats.app.ui.screens.*

@Composable
fun PiPEatsNavGraph(
    navController: NavHostController,
    dataStoreManager: DataStoreManager
) {
    val hasCompletedOnboarding by dataStoreManager.hasCompletedOnboarding.collectAsState(initial = null)
    
    val startDestination = when (hasCompletedOnboarding) {
        null -> NavRoutes.Splash.route
        true -> NavRoutes.Home.route
        false -> NavRoutes.Splash.route
    }
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavRoutes.Splash.route) {
            SplashScreen(
                dataStoreManager = dataStoreManager,
                onNavigateToOnboarding = {
                    navController.navigate(NavRoutes.Onboarding.route) {
                        popUpTo(NavRoutes.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(NavRoutes.Onboarding.route) {
            OnboardingScreen(
                dataStoreManager = dataStoreManager,
                onOnboardingComplete = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(NavRoutes.Home.route) {
            HomeScreen(
                dataStoreManager = dataStoreManager,
                onRestaurantClick = { restaurantId ->
                    navController.navigate(NavRoutes.Restaurant.createRoute(restaurantId))
                },
                onCartClick = {
                    navController.navigate(NavRoutes.Cart.route)
                },
                onProfileClick = {
                    navController.navigate(NavRoutes.Profile.route)
                },
                onOrderHistoryClick = {
                    navController.navigate(NavRoutes.OrderHistory.route)
                }
            )
        }
        
        composable(
            route = NavRoutes.Restaurant.route,
            arguments = listOf(navArgument("restaurantId") { type = NavType.StringType })
        ) { backStackEntry ->
            val restaurantId = backStackEntry.arguments?.getString("restaurantId") ?: ""
            RestaurantDetailScreen(
                restaurantId = restaurantId,
                dataStoreManager = dataStoreManager,
                onBackClick = { navController.popBackStack() },
                onViewCartClick = { navController.navigate(NavRoutes.Cart.route) }
            )
        }
        
        composable(NavRoutes.Cart.route) {
            CartScreen(
                dataStoreManager = dataStoreManager,
                onBackClick = { navController.popBackStack() },
                onCheckoutClick = { navController.navigate(NavRoutes.Checkout.route) },
                onBrowseClick = { 
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Home.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(NavRoutes.Checkout.route) {
            CheckoutScreen(
                dataStoreManager = dataStoreManager,
                onBackClick = { navController.popBackStack() },
                onOrderPlaced = { orderId ->
                    navController.navigate(NavRoutes.OrderConfirmation.createRoute(orderId)) {
                        popUpTo(NavRoutes.Home.route)
                    }
                }
            )
        }
        
        composable(
            route = NavRoutes.OrderConfirmation.route,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderConfirmationScreen(
                orderId = orderId,
                dataStoreManager = dataStoreManager,
                onTrackOrderClick = {
                    navController.navigate(NavRoutes.OrderTracking.createRoute(orderId)) {
                        popUpTo(NavRoutes.OrderConfirmation.route) { inclusive = true }
                    }
                },
                onBackToHomeClick = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Home.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(
            route = NavRoutes.OrderTracking.route,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderTrackingScreen(
                orderId = orderId,
                dataStoreManager = dataStoreManager,
                onBackClick = { navController.popBackStack() },
                onBackToHomeClick = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Home.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(NavRoutes.OrderHistory.route) {
            OrderHistoryScreen(
                dataStoreManager = dataStoreManager,
                onBackClick = { navController.popBackStack() },
                onOrderClick = { orderId ->
                    navController.navigate(NavRoutes.OrderDetail.createRoute(orderId))
                }
            )
        }
        
        composable(
            route = NavRoutes.OrderDetail.route,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderDetailScreen(
                orderId = orderId,
                dataStoreManager = dataStoreManager,
                onBackClick = { navController.popBackStack() },
                onTrackOrderClick = {
                    navController.navigate(NavRoutes.OrderTracking.createRoute(orderId))
                }
            )
        }
        
        composable(NavRoutes.Profile.route) {
            ProfileScreen(
                dataStoreManager = dataStoreManager,
                onBackClick = { navController.popBackStack() },
                onEditProfileClick = { navController.navigate(NavRoutes.EditProfile.route) },
                onAddAddressClick = { navController.navigate(NavRoutes.AddAddress.route) },
                onEditAddressClick = { addressId ->
                    navController.navigate(NavRoutes.EditAddress.createRoute(addressId))
                },
                onLogoutComplete = {
                    navController.navigate(NavRoutes.Onboarding.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        composable(NavRoutes.EditProfile.route) {
            EditProfileScreen(
                dataStoreManager = dataStoreManager,
                onBackClick = { navController.popBackStack() },
                onSaveComplete = { navController.popBackStack() }
            )
        }
        
        composable(NavRoutes.AddAddress.route) {
            AddAddressScreen(
                dataStoreManager = dataStoreManager,
                onBackClick = { navController.popBackStack() },
                onSaveComplete = { navController.popBackStack() }
            )
        }
        
        composable(
            route = NavRoutes.EditAddress.route,
            arguments = listOf(navArgument("addressId") { type = NavType.StringType })
        ) { backStackEntry ->
            val addressId = backStackEntry.arguments?.getString("addressId") ?: ""
            EditAddressScreen(
                addressId = addressId,
                dataStoreManager = dataStoreManager,
                onBackClick = { navController.popBackStack() },
                onSaveComplete = { navController.popBackStack() }
            )
        }
    }
}
