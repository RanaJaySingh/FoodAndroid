# PiP Eats - Android Consumer MVP

A greenfield native Android food delivery consumer app MVP built with Kotlin and Jetpack Compose.

## Overview

**Brand:** PiP Eats  
**Market:** India-first, English UI  
**Stack:** Kotlin, Jetpack Compose, Navigation Compose, ViewModel, DataStore

## Features

This MVP includes 7 screens implementing the core consumer food delivery flow:

### T1: Splash + Onboarding (Location)
- Animated splash screen with PiP Eats branding
- Location permission request with allow/deny paths
- Manual address entry option for users who deny location
- Returning users skip directly to Home

### T2: Home - Restaurants, Search, Categories
- List of nearby mock restaurants with image, name, cuisine, rating, ETA, delivery fee
- Search functionality filtering by restaurant name or cuisine
- Horizontal category chips for filtering (All, Biryani, Pizza, Burger, Chinese, South Indian, Desserts, Healthy)
- Empty state for no results
- Cart FAB with item count and total when cart has items

### T3: Restaurant Detail + Menu
- Restaurant header with image, name, rating, delivery time, fee
- Menu grouped by category sections
- Veg/Non-veg indicators for each item
- Add/remove items with quantity steppers
- "Bestseller" badges for popular items
- Unavailable items disabled with label
- View Cart bar at bottom when cart has items

### T4: Cart + Checkout (Payment Mocked)
- Line items with quantity steppers
- Bill details breakdown (subtotal, delivery fee, taxes, total)
- Empty cart state with browse CTA
- Checkout collects delivery address and phone
- Address selector for users with multiple addresses
- Mock "Cash on Delivery" payment (clearly labeled as mock)
- Form validation before submission

### T5: Order Confirmation + Tracking (Mocked)
- Order confirmation with celebration animation
- Order ID, restaurant name, ETA, total display
- Delivery address summary
- Order tracking with status stepper (Placed → Confirmed → Preparing → On the Way → Delivered)
- Mock status progression (auto-advances every 5 seconds for demo)
- Map placeholder (mock)

### T6: Order History
- List of past orders (newest first)
- Order card showing restaurant, date, status, item count, total
- Tap to view order details
- Empty state when no orders

### T7: Profile
- User info display (name, phone, email)
- Edit profile functionality
- Saved addresses management (add/edit/delete)
- Set default address
- Logout returns to onboarding/login stub

## Data Models

- **User**: id, name, phone, email, addresses, defaultAddressId
- **Address**: id, label, addressLine1, addressLine2, city, state, pincode, latitude, longitude
- **Restaurant**: id, name, cuisine, rating, ratingCount, deliveryTimeMinutes, deliveryFee, imageUrl, categories
- **MenuItem**: id, restaurantId, name, description, price, category, imageUrl, isVegetarian, isAvailable, isPopular
- **Cart**: restaurantId, restaurantName, items (CartLine[])
- **CartLine**: menuItemId, name, price, quantity, isVegetarian
- **Order**: id, restaurantId, restaurantName, items, subtotal, deliveryFee, taxes, total, status, deliveryAddress, phone, createdAt, estimatedDeliveryMinutes
- **OrderStatus**: PLACED, CONFIRMED, PREPARING, ON_THE_WAY, DELIVERED, CANCELLED

## Local Persistence

Using Jetpack DataStore (Preferences) for:
- Onboarding completion state
- User data
- Cart state (persists across app restarts)
- Order history (persists across app restarts)

## Mock Data

The app includes comprehensive mock fixtures:
- 8 restaurants across various cuisines
- 40+ menu items with categories
- Default user with 2 sample addresses
- Category chips for filtering

## Project Structure

```
app/src/main/kotlin/com/pipeats/app/
├── PiPEatsApplication.kt       # Application class
├── MainActivity.kt             # Single activity entry point
├── data/
│   ├── local/
│   │   └── DataStoreManager.kt # Local persistence
│   ├── mock/
│   │   └── MockData.kt         # Mock fixtures
│   └── model/                  # Data models
│       ├── Address.kt
│       ├── Cart.kt
│       ├── Category.kt
│       ├── MenuItem.kt
│       ├── Order.kt
│       ├── Restaurant.kt
│       └── User.kt
└── ui/
    ├── components/
    │   └── CommonComponents.kt # Reusable UI components
    ├── navigation/
    │   ├── NavRoutes.kt        # Navigation routes
    │   └── PiPEatsNavGraph.kt  # Navigation graph
    ├── screens/                # All screen composables
    │   ├── SplashScreen.kt
    │   ├── OnboardingScreen.kt
    │   ├── HomeScreen.kt
    │   ├── RestaurantDetailScreen.kt
    │   ├── CartScreen.kt
    │   ├── CheckoutScreen.kt
    │   ├── OrderConfirmationScreen.kt
    │   ├── OrderTrackingScreen.kt
    │   ├── OrderHistoryScreen.kt
    │   ├── OrderDetailScreen.kt
    │   ├── ProfileScreen.kt
    │   ├── EditProfileScreen.kt
    │   ├── AddAddressScreen.kt
    │   └── EditAddressScreen.kt
    └── theme/                  # Material 3 theming
        ├── Color.kt
        ├── Theme.kt
        └── Type.kt
```

## Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34

### Building the App

1. Clone the repository:
   ```bash
   git clone https://github.com/RanaJaySingh/FoodAndroid.git
   cd FoodAndroid
   ```

2. Open the project in Android Studio

3. Sync Gradle dependencies (Android Studio will prompt automatically)

4. Build the project:
   - **Menu:** Build → Make Project
   - Or use the green hammer icon in the toolbar

5. Run the app:
   - Connect an Android device (API 26+) or start an emulator
   - Click the green play button or use Run → Run 'app'

### Configuration

The app is pre-configured with:
- `minSdk = 26` (Android 8.0)
- `targetSdk = 34` (Android 14)
- Kotlin 1.9.21
- Jetpack Compose BOM 2023.10.01
- Navigation Compose 2.7.5
- DataStore 1.0.0
- Coil 2.5.0 (image loading)
- Gson 2.10.1 (JSON serialization)

## Out of Scope

The following are intentionally not implemented in this MVP:
- Real payments / payment gateway integration
- Live maps / real-time ETA tracking
- Rider chat functionality
- Internationalization (i18n)
- Dark mode polish
- Real backend / network API
- Authentication system (FE-010 is separate)

## Brand Colors

- **Primary:** #FF6B35 (Orange)
- **Secondary:** #2EC4B6 (Teal)
- **Success:** #28A745 (Green)
- **Error:** #DC3545 (Red)
- **Veg Indicator:** #0F8A3E
- **Non-Veg Indicator:** #B5485F

## License

Proprietary - Internal use only
