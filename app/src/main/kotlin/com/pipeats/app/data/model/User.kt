package com.pipeats.app.data.model

data class User(
    val id: String,
    val name: String,
    val phone: String,
    val email: String = "",
    val addresses: List<Address> = emptyList(),
    val defaultAddressId: String? = null
) {
    val defaultAddress: Address?
        get() = addresses.find { it.id == defaultAddressId }
}
