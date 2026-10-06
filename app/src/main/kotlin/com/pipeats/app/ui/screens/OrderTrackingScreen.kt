package com.pipeats.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pipeats.app.data.local.DataStoreManager
import com.pipeats.app.data.model.Order
import com.pipeats.app.data.model.OrderStatus
import com.pipeats.app.ui.components.PiPEatsTopBar
import com.pipeats.app.ui.components.PrimaryButton
import com.pipeats.app.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingScreen(
    orderId: String,
    dataStoreManager: DataStoreManager,
    onBackClick: () -> Unit,
    onBackToHomeClick: () -> Unit
) {
    val orders by dataStoreManager.orders.collectAsState(initial = emptyList())
    val order = orders.find { it.id == orderId }
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(orderId) {
        val statuses = listOf(
            OrderStatus.CONFIRMED,
            OrderStatus.PREPARING,
            OrderStatus.ON_THE_WAY,
            OrderStatus.DELIVERED
        )
        
        for (status in statuses) {
            delay(5000)
            val currentOrder = dataStoreManager.orders.first().find { it.id == orderId }
            if (currentOrder?.status?.isActive == true) {
                dataStoreManager.updateOrderStatus(orderId, status)
            } else {
                break
            }
        }
    }
    
    Scaffold(
        topBar = {
            PiPEatsTopBar(
                title = "Track Order",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        if (order == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Order not found")
            }
        } else {
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
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceVariant
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Order #${order.id}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = order.restaurantName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = OnSurfaceVariant
                                )
                            }
                            
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = when (order.status) {
                                    OrderStatus.DELIVERED -> Success
                                    OrderStatus.CANCELLED -> Error
                                    else -> Primary
                                }.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = order.status.displayName,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = when (order.status) {
                                        OrderStatus.DELIVERED -> Success
                                        OrderStatus.CANCELLED -> Error
                                        else -> Primary
                                    },
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                        
                        if (order.status.isActive) {
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Estimated delivery: ${order.estimatedDeliveryMinutes} mins",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
                
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Order Status",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        OrderStatusStepper(currentStatus = order.status)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceVariant
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Delivery Location",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(OnSurfaceVariant.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Map,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = OnSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Map View (Mock)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = OnSurfaceVariant
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = order.deliveryAddress.label,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = order.deliveryAddress.fullAddress,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = OnSurfaceVariant
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                if (order.status == OrderStatus.DELIVERED) {
                    PrimaryButton(
                        text = "Back to Home",
                        onClick = onBackToHomeClick,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun OrderStatusStepper(currentStatus: OrderStatus) {
    val statuses = listOf(
        StatusStep(OrderStatus.PLACED, "Order Placed", "Your order has been placed"),
        StatusStep(OrderStatus.CONFIRMED, "Confirmed", "Restaurant confirmed your order"),
        StatusStep(OrderStatus.PREPARING, "Preparing", "Your food is being prepared"),
        StatusStep(OrderStatus.ON_THE_WAY, "On the Way", "Rider is on the way"),
        StatusStep(OrderStatus.DELIVERED, "Delivered", "Order delivered successfully")
    )
    
    val currentIndex = statuses.indexOfFirst { it.status == currentStatus }
    
    Column {
        statuses.forEachIndexed { index, step ->
            val isCompleted = index <= currentIndex
            val isCurrent = index == currentIndex
            
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(32.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isCompleted -> Success
                                    else -> DisabledColor
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = OnPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    
                    if (index < statuses.size - 1) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(40.dp)
                                .background(
                                    if (index < currentIndex) Success else DisabledColor
                                )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.padding(bottom = if (index < statuses.size - 1) 24.dp else 0.dp)) {
                    Text(
                        text = step.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                        color = if (isCompleted) OnSurface else DisabledColor
                    )
                    Text(
                        text = step.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isCompleted) OnSurfaceVariant else DisabledColor
                    )
                }
            }
        }
    }
}

private data class StatusStep(
    val status: OrderStatus,
    val title: String,
    val description: String
)
