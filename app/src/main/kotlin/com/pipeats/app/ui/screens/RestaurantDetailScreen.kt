package com.pipeats.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pipeats.app.data.local.DataStoreManager
import com.pipeats.app.data.mock.MockData
import com.pipeats.app.data.model.*
import com.pipeats.app.ui.components.*
import com.pipeats.app.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(
    restaurantId: String,
    dataStoreManager: DataStoreManager,
    onBackClick: () -> Unit,
    onViewCartClick: () -> Unit
) {
    val restaurant = remember { MockData.getRestaurantById(restaurantId) }
    val menuCategories = remember { MockData.getMenuCategoriesForRestaurant(restaurantId) }
    val cart by dataStoreManager.cart.collectAsState(initial = Cart())
    val scope = rememberCoroutineScope()
    
    if (restaurant == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Restaurant not found")
        }
        return
    }
    
    Scaffold(
        bottomBar = {
            if (!cart.isEmpty) {
                ViewCartBar(
                    itemCount = cart.itemCount,
                    total = cart.formattedTotal,
                    onClick = onViewCartClick
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
        ) {
            item {
                RestaurantHeader(
                    restaurant = restaurant,
                    onBackClick = onBackClick
                )
            }
            
            menuCategories.forEach { category ->
                item {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
                
                items(category.items) { menuItem ->
                    MenuItemRow(
                        menuItem = menuItem,
                        quantity = cart.items.find { it.menuItemId == menuItem.id }?.quantity ?: 0,
                        onAddClick = {
                            scope.launch {
                                val updatedCart = addItemToCart(cart, menuItem, restaurant)
                                dataStoreManager.updateCart(updatedCart)
                            }
                        },
                        onIncrementClick = {
                            scope.launch {
                                val updatedCart = incrementItemInCart(cart, menuItem.id)
                                dataStoreManager.updateCart(updatedCart)
                            }
                        },
                        onDecrementClick = {
                            scope.launch {
                                val updatedCart = decrementItemInCart(cart, menuItem.id)
                                dataStoreManager.updateCart(updatedCart)
                            }
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = DividerColor
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
private fun RestaurantHeader(
    restaurant: Restaurant,
    onBackClick: () -> Unit
) {
    Box {
        AsyncImage(
            model = restaurant.imageUrl,
            contentDescription = restaurant.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.4f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.6f)
                        )
                    )
                )
        )
        
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.White.copy(alpha = 0.9f))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
    }
    
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = restaurant.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = restaurant.cuisine,
            style = MaterialTheme.typography.bodyLarge,
            color = OnSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Success
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = restaurant.formattedRating,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = OnPrimary
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = OnPrimary
                        )
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "(${restaurant.ratingCount})",
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceVariant
                )
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = OnSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = restaurant.formattedDeliveryTime,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Text(
                text = restaurant.formattedDeliveryFee,
                style = MaterialTheme.typography.bodyMedium,
                color = if (restaurant.deliveryFee == 0.0) Success else OnSurface
            )
        }
        
        if (restaurant.minimumOrder > 0) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Minimum order: ₹${restaurant.minimumOrder.toInt()}",
                style = MaterialTheme.typography.bodySmall,
                color = OnSurfaceVariant
            )
        }
    }
    
    HorizontalDivider(thickness = 8.dp, color = SurfaceVariant)
}

@Composable
private fun MenuItemRow(
    menuItem: MenuItem,
    quantity: Int,
    onAddClick: () -> Unit,
    onIncrementClick: () -> Unit,
    onDecrementClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                VegNonVegIndicator(isVegetarian = menuItem.isVegetarian)
                
                if (menuItem.isPopular) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = PrimaryLight
                    ) {
                        Text(
                            text = "BESTSELLER",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = Primary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = menuItem.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (menuItem.isAvailable) OnSurface else DisabledColor
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = menuItem.formattedPrice,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (menuItem.isAvailable) OnSurface else DisabledColor
            )
            
            if (menuItem.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = menuItem.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            if (!menuItem.isAvailable) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Currently unavailable",
                    style = MaterialTheme.typography.bodySmall,
                    color = Error,
                    textDecoration = TextDecoration.LineThrough
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (menuItem.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = menuItem.imageUrl,
                    contentDescription = menuItem.name,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            if (menuItem.isAvailable) {
                if (quantity > 0) {
                    QuantityStepper(
                        quantity = quantity,
                        onIncrement = onIncrementClick,
                        onDecrement = onDecrementClick
                    )
                } else {
                    AddButton(onClick = onAddClick)
                }
            }
        }
    }
}

@Composable
private fun ViewCartBar(
    itemCount: Int,
    total: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = OnPrimary.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = itemCount.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = OnPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "items",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnPrimary
                    )
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "View Cart",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = OnPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = total,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = OnPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = OnPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

private fun addItemToCart(cart: Cart, menuItem: MenuItem, restaurant: Restaurant): Cart {
    if (cart.restaurantId != null && cart.restaurantId != restaurant.id && cart.items.isNotEmpty()) {
        return Cart(
            restaurantId = restaurant.id,
            restaurantName = restaurant.name,
            items = listOf(
                CartLine(
                    menuItemId = menuItem.id,
                    name = menuItem.name,
                    price = menuItem.price,
                    quantity = 1,
                    isVegetarian = menuItem.isVegetarian
                )
            )
        )
    }
    
    val existingItem = cart.items.find { it.menuItemId == menuItem.id }
    val updatedItems = if (existingItem != null) {
        cart.items.map {
            if (it.menuItemId == menuItem.id) it.copy(quantity = it.quantity + 1)
            else it
        }
    } else {
        cart.items + CartLine(
            menuItemId = menuItem.id,
            name = menuItem.name,
            price = menuItem.price,
            quantity = 1,
            isVegetarian = menuItem.isVegetarian
        )
    }
    
    return cart.copy(
        restaurantId = restaurant.id,
        restaurantName = restaurant.name,
        items = updatedItems
    )
}

private fun incrementItemInCart(cart: Cart, menuItemId: String): Cart {
    return cart.copy(
        items = cart.items.map {
            if (it.menuItemId == menuItemId) it.copy(quantity = it.quantity + 1)
            else it
        }
    )
}

private fun decrementItemInCart(cart: Cart, menuItemId: String): Cart {
    val updatedItems = cart.items.mapNotNull {
        if (it.menuItemId == menuItemId) {
            if (it.quantity > 1) it.copy(quantity = it.quantity - 1)
            else null
        } else it
    }
    
    return if (updatedItems.isEmpty()) {
        Cart()
    } else {
        cart.copy(items = updatedItems)
    }
}
