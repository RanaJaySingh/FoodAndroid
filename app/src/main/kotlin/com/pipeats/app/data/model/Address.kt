package com.pipeats.app.data.model

data class Address(
    val id: String,
    val label: String,
    val addressLine1: String,
    val addressLine2: String = "",
    val city: String,
    val state: String,
    val pincode: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) {
    val fullAddress: String
        get() = buildString {
            append(addressLine1)
            if (addressLine2.isNotBlank()) {
                append(", ")
                append(addressLine2)
            }
            append(", ")
            append(city)
            append(", ")
            append(state)
            append(" - ")
            append(pincode)
        }
    
    val shortAddress: String
        get() = "$addressLine1, $city"
}
