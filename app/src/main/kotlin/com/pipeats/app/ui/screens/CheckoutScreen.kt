package com.pipeats.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pipeats.app.data.local.DataStoreManager
import com.pipeats.app.data.model.*
import com.pipeats.app.ui.components.*
import com.pipeats.app.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    dataStoreManager: DataStoreManager,
    onBackClick: () -> Unit,
    onOrderPlaced: (String) -> Unit
) {
    val cart by dataStoreManager.cart.collectAsState(initial = Cart())
    val user by dataStoreManager.user.collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    
    var selectedAddress by remember { mutableStateOf<Address?>(null) }
    var phone by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    var showAddressSelector by remember { mutableStateOf(false) }
    
    LaunchedEffect(user) {
        user?.let {
            selectedAddress = it.defaultAddress ?: it.addresses.firstOrNull()
            phone = it.phone
        }
    }
    
    val isFormValid = selectedAddress != null && phone.isNotBlank() && phone.length >= 10
    
    Scaffold(
        topBar = {
            PiPEatsTopBar(
                title = "Checkout",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            CheckoutBottomBar(
                total = cart.formattedTotal,
                isProcessing = isProcessing,
                isEnabled = isFormValid && !isProcessing,
                onPlaceOrderClick = {
                    scope.launch {
                        isProcessing = true
                        delay(1500)
                        
                        val orderId = "PIP${UUID.randomUUID().toString().take(8).uppercase()}"
                        val order = Order.fromCart(
                            orderId = orderId,
                            cart = cart,
                            deliveryAddress = selectedAddress!!,
                            phone = phone
                        )
                        
                        dataStoreManager.addOrder(order)
                        dataStoreManager.clearCart()
                        
                        isProcessing = false
                        onOrderPlaced(orderId)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                color = SurfaceVariant
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Delivery Address",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        
                        if ((user?.addresses?.size ?: 0) > 1) {
                            TextButton(onClick = { showAddressSelector = true }) {
                                Text("Change", color = Primary)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    if (selectedAddress != null) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = when (selectedAddress?.label) {
                                    "Home" -> Icons.Default.Home
                                    "Work" -> Icons.Default.Work
                                    else -> Icons.Default.LocationOn
                                },
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = selectedAddress?.label ?: "",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = selectedAddress?.fullAddress ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = OnSurfaceVariant
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "No address available. Please add an address in your profile.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Error
                        )
                    }
                }
            }
            
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                color = SurfaceVariant
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Contact Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { 
                            if (it.length <= 15 && it.all { c -> c.isDigit() || c == '+' || c == ' ' }) {
                                phone = it
                            }
                        },
                        label = { Text("Phone Number") },
                        placeholder = { Text("+91 XXXXX XXXXX") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            focusedLabelColor = Primary,
                            unfocusedContainerColor = Background,
                            focusedContainerColor = Background
                        ),
                        singleLine = true,
                        isError = phone.isNotBlank() && phone.length < 10
                    )
                    
                    if (phone.isNotBlank() && phone.length < 10) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Please enter a valid phone number",
                            style = MaterialTheme.typography.bodySmall,
                            color = Error
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                color = SurfaceVariant
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Payment",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Money,
                            contentDescription = null,
                            tint = Primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Cash on Delivery",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Pay when your order arrives",
                                style = MaterialTheme.typography.bodySmall,
                                color = OnSurfaceVariant
                            )
                        }
                        RadioButton(
                            selected = true,
                            onClick = {},
                            colors = RadioButtonDefaults.colors(selectedColor = Primary)
                        )
                    }
                    
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = DividerColor
                    )
                    
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Warning.copy(alpha = 0.1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Warning,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "This is a mock payment. No actual transaction will occur.",
                                style = MaterialTheme.typography.bodySmall,
                                color = OnSurface
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                color = SurfaceVariant
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Order Summary",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Restaurant,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = cart.restaurantName,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    cart.items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${item.quantity}x ${item.name}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = OnSurfaceVariant
                            )
                            Text(
                                text = item.formattedTotalPrice,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = DividerColor
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = cart.formattedTotal,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
    
    if (showAddressSelector) {
        AddressSelectorDialog(
            addresses = user?.addresses ?: emptyList(),
            selectedAddressId = selectedAddress?.id,
            onAddressSelected = { address ->
                selectedAddress = address
                showAddressSelector = false
            },
            onDismiss = { showAddressSelector = false }
        )
    }
}

@Composable
private fun AddressSelectorDialog(
    addresses: List<Address>,
    selectedAddressId: String?,
    onAddressSelected: (Address) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Select Delivery Address",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column {
                addresses.forEach { address ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAddressSelected(address) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = address.id == selectedAddressId,
                            onClick = { onAddressSelected(address) },
                            colors = RadioButtonDefaults.colors(selectedColor = Primary)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = address.label,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = address.shortAddress,
                                style = MaterialTheme.typography.bodySmall,
                                color = OnSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun CheckoutBottomBar(
    total: String,
    isProcessing: Boolean,
    isEnabled: Boolean,
    onPlaceOrderClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            PrimaryButton(
                text = if (isProcessing) "Processing..." else "Place Order • $total",
                onClick = onPlaceOrderClick,
                enabled = isEnabled,
                isLoading = isProcessing
            )
        }
    }
}
