package com.pipeats.app.ui.navigation

sealed class NavRoutes(val route: String) {
    object Splash : NavRoutes("splash")
    object Onboarding : NavRoutes("onboarding")
    object Home : NavRoutes("home")
    object Restaurant : NavRoutes("restaurant/{restaurantId}") {
        fun createRoute(restaurantId: String) = "restaurant/$restaurantId"
    }
    object Cart : NavRoutes("cart")
    object Checkout : NavRoutes("checkout")
    object OrderConfirmation : NavRoutes("order_confirmation/{orderId}") {
        fun createRoute(orderId: String) = "order_confirmation/$orderId"
    }
    object OrderTracking : NavRoutes("order_tracking/{orderId}") {
        fun createRoute(orderId: String) = "order_tracking/$orderId"
    }
    object OrderHistory : NavRoutes("order_history")
    object OrderDetail : NavRoutes("order_detail/{orderId}") {
        fun createRoute(orderId: String) = "order_detail/$orderId"
    }
    object Profile : NavRoutes("profile")
    object EditProfile : NavRoutes("edit_profile")
    object AddAddress : NavRoutes("add_address")
    object EditAddress : NavRoutes("edit_address/{addressId}") {
        fun createRoute(addressId: String) = "edit_address/$addressId"
    }
}
