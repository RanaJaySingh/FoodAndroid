package com.pipeats.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.pipeats.app.ui.navigation.PiPEatsNavGraph
import com.pipeats.app.ui.theme.Background
import com.pipeats.app.ui.theme.PiPEatsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val app = application as PiPEatsApplication
        
        setContent {
            PiPEatsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Background
                ) {
                    val navController = rememberNavController()
                    PiPEatsNavGraph(
                        navController = navController,
                        dataStoreManager = app.dataStoreManager
                    )
                }
            }
        }
    }
}
