package com.pipeats.app.data.model

data class MenuItem(
    val id: String,
    val restaurantId: String,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrl: String = "",
    val isVegetarian: Boolean = true,
    val isAvailable: Boolean = true,
    val isPopular: Boolean = false
) {
    val formattedPrice: String
        get() = "₹${price.toInt()}"
}

data class MenuCategory(
    val name: String,
    val items: List<MenuItem>
)
