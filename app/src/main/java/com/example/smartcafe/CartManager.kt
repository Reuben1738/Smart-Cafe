package com.example.smartcafe

import com.example.smartcafe.data.model.MenuItem

object CartManager {
    // A mutable list to hold the items the user wants to buy
    val cartItems = mutableListOf<MenuItem>()

    // Add an item to the cart
    fun addToCart(item: MenuItem) {
        cartItems.add(item)
    }

    // Remove an item from the cart
    fun removeFromCart(item: MenuItem) {
        cartItems.remove(item)
    }

    // Calculates the total price of all items in the cart
    fun getTotalPrice(): Double {
        var total = 0.0
        for (item in cartItems) {
            total += item.price
        }
        return total
    }

    // Clear the cart completely
    fun clearCart() {
        cartItems.clear()
    }
}