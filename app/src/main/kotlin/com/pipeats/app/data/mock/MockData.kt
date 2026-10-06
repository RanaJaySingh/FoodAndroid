package com.pipeats.app.data.mock

import com.pipeats.app.data.model.*

object MockData {
    
    val categories = listOf(
        Category("1", "All", "🍽️"),
        Category("2", "Biryani", "🍚"),
        Category("3", "Pizza", "🍕"),
        Category("4", "Burger", "🍔"),
        Category("5", "Chinese", "🥡"),
        Category("6", "South Indian", "🥘"),
        Category("7", "Desserts", "🍰"),
        Category("8", "Healthy", "🥗")
    )
    
    val restaurants = listOf(
        Restaurant(
            id = "r1",
            name = "Paradise Biryani",
            cuisine = "Biryani, Mughlai",
            rating = 4.5f,
            ratingCount = 2340,
            deliveryTimeMinutes = 30,
            deliveryFee = 25.0,
            minimumOrder = 150.0,
            imageUrl = "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=400",
            categories = listOf("Biryani")
        ),
        Restaurant(
            id = "r2",
            name = "Pizza Palace",
            cuisine = "Pizza, Italian",
            rating = 4.3f,
            ratingCount = 1856,
            deliveryTimeMinutes = 25,
            deliveryFee = 0.0,
            minimumOrder = 200.0,
            imageUrl = "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=400",
            categories = listOf("Pizza")
        ),
        Restaurant(
            id = "r3",
            name = "Burger Barn",
            cuisine = "Burgers, Fast Food",
            rating = 4.1f,
            ratingCount = 1245,
            deliveryTimeMinutes = 20,
            deliveryFee = 15.0,
            minimumOrder = 100.0,
            imageUrl = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400",
            categories = listOf("Burger")
        ),
        Restaurant(
            id = "r4",
            name = "Wok & Roll",
            cuisine = "Chinese, Thai",
            rating = 4.4f,
            ratingCount = 987,
            deliveryTimeMinutes = 35,
            deliveryFee = 30.0,
            minimumOrder = 200.0,
            imageUrl = "https://images.unsplash.com/photo-1585032226651-759b368d7246?w=400",
            categories = listOf("Chinese")
        ),
        Restaurant(
            id = "r5",
            name = "Dosa Corner",
            cuisine = "South Indian",
            rating = 4.6f,
            ratingCount = 3210,
            deliveryTimeMinutes = 25,
            deliveryFee = 20.0,
            minimumOrder = 100.0,
            imageUrl = "https://images.unsplash.com/photo-1630383249896-424e482df921?w=400",
            categories = listOf("South Indian")
        ),
        Restaurant(
            id = "r6",
            name = "Sweet Treats",
            cuisine = "Desserts, Bakery",
            rating = 4.7f,
            ratingCount = 567,
            deliveryTimeMinutes = 30,
            deliveryFee = 25.0,
            minimumOrder = 150.0,
            imageUrl = "https://images.unsplash.com/photo-1551024601-bec78aea704b?w=400",
            categories = listOf("Desserts")
        ),
        Restaurant(
            id = "r7",
            name = "Green Bowl",
            cuisine = "Healthy, Salads",
            rating = 4.2f,
            ratingCount = 432,
            deliveryTimeMinutes = 20,
            deliveryFee = 0.0,
            minimumOrder = 200.0,
            imageUrl = "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=400",
            categories = listOf("Healthy")
        ),
        Restaurant(
            id = "r8",
            name = "Royal Mughal",
            cuisine = "Biryani, North Indian",
            rating = 4.4f,
            ratingCount = 1567,
            deliveryTimeMinutes = 40,
            deliveryFee = 35.0,
            minimumOrder = 250.0,
            imageUrl = "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=400",
            categories = listOf("Biryani")
        )
    )
    
    val menuItems = mapOf(
        "r1" to listOf(
            MenuItem("m1", "r1", "Chicken Biryani", "Aromatic basmati rice with tender chicken pieces", 280.0, "Biryani", isVegetarian = false, isPopular = true),
            MenuItem("m2", "r1", "Mutton Biryani", "Slow-cooked mutton with fragrant spices", 350.0, "Biryani", isVegetarian = false, isPopular = true),
            MenuItem("m3", "r1", "Veg Biryani", "Mixed vegetables in aromatic rice", 220.0, "Biryani", isVegetarian = true),
            MenuItem("m4", "r1", "Raita", "Fresh yogurt with cucumber and spices", 60.0, "Sides", isVegetarian = true),
            MenuItem("m5", "r1", "Mirchi Ka Salan", "Spicy green chili curry", 120.0, "Sides", isVegetarian = true),
            MenuItem("m6", "r1", "Chicken 65", "Crispy fried chicken with spices", 220.0, "Starters", isVegetarian = false),
            MenuItem("m7", "r1", "Paneer 65", "Crispy fried paneer with spices", 200.0, "Starters", isVegetarian = true)
        ),
        "r2" to listOf(
            MenuItem("m8", "r2", "Margherita Pizza", "Classic tomato and mozzarella", 299.0, "Pizza", isVegetarian = true, isPopular = true),
            MenuItem("m9", "r2", "Pepperoni Pizza", "Loaded with spicy pepperoni", 399.0, "Pizza", isVegetarian = false, isPopular = true),
            MenuItem("m10", "r2", "Veggie Supreme", "Loaded with fresh vegetables", 349.0, "Pizza", isVegetarian = true),
            MenuItem("m11", "r2", "BBQ Chicken Pizza", "Tangy BBQ sauce with chicken", 449.0, "Pizza", isVegetarian = false),
            MenuItem("m12", "r2", "Garlic Bread", "Crispy bread with garlic butter", 149.0, "Sides", isVegetarian = true),
            MenuItem("m13", "r2", "Pasta Alfredo", "Creamy white sauce pasta", 279.0, "Pasta", isVegetarian = true)
        ),
        "r3" to listOf(
            MenuItem("m14", "r3", "Classic Burger", "Juicy beef patty with fresh toppings", 180.0, "Burgers", isVegetarian = false, isPopular = true),
            MenuItem("m15", "r3", "Veggie Burger", "Crispy vegetable patty", 150.0, "Burgers", isVegetarian = true),
            MenuItem("m16", "r3", "Chicken Burger", "Grilled chicken with mayo", 200.0, "Burgers", isVegetarian = false),
            MenuItem("m17", "r3", "French Fries", "Crispy golden fries", 99.0, "Sides", isVegetarian = true),
            MenuItem("m18", "r3", "Onion Rings", "Crispy battered onion rings", 120.0, "Sides", isVegetarian = true),
            MenuItem("m19", "r3", "Chocolate Shake", "Rich chocolate milkshake", 149.0, "Beverages", isVegetarian = true)
        ),
        "r4" to listOf(
            MenuItem("m20", "r4", "Veg Fried Rice", "Wok-tossed rice with vegetables", 180.0, "Rice", isVegetarian = true),
            MenuItem("m21", "r4", "Chicken Fried Rice", "Wok-tossed rice with chicken", 220.0, "Rice", isVegetarian = false, isPopular = true),
            MenuItem("m22", "r4", "Veg Manchurian", "Crispy vegetable balls in sauce", 200.0, "Starters", isVegetarian = true, isPopular = true),
            MenuItem("m23", "r4", "Chilli Chicken", "Spicy Indo-Chinese chicken", 250.0, "Starters", isVegetarian = false),
            MenuItem("m24", "r4", "Hakka Noodles", "Stir-fried noodles with vegetables", 180.0, "Noodles", isVegetarian = true),
            MenuItem("m25", "r4", "Sweet Corn Soup", "Creamy corn soup", 120.0, "Soups", isVegetarian = true)
        ),
        "r5" to listOf(
            MenuItem("m26", "r5", "Masala Dosa", "Crispy dosa with potato filling", 120.0, "Dosa", isVegetarian = true, isPopular = true),
            MenuItem("m27", "r5", "Plain Dosa", "Classic crispy dosa", 90.0, "Dosa", isVegetarian = true),
            MenuItem("m28", "r5", "Idli Sambar", "Soft idlis with sambar", 80.0, "Idli", isVegetarian = true, isPopular = true),
            MenuItem("m29", "r5", "Vada", "Crispy lentil fritters", 70.0, "Snacks", isVegetarian = true),
            MenuItem("m30", "r5", "Filter Coffee", "Traditional South Indian coffee", 50.0, "Beverages", isVegetarian = true),
            MenuItem("m31", "r5", "Rava Upma", "Semolina breakfast dish", 90.0, "Breakfast", isVegetarian = true)
        ),
        "r6" to listOf(
            MenuItem("m32", "r6", "Chocolate Brownie", "Rich fudgy brownie", 150.0, "Brownies", isVegetarian = true, isPopular = true),
            MenuItem("m33", "r6", "Gulab Jamun", "Soft milk dumplings in syrup", 100.0, "Indian Sweets", isVegetarian = true, isPopular = true),
            MenuItem("m34", "r6", "Ice Cream Sundae", "Vanilla ice cream with toppings", 180.0, "Ice Cream", isVegetarian = true),
            MenuItem("m35", "r6", "Cheesecake", "Creamy New York cheesecake", 220.0, "Cakes", isVegetarian = true),
            MenuItem("m36", "r6", "Rasmalai", "Soft paneer in sweet milk", 120.0, "Indian Sweets", isVegetarian = true)
        ),
        "r7" to listOf(
            MenuItem("m37", "r7", "Caesar Salad", "Fresh romaine with Caesar dressing", 250.0, "Salads", isVegetarian = true, isPopular = true),
            MenuItem("m38", "r7", "Greek Salad", "Mediterranean vegetables with feta", 280.0, "Salads", isVegetarian = true),
            MenuItem("m39", "r7", "Quinoa Bowl", "Protein-rich quinoa with vegetables", 320.0, "Bowls", isVegetarian = true, isPopular = true),
            MenuItem("m40", "r7", "Fresh Juice", "Seasonal fresh fruit juice", 150.0, "Beverages", isVegetarian = true),
            MenuItem("m41", "r7", "Smoothie Bowl", "Thick smoothie with granola", 280.0, "Bowls", isVegetarian = true)
        ),
        "r8" to listOf(
            MenuItem("m42", "r8", "Dum Biryani", "Slow-cooked aromatic biryani", 320.0, "Biryani", isVegetarian = false, isPopular = true),
            MenuItem("m43", "r8", "Paneer Biryani", "Rich paneer biryani", 280.0, "Biryani", isVegetarian = true),
            MenuItem("m44", "r8", "Kebab Platter", "Assorted grilled kebabs", 450.0, "Starters", isVegetarian = false, isPopular = true),
            MenuItem("m45", "r8", "Dal Makhani", "Creamy black lentils", 180.0, "Curries", isVegetarian = true),
            MenuItem("m46", "r8", "Naan", "Fresh tandoor bread", 40.0, "Breads", isVegetarian = true),
            MenuItem("m47", "r8", "Butter Chicken", "Creamy tomato chicken curry", 280.0, "Curries", isVegetarian = false)
        )
    )
    
    val defaultUser = User(
        id = "u1",
        name = "Rahul Kumar",
        phone = "+91 9876543210",
        email = "rahul@example.com",
        addresses = listOf(
            Address(
                id = "a1",
                label = "Home",
                addressLine1 = "123, Green Park",
                addressLine2 = "Near Metro Station",
                city = "New Delhi",
                state = "Delhi",
                pincode = "110016",
                latitude = 28.5562,
                longitude = 77.2099
            ),
            Address(
                id = "a2",
                label = "Work",
                addressLine1 = "456, Cyber Hub",
                addressLine2 = "DLF Phase 2",
                city = "Gurugram",
                state = "Haryana",
                pincode = "122002",
                latitude = 28.4956,
                longitude = 77.0887
            )
        ),
        defaultAddressId = "a1"
    )
    
    fun getRestaurantById(id: String): Restaurant? = restaurants.find { it.id == id }
    
    fun getMenuForRestaurant(restaurantId: String): List<MenuItem> = 
        menuItems[restaurantId] ?: emptyList()
    
    fun getMenuCategoriesForRestaurant(restaurantId: String): List<MenuCategory> {
        val items = getMenuForRestaurant(restaurantId)
        return items.groupBy { it.category }
            .map { (category, items) -> MenuCategory(category, items) }
    }
    
    fun filterRestaurants(query: String, categoryFilter: String?): List<Restaurant> {
        var result = restaurants
        
        if (query.isNotBlank()) {
            result = result.filter { 
                it.name.contains(query, ignoreCase = true) || 
                it.cuisine.contains(query, ignoreCase = true) 
            }
        }
        
        if (categoryFilter != null && categoryFilter != "All") {
            result = result.filter { it.categories.contains(categoryFilter) }
        }
        
        return result
    }
}
