package com.pipeats.app.data.model

data class Restaurant(
    val id: String,
    val name: String,
    val cuisine: String,
    val rating: Float,
    val ratingCount: Int,
    val deliveryTimeMinutes: Int,
    val deliveryFee: Double,
    val minimumOrder: Double = 0.0,
    val imageUrl: String,
    val isOpen: Boolean = true,
    val address: String = "",
    val categories: List<String> = emptyList()
) {
    val formattedDeliveryTime: String
        get() = "$deliveryTimeMinutes-${deliveryTimeMinutes + 10} min"
    
    val formattedDeliveryFee: String
        get() = if (deliveryFee == 0.0) "Free delivery" else "₹${deliveryFee.toInt()} delivery"
    
    val formattedRating: String
        get() = "%.1f".format(rating)
}
