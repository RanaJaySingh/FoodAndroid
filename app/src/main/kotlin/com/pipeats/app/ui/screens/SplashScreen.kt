package com.pipeats.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pipeats.app.data.local.DataStoreManager
import com.pipeats.app.ui.theme.OnPrimary
import com.pipeats.app.ui.theme.Primary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    dataStoreManager: DataStoreManager,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val hasCompletedOnboarding by dataStoreManager.hasCompletedOnboarding.collectAsState(initial = null)
    
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    LaunchedEffect(hasCompletedOnboarding) {
        delay(2000)
        when (hasCompletedOnboarding) {
            true -> onNavigateToHome()
            false -> onNavigateToOnboarding()
            null -> {}
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🍽️",
                fontSize = 80.sp,
                modifier = Modifier.alpha(alpha)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "PiP Eats",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = OnPrimary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Delicious food, delivered fast",
                style = MaterialTheme.typography.bodyLarge,
                color = OnPrimary.copy(alpha = 0.8f)
            )
        }
    }
}
