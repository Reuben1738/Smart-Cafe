package com.example.smartcafe

import com.example.smartcafe.data.model.MenuItem

object CartManager {
    // Track how many of each item we have
    val cartItems = mutableMapOf<MenuItem, Int>()

    fun addToCart(item: MenuItem) {
        cartItems[item] = (cartItems[item] ?: 0) + 1
    }

    fun removeFromCart(item: MenuItem) {
        val currentQty = cartItems[item] ?: 0
        if (currentQty > 1) {
            cartItems[item] = currentQty - 1
        } else {
            cartItems.remove(item)
        }
    }

    // New function to completely delete an item
    fun deleteItem(item: MenuItem) {
        cartItems.remove(item)
    }

    fun getTotalPrice(): Double {
        var total = 0.0
        for ((item, quantity) in cartItems) {
            total += item.price * quantity
        }
        return total
    }

    fun clearCart() {
        cartItems.clear()
    }
}