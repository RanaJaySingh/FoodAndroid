package com.pipeats.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pipeats.app.data.local.DataStoreManager
import com.pipeats.app.data.model.Cart
import com.pipeats.app.data.model.CartLine
import com.pipeats.app.ui.components.*
import com.pipeats.app.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    dataStoreManager: DataStoreManager,
    onBackClick: () -> Unit,
    onCheckoutClick: () -> Unit,
    onBrowseClick: () -> Unit
) {
    val cart by dataStoreManager.cart.collectAsState(initial = Cart())
    val scope = rememberCoroutineScope()
    
    Scaffold(
        topBar = {
            PiPEatsTopBar(
                title = "Your Cart",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            if (!cart.isEmpty) {
                CartBottomBar(
                    total = cart.formattedTotal,
                    onCheckoutClick = onCheckoutClick
                )
            }
        }
    ) { paddingValues ->
        if (cart.isEmpty) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                EmptyStateView(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = OnSurfaceVariant.copy(alpha = 0.5f)
                        )
                    },
                    title = "Your cart is empty",
                    subtitle = "Add items from restaurants to get started",
                    actionText = "Browse Restaurants",
                    onActionClick = onBrowseClick
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Background)
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceVariant
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = null,
                                tint = Primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = cart.restaurantName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "${cart.itemCount} items",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = OnSurfaceVariant
                                )
                            }
                        }
                    }
                }
                
                items(cart.items) { item ->
                    CartItemRow(
                        item = item,
                        onIncrementClick = {
                            scope.launch {
                                val updatedCart = cart.copy(
                                    items = cart.items.map {
                                        if (it.menuItemId == item.menuItemId) 
                                            it.copy(quantity = it.quantity + 1)
                                        else it
                                    }
                                )
                                dataStoreManager.updateCart(updatedCart)
                            }
                        },
                        onDecrementClick = {
                            scope.launch {
                                val updatedItems = cart.items.mapNotNull {
                                    if (it.menuItemId == item.menuItemId) {
                                        if (it.quantity > 1) it.copy(quantity = it.quantity - 1)
                                        else null
                                    } else it
                                }
                                if (updatedItems.isEmpty()) {
                                    dataStoreManager.clearCart()
                                } else {
                                    dataStoreManager.updateCart(cart.copy(items = updatedItems))
                                }
                            }
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = DividerColor
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    BillDetails(cart = cart)
                }
            }
        }
    }
}

@Composable
private fun CartItemRow(
    item: CartLine,
    onIncrementClick: () -> Unit,
    onDecrementClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        VegNonVegIndicator(isVegetarian = item.isVegetarian)
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = item.formattedPrice,
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceVariant
            )
        }
        
        QuantityStepper(
            quantity = item.quantity,
            onIncrement = onIncrementClick,
            onDecrement = onDecrementClick
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = item.formattedTotalPrice,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun BillDetails(cart: Cart) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        color = SurfaceVariant
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Bill Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            BillRow(label = "Item Total", value = cart.formattedSubtotal)
            BillRow(label = "Delivery Fee", value = cart.formattedDeliveryFee)
            BillRow(label = "Taxes & Charges", value = cart.formattedTaxes)
            
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = DividerColor
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "To Pay",
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
}

@Composable
private fun BillRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun CartBottomBar(
    total: String,
    onCheckoutClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            PrimaryButton(
                text = "Proceed to Checkout • $total",
                onClick = onCheckoutClick
            )
        }
    }
}
