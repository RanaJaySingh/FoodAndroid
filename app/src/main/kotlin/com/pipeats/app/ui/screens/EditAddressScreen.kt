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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pipeats.app.data.local.DataStoreManager
import com.pipeats.app.data.model.Address
import com.pipeats.app.ui.components.PiPEatsTopBar
import com.pipeats.app.ui.components.PrimaryButton
import com.pipeats.app.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAddressScreen(
    addressId: String,
    dataStoreManager: DataStoreManager,
    onBackClick: () -> Unit,
    onSaveComplete: () -> Unit
) {
    val user by dataStoreManager.user.collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    val existingAddress = user?.addresses?.find { it.id == addressId }
    
    var label by remember { mutableStateOf("Home") }
    var addressLine1 by remember { mutableStateOf("") }
    var addressLine2 by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var setAsDefault by remember { mutableStateOf(false) }
    
    LaunchedEffect(existingAddress) {
        existingAddress?.let {
            label = it.label
            addressLine1 = it.addressLine1
            addressLine2 = it.addressLine2
            city = it.city
            state = it.state
            pincode = it.pincode
            setAsDefault = user?.defaultAddressId == it.id
        }
    }
    
    val isValid = addressLine1.isNotBlank() && city.isNotBlank() && 
            state.isNotBlank() && pincode.length == 6
    
    Scaffold(
        topBar = {
            PiPEatsTopBar(
                title = "Edit Address",
                onBackClick = onBackClick,
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Error
                        )
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
                        verticalAlignment = Alignment.CenterVertically
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
                text = "Save Changes",
                onClick = {
                    scope.launch {
                        user?.let { currentUser ->
                            val updatedAddress = Address(
                                id = addressId,
                                label = label,
                                addressLine1 = addressLine1.trim(),
                                addressLine2 = addressLine2.trim(),
                                city = city.trim(),
                                state = state.trim(),
                                pincode = pincode.trim(),
                                latitude = existingAddress?.latitude ?: 0.0,
                                longitude = existingAddress?.longitude ?: 0.0
                            )
                            
                            val updatedAddresses = currentUser.addresses.map {
                                if (it.id == addressId) updatedAddress else it
                            }
                            
                            val updatedUser = currentUser.copy(
                                addresses = updatedAddresses,
                                defaultAddressId = if (setAsDefault) addressId else currentUser.defaultAddressId
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
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text("Delete Address")
            },
            text = {
                Text("Are you sure you want to delete this address?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            user?.let { currentUser ->
                                val updatedAddresses = currentUser.addresses.filter { it.id != addressId }
                                val newDefaultId = if (currentUser.defaultAddressId == addressId) {
                                    updatedAddresses.firstOrNull()?.id
                                } else {
                                    currentUser.defaultAddressId
                                }
                                
                                val updatedUser = currentUser.copy(
                                    addresses = updatedAddresses,
                                    defaultAddressId = newDefaultId
                                )
                                
                                dataStoreManager.updateUser(updatedUser)
                                showDeleteDialog = false
                                onSaveComplete()
                            }
                        }
                    }
                ) {
                    Text("Delete", color = Error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
