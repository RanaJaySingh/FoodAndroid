package com.pipeats.app.data.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class OrderStatus {
    PLACED,
    CONFIRMED,
    PREPARING,
    ON_THE_WAY,
    DELIVERED,
    CANCELLED;
    
    val displayName: String
        get() = when (this) {
            PLACED -> "Order Placed"
            CONFIRMED -> "Confirmed"
            PREPARING -> "Preparing"
            ON_THE_WAY -> "On the Way"
            DELIVERED -> "Delivered"
            CANCELLED -> "Cancelled"
        }
    
    val isActive: Boolean
        get() = this in listOf(PLACED, CONFIRMED, PREPARING, ON_THE_WAY)
}

data class Order(
    val id: String,
    val restaurantId: String,
    val restaurantName: String,
    val items: List<CartLine>,
    val subtotal: Double,
    val deliveryFee: Double,
    val taxes: Double,
    val total: Double,
    val status: OrderStatus,
    val deliveryAddress: Address,
    val phone: String,
    val createdAt: String,
    val estimatedDeliveryMinutes: Int = 35
) {
    val formattedTotal: String
        get() = "₹${total.toInt()}"
    
    val formattedDate: String
        get() = try {
            val dateTime = LocalDateTime.parse(createdAt)
            dateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
        } catch (e: Exception) {
            createdAt
        }
    
    val itemCount: Int
        get() = items.sumOf { it.quantity }
    
    val orderSummary: String
        get() = "${items.size} items • $formattedTotal"
    
    companion object {
        fun fromCart(
            orderId: String,
            cart: Cart,
            deliveryAddress: Address,
            phone: String
        ): Order {
            return Order(
                id = orderId,
                restaurantId = cart.restaurantId ?: "",
                restaurantName = cart.restaurantName,
                items = cart.items,
                subtotal = cart.subtotal,
                deliveryFee = cart.deliveryFee,
                taxes = cart.taxes,
                total = cart.total,
                status = OrderStatus.PLACED,
                deliveryAddress = deliveryAddress,
                phone = phone,
                createdAt = LocalDateTime.now().toString()
            )
        }
    }
}
