package com.pipeats.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pipeats.app.data.local.DataStoreManager
import com.pipeats.app.data.model.Address
import com.pipeats.app.ui.components.PiPEatsTopBar
import com.pipeats.app.ui.components.PrimaryButton
import com.pipeats.app.ui.theme.*
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAddressScreen(
    dataStoreManager: DataStoreManager,
    onBackClick: () -> Unit,
    onSaveComplete: () -> Unit
) {
    val user by dataStoreManager.user.collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    
    var label by remember { mutableStateOf("Home") }
    var addressLine1 by remember { mutableStateOf("") }
    var addressLine2 by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var setAsDefault by remember { mutableStateOf(false) }
    
    val isValid = addressLine1.isNotBlank() && city.isNotBlank() && 
            state.isNotBlank() && pincode.length == 6
    
    Scaffold(
        topBar = {
            PiPEatsTopBar(
                title = "Add Address",
                onBackClick = onBackClick
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
                shape = RoundedCornerShape(16.dp),
                color = Surface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Address Type",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Home", "Work", "Other").forEach { type ->
                            FilterChip(
                                selected = label == type,
                                onClick = { label = type },
                                label = { Text(type) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = when (type) {
                                            "Home" -> Icons.Default.Home
                                            "Work" -> Icons.Default.Work
                                            else -> Icons.Default.LocationOn
                                        },
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Primary,
                                    selectedLabelColor = OnPrimary,
                                    selectedLeadingIconColor = OnPrimary
                                )
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    OutlinedTextField(
                        value = addressLine1,
                        onValueChange = { addressLine1 = it },
                        label = { Text("Address Line 1 *") },
                        placeholder = { Text("House/Flat No., Building, Street") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            focusedLabelColor = Primary
                        ),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    OutlinedTextField(
                        value = addressLine2,
                        onValueChange = { addressLine2 = it },
                        label = { Text("Address Line 2") },
                        placeholder = { Text("Area, Landmark (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            focusedLabelColor = Primary
                        ),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("City *") },
                        placeholder = { Text("Enter city") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            focusedLabelColor = Primary
                        ),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = state,
                            onValueChange = { state = it },
                            label = { Text("State *") },
                            placeholder = { Text("State") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Primary,
                                focusedLabelColor = Primary
                            ),
                            singleLine = true
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        OutlinedTextField(
                            value = pincode,
                            onValueChange = { 
                                if (it.length <= 6 && it.all { c -> c.isDigit() }) {
                                    pincode = it
                                }
                            },
                            label = { Text("Pincode *") },
                            placeholder = { Text("6-digit") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Primary,
                                focusedLabelColor = Primary
                            ),
                            singleLine = true,
                            isError = pincode.isNotEmpty() && pincode.length != 6
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = setAsDefault,
                            onCheckedChange = { setAsDefault = it },
                            colors = CheckboxDefaults.colors(checkedColor = Primary)
                        )
                        Text(
                            text = "Set as default address",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            PrimaryButton(
                text = "Save Address",
                onClick = {
                    scope.launch {
                        user?.let { currentUser ->
                            val newAddress = Address(
                                id = UUID.randomUUID().toString(),
                                label = label,
                                addressLine1 = addressLine1.trim(),
                                addressLine2 = addressLine2.trim(),
                                city = city.trim(),
                                state = state.trim(),
                                pincode = pincode.trim()
                            )
                            
                            val updatedAddresses = currentUser.addresses + newAddress
                            val updatedUser = currentUser.copy(
                                addresses = updatedAddresses,
                                defaultAddressId = if (setAsDefault || currentUser.addresses.isEmpty()) 
                                    newAddress.id else currentUser.defaultAddressId
                            )
                            
                            dataStoreManager.updateUser(updatedUser)
                            onSaveComplete()
                        }
                    }
                },
                enabled = isValid,
                modifier = Modifier.padding(16.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
