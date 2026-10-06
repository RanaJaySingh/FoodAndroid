package com.pipeats.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pipeats.app.data.local.DataStoreManager
import com.pipeats.app.data.mock.MockData
import com.pipeats.app.data.model.Cart
import com.pipeats.app.data.model.User
import com.pipeats.app.ui.components.*
import com.pipeats.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    dataStoreManager: DataStoreManager,
    onRestaurantClick: (String) -> Unit,
    onCartClick: () -> Unit,
    onProfileClick: () -> Unit,
    onOrderHistoryClick: () -> Unit
) {
    val user by dataStoreManager.user.collectAsState(initial = null)
    val cart by dataStoreManager.cart.collectAsState(initial = Cart())
    
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    
    val filteredRestaurants = remember(searchQuery, selectedCategory) {
        MockData.filterRestaurants(searchQuery, selectedCategory)
    }
    
    Scaffold(
        topBar = {
            HomeTopBar(
                user = user,
                onProfileClick = onProfileClick
            )
        },
        floatingActionButton = {
            if (!cart.isEmpty) {
                CartFab(
                    itemCount = cart.itemCount,
                    total = cart.formattedTotal,
                    onClick = onCartClick
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        placeholder = "Search restaurants or cuisines..."
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(MockData.categories) { category ->
                        CategoryChip(
                            name = category.name,
                            emoji = category.iconEmoji,
                            isSelected = selectedCategory == category.name,
                            onClick = { selectedCategory = category.name }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (searchQuery.isNotBlank() || selectedCategory != "All") 
                            "${filteredRestaurants.size} restaurants found" 
                        else 
                            "Restaurants near you",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    TextButton(onClick = onOrderHistoryClick) {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Orders")
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            if (filteredRestaurants.isEmpty()) {
                item {
                    EmptyStateView(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = OnSurfaceVariant
                            )
                        },
                        title = "No restaurants found",
                        subtitle = "Try adjusting your search or filters",
                        modifier = Modifier.padding(vertical = 48.dp)
                    )
                }
            } else {
                items(filteredRestaurants) { restaurant ->
                    RestaurantCard(
                        restaurant = restaurant,
                        onClick = { onRestaurantClick(restaurant.id) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(
    user: User?,
    onProfileClick: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Deliver to",
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceVariant
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = user?.defaultAddress?.shortAddress ?: "Select address",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onProfileClick) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Background
        )
    )
}

@Composable
private fun CartFab(
    itemCount: Int,
    total: String,
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        containerColor = Primary,
        contentColor = OnPrimary
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$itemCount items • $total",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
