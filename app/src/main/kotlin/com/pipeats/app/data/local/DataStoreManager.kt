package com.pipeats.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pipeats.app.data.model.*
import com.pipeats.app.data.mock.MockData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "pip_eats_data")

class DataStoreManager(private val context: Context) {
    
    private val gson = Gson()
    
    companion object {
        private val HAS_COMPLETED_ONBOARDING = booleanPreferencesKey("has_completed_onboarding")
        private val USER_DATA = stringPreferencesKey("user_data")
        private val CART_DATA = stringPreferencesKey("cart_data")
        private val ORDERS_DATA = stringPreferencesKey("orders_data")
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }
    
    val hasCompletedOnboarding: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[HAS_COMPLETED_ONBOARDING] ?: false
    }
    
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }
    
    val user: Flow<User?> = context.dataStore.data.map { preferences ->
        val json = preferences[USER_DATA]
        if (json != null) {
            try {
                gson.fromJson(json, User::class.java)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }
    
    val cart: Flow<Cart> = context.dataStore.data.map { preferences ->
        val json = preferences[CART_DATA]
        if (json != null) {
            try {
                gson.fromJson(json, Cart::class.java)
            } catch (e: Exception) {
                Cart()
            }
        } else {
            Cart()
        }
    }
    
    val orders: Flow<List<Order>> = context.dataStore.data.map { preferences ->
        val json = preferences[ORDERS_DATA]
        if (json != null) {
            try {
                val type = object : TypeToken<List<Order>>() {}.type
                gson.fromJson(json, type)
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }
    
    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[HAS_COMPLETED_ONBOARDING] = completed
        }
    }
    
    suspend fun setLoggedIn(loggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = loggedIn
            if (loggedIn && preferences[USER_DATA] == null) {
                preferences[USER_DATA] = gson.toJson(MockData.defaultUser)
            }
        }
    }
    
    suspend fun updateUser(user: User) {
        context.dataStore.edit { preferences ->
            preferences[USER_DATA] = gson.toJson(user)
        }
    }
    
    suspend fun updateCart(cart: Cart) {
        context.dataStore.edit { preferences ->
            preferences[CART_DATA] = gson.toJson(cart)
        }
    }
    
    suspend fun clearCart() {
        context.dataStore.edit { preferences ->
            preferences[CART_DATA] = gson.toJson(Cart())
        }
    }
    
    suspend fun addOrder(order: Order) {
        context.dataStore.edit { preferences ->
            val currentOrdersJson = preferences[ORDERS_DATA]
            val currentOrders: MutableList<Order> = if (currentOrdersJson != null) {
                try {
                    val type = object : TypeToken<MutableList<Order>>() {}.type
                    gson.fromJson(currentOrdersJson, type)
                } catch (e: Exception) {
                    mutableListOf()
                }
            } else {
                mutableListOf()
            }
            currentOrders.add(0, order)
            preferences[ORDERS_DATA] = gson.toJson(currentOrders)
        }
    }
    
    suspend fun updateOrderStatus(orderId: String, status: OrderStatus) {
        context.dataStore.edit { preferences ->
            val currentOrdersJson = preferences[ORDERS_DATA]
            if (currentOrdersJson != null) {
                val type = object : TypeToken<MutableList<Order>>() {}.type
                val currentOrders: MutableList<Order> = gson.fromJson(currentOrdersJson, type)
                val index = currentOrders.indexOfFirst { it.id == orderId }
                if (index >= 0) {
                    currentOrders[index] = currentOrders[index].copy(status = status)
                    preferences[ORDERS_DATA] = gson.toJson(currentOrders)
                }
            }
        }
    }
    
    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = false
            preferences[HAS_COMPLETED_ONBOARDING] = false
            preferences[USER_DATA] = gson.toJson(MockData.defaultUser)
            preferences[CART_DATA] = gson.toJson(Cart())
        }
    }
}
