package com.pipeats.app.data.model

data class Cart(
    val restaurantId: String? = null,
    val restaurantName: String = "",
    val items: List<CartLine> = emptyList()
) {
    val itemCount: Int
        get() = items.sumOf { it.quantity }
    
    val subtotal: Double
        get() = items.sumOf { it.totalPrice }
    
    val deliveryFee: Double
        get() = if (items.isEmpty()) 0.0 else 40.0
    
    val taxes: Double
        get() = subtotal * 0.05
    
    val total: Double
        get() = subtotal + deliveryFee + taxes
    
    val formattedSubtotal: String
        get() = "₹${subtotal.toInt()}"
    
    val formattedDeliveryFee: String
        get() = "₹${deliveryFee.toInt()}"
    
    val formattedTaxes: String
        get() = "₹${taxes.toInt()}"
    
    val formattedTotal: String
        get() = "₹${total.toInt()}"
    
    val isEmpty: Boolean
        get() = items.isEmpty()
}

data class CartLine(
    val menuItemId: String,
    val name: String,
    val price: Double,
    val quantity: Int,
    val isVegetarian: Boolean = true
) {
    val totalPrice: Double
        get() = price * quantity
    
    val formattedPrice: String
        get() = "₹${price.toInt()}"
    
    val formattedTotalPrice: String
        get() = "₹${totalPrice.toInt()}"
}
